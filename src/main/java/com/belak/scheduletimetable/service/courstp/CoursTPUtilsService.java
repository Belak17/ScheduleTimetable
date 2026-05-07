package com.belak.scheduletimetable.service.courstp;

import com.belak.scheduletimetable.model.CoursTP;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CoursTPUtilsService {

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
}
