package uz.isystem.Certificate.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isystem.Certificate.exception.BadRequest;
import uz.isystem.Certificate.model.Certificate;
import uz.isystem.Certificate.repository.CertificateRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DirectionService directionService;
    private String assets = System.getProperty("user.dir") + "/assets/";

    public boolean create(Certificate certificate) {
        String token = UUID.randomUUID().toString();
        String YMD = getYMD();
        String urlQR = "http://localhost:8080/certificates/" + token;
        String url = "http://localhost:8080/api/v1/certificates/get" + token;

        System.out.println(assets);

        BufferedImage QR = generateQR(urlQR);


        String path =assets + YMD + "/" + token + ".png";

        File folder = new File(assets + YMD);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        certificate.setUser(userService.getById(certificate.getUserId()));
        certificate.setDirection(directionService.getById(certificate.getDirectionId()));
        // RASMGA OZGARTIRILISHLA KIRITILINADI
        generateImage(QR, certificate, path);

        // DB save
        saveCertificateToDb(token, url, urlQR, path, certificate);

        return true;
    }

    private void saveCertificateToDb(String token, String url, String urlQR, String path, Certificate certificate) {
        certificate.setToken(token);
        certificate.setUrl(url);
        certificate.setPath(path);
        certificate.setUrlQR(urlQR);
        certificateRepository.save(certificate);
    }

    private void generateImage(BufferedImage qr, Certificate certificate, String path) {
        String originalPath = assets + "certificate.jpg";
        BufferedImage image;
        try {
            image = ImageIO.read(new File(originalPath));
            Graphics2D graphics = image.createGraphics();

            int x;
            int y;

            Font font = new Font("Serif", Font.BOLD, 120);
            graphics.setFont(font);
            graphics.setColor(Color.BLACK);
            String firstname = certificate.getUser().getFirstname();
            String lastname = certificate.getUser().getLastname();

            FontMetrics fontMetrics = graphics.getFontMetrics(font);
            Rectangle rectangle = new Rectangle();
            rectangle.x = 270;
            rectangle.y = 620;
            rectangle.width = 683;
            rectangle.height = 95;
            x = rectangle.x + (rectangle.width - fontMetrics.stringWidth(firstname+" "+lastname)) / 2;
            y = rectangle.y + rectangle.height;
            graphics.drawString(firstname+ " " +lastname, x, y );
//            x = rectangle.x + (rectangle.width - fontMetrics.stringWidth(lastname)) ;
//            y = rectangle.y + rectangle.height;
//            graphics.drawString(lastname, x, y);


            font = new Font("Serif", Font.BOLD, 100);
            graphics.setFont(font);
            graphics.setColor(Color.WHITE);
            fontMetrics = graphics.getFontMetrics(font);

            String direction = certificate.getDirection().getName();

            rectangle = new Rectangle();
            rectangle.x = 1750;
            rectangle.y = 2066;
            rectangle.width = 650;
            rectangle.height = 200;

            x = rectangle.x + (rectangle.width - fontMetrics.stringWidth(direction)) / 2;
            y = rectangle.y + rectangle.height - 50;

            graphics.drawString(direction, x, y);

            graphics.drawImage(qr, 1565, 1000, qr.getWidth(), qr.getHeight(), null);

            graphics.dispose();

            File file = new File(path);
            ImageIO.write(image, "png", file);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // QR rasm yaratish uchun funksiya
    public BufferedImage generateQR(String url) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 350, 350);
            ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "png", new File(assets + "qr" + ".png"));
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getYMD() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return String.format("%s/%s/%s", year, month + 1, day);
    }

    public byte[] get(String token) {
        try {
            Certificate entity = getCertificate(token);
            String imagePath = entity.getPath();

            byte[] imageInByte;
            BufferedImage originalImage;
            try {
                originalImage = ImageIO.read(new File(imagePath));
            } catch (Exception e) {
                return new byte[0];
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private Certificate getCertificate(String token) {
        Optional<Certificate> optional = certificateRepository.findByTokenAndDeletedAtIsNull(token);
        if (optional.isEmpty()) {
            throw new BadRequest("Certificate not found");
        }
        return optional.get();
    }

    public List<Certificate> getAll() {
        return certificateRepository.findAll();
    }
}