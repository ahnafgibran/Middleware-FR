package id.grit.facereco.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUtilService {

    public static BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) {
        return Scalr.resize(originalImage, targetWidth);
    }

    public static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] resizeFileImage(MultipartFile imageData) throws IOException {
        BufferedImage bi = ImageUtilService.createImageFromBytes(imageData.getBytes());
        BufferedImage newBi = ImageUtilService.simpleResizeImage(bi, 800);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String fileType = "jpg";
        try {
            if (imageData.getContentType().contains("png") || imageData.getOriginalFilename().contains("png")) {
                fileType = "png";
            }
        } catch (Exception e) {
        }
        ImageIO.write(newBi, fileType, baos);
        byte[] newBytes = baos.toByteArray();
        return newBytes;
    }

    public static byte[] resizeByteImage(byte[] imageData) throws IOException {
        BufferedImage bi = ImageUtilService.createImageFromBytes(imageData);
        BufferedImage newBi = ImageUtilService.simpleResizeImage(bi, 800);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String fileType = "jpg";
        ImageIO.write(newBi, fileType, baos);
        byte[] newBytes = baos.toByteArray();
        return newBytes;
    }
}

