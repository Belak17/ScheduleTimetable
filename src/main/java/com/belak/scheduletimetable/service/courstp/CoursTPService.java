package com.belak.scheduletimetable.service.courstp;

import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.GroupTimetable;
import com.belak.scheduletimetable.repository.CoursTPRepository;
import com.belak.scheduletimetable.repository.GroupTimetableRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spire.xls.Worksheet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CoursTPService {
    private final CoursTPRepository coursTPrepository;
    private  final GroupTimetableRepository timetableRepository ;
    public String extractGroupSalle(String text) {

        if (text == null) return null;

        Pattern pattern = Pattern.compile("[A-Z][0-9](\\.[0-9])?");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(); // retourne B1.2 par exemple
        }

        return null;
    }
    List<String> extractCourses(String value) {

        if (value == null) return List.of();

        value = value.replace('\u00A0', ' ');

        Pattern pattern = Pattern.compile(".*?\\(TP\\s*/?\\s*[^\\)]+\\)");
        Matcher matcher = pattern.matcher(value);

        List<String> result = new ArrayList<>();

        while (matcher.find()) {
            result.add(matcher.group().trim());
        }

        return result;
    }
    public String extractTPFrequency(String text) {

        if (text == null) return null;

        Pattern pattern = Pattern.compile("\\(\\s*TP\\s*/?\\s*([^\\)]+)\\)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {

            String freq = matcher.group(1)
                    .trim()
                    .replaceAll("\\s+", ""); // enlève tous les espaces

            return "TP/" + freq;
        }

        return null;
    }
    public byte[] generateQrCode(CoursTP coursTP) {
        try {
            String text = extractGroupSalle(coursTP.getIntitule());
            int width = 300;
            int height = 300;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }
            //  Conversion image -> byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            // Stockage dans ton entité
            //coursTP.setCodeQr(imageBytes);
            return imageBytes ;
            //  Sauvegarde DB
           // coursTPrepository.save(coursTP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
    private CoursTP buildTP(String value, String dayRaw, String start, String end) {
        CoursTP tp = new CoursTP();
        tp.setIntitule(value);
        tp.setDayOfWeek(dayRaw);
        tp.setDebut(LocalTime.parse(start));
        tp.setFin(LocalTime.parse(end));
        tp.setCodeQr(generateQrCode(tp));
        tp.setQrData(extractGroupSalle(value));

        String frequency = extractTPFrequency(value);
        if (frequency == null) {
            tp.setFrequence(1); // défaut
        }
        else if (frequency.contains("15")) tp.setFrequence(2);
        else if (frequency.contains("3s")) tp.setFrequence(3);
        else tp.setFrequence(1);

        return tp;
    }
    public void extractAllCoursTPforOneGroup(Worksheet sheet, GroupTimetable timetable) {

        for (int row = 10; row <= 15; row++) {

            String hourRaw = sheet.getCellRange(row, 4).getValue();
            if (hourRaw == null || !hourRaw.contains("à")) continue;

            hourRaw = hourRaw.replace("de", "").trim();

            String[] parts = hourRaw.split("à");
            String start = parts[0].trim();
            String end = parts[1].trim();

            for (int col = 5; col <= 15; col++) {

                String value = sheet.getCellRange(row, col).getValue();
                if (value == null || value.isEmpty()) continue;

                String dayRaw = sheet.getCellRange(9, col).getValue();
                if (dayRaw == null || dayRaw.isBlank()) {
                    dayRaw = sheet.getCellRange(10, col).getValue();
                }

                if (value.contains("TP")) {
                    List<String> courses = extractCourses(value);

                    //  : plusieurs TP dans la cellule
                    if (courses.size() > 1) {

                        for (int j = 0; j < courses.size(); j++) {

                            CoursTP tp = buildTP(courses.get(j), dayRaw, start, end);
                            tp.setRotationOffset(j); // 0,1 (plus logique que j+1)

                            coursTPrepository.save(tp);
                            timetable.addCoursTP(tp);
                        }

                    }
                    else {
                        CoursTP tp = buildTP(value, dayRaw, start, end);
                        coursTPrepository.save(tp);
                        timetable.addCoursTP(tp);
                    }
                }
            }
        }

        timetableRepository.save(timetable);
    }
}