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
import java.io.File;

@Service
@RequiredArgsConstructor
public class SalleService {
    private  final SalleRepository salleRepository ;
    public byte[] generateQrCode(Salle salle) {
        try {
            String text = salle.getCode();

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

            //  1. Créer dossier s'il n'existe pas
            File directory = new File("codeqrsalle");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            //  2. Nom du fichier
            File file = new File(directory, salle.getCode() + ".png");

            //  3. Sauvegarder en PNG
            ImageIO.write(image, "png", file);

            //  4. Conversion en byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}
