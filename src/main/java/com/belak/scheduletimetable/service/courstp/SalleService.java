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
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.RenderingHints;
@Service
@RequiredArgsConstructor
public class SalleService {
    private  final SalleRepository salleRepository ;
    public byte[] generateQrCode(Salle salle) {
        try {
            String code = salle.getCode();

            String text = "https://172.20.96.216:8082/api/qr/scan?code=" + code;

            int qrSize = 300;

            // Dimensions de la carte
            int width = 400;
            int height = 500;

            BufferedImage image = new BufferedImage(
                    width,
                    height,
                    BufferedImage.TYPE_INT_RGB
            );

            Graphics2D graphics = image.createGraphics();

            // Anti-aliasing pour le texte
            graphics.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            // -------------------------
            // BACKGROUND
            // -------------------------

            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);

            // -------------------------
            // TITRE
            // -------------------------

            graphics.setColor(new Color(37, 99, 235));
            graphics.setFont(new Font("Arial", Font.BOLD, 24));

            String title = "QR ATTENDANCE";

            FontMetrics titleMetrics = graphics.getFontMetrics();

            int titleX = (width - titleMetrics.stringWidth(title)) / 2;

            graphics.drawString(title, titleX, 45);

            // -------------------------
            // QR CODE
            // -------------------------

            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    qrSize,
                    qrSize
            );

            int qrX = (width - qrSize) / 2;
            int qrY = 70;

            for (int x = 0; x < qrSize; x++) {
                for (int y = 0; y < qrSize; y++) {

                    if (bitMatrix.get(x, y)) {
                        image.setRGB(
                                qrX + x,
                                qrY + y,
                                Color.BLACK.getRGB()
                        );
                    } else {
                        image.setRGB(
                                qrX + x,
                                qrY + y,
                                Color.WHITE.getRGB()
                        );
                    }
                }
            }

            // -------------------------
            // LABEL "SALLE"
            // -------------------------

            graphics.setColor(Color.GRAY);
            graphics.setFont(new Font("Arial", Font.PLAIN, 16));

            String salleLabel = "SALLE";

            FontMetrics salleMetrics = graphics.getFontMetrics();

            int salleX =
                    (width - salleMetrics.stringWidth(salleLabel)) / 2;

            graphics.drawString(
                    salleLabel,
                    salleX,
                    405
            );

            // -------------------------
            // CODE SALLE
            // -------------------------

            graphics.setColor(new Color(37, 99, 235));
            graphics.setFont(new Font("Arial", Font.BOLD, 32));

            String salleCode = code;

            FontMetrics codeMetrics = graphics.getFontMetrics();

            int codeX =
                    (width - codeMetrics.stringWidth(salleCode)) / 2;

            graphics.drawString(
                    salleCode,
                    codeX,
                    440
            );

            // -------------------------
            // INSTRUCTION
            // -------------------------

            graphics.setColor(Color.DARK_GRAY);
            graphics.setFont(new Font("Arial", Font.PLAIN, 14));

            String instruction =
                    "Scannez le QR code pour valider votre présence";

            FontMetrics instructionMetrics =
                    graphics.getFontMetrics();

            int instructionX =
                    (width - instructionMetrics.stringWidth(instruction)) / 2;

            graphics.drawString(
                    instruction,
                    instructionX,
                    475
            );

            graphics.dispose();

            // -------------------------
            // DOSSIER
            // -------------------------

            File directory = new File("codeqrsalle");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            // -------------------------
            // SAUVEGARDE
            // -------------------------

            File file = new File(
                    directory,
                    code + ".png"
            );

            ImageIO.write(
                    image,
                    "png",
                    file
            );

            // -------------------------
            // BYTE[]
            // -------------------------

            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            ImageIO.write(
                    image,
                    "png",
                    baos
            );

            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}
