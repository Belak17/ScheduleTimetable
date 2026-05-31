package com.belak.scheduletimetable.service.courstp;

import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Salle;
import com.belak.scheduletimetable.repository.SalleRepository;
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

        Pattern pattern = Pattern.compile("\\(\\s*TP\\s*/\\s*([^\\)]+?)\\s*\\)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {

            String freq = matcher.group(1)
                    .toLowerCase()
                    .replaceAll("\\s+", ""); // enlève espaces

            return "TP/" + freq;
        }

        return null;
    }
}
