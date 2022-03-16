package com.example.charta.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;

public class FileUtils {

    private static final String EXTENSION = ".bmp";
    private static final String REPOSITORY = "images/";
    private static final String DEFAULT_NAME = "Image";


    public File getImageByID(int id) {
        URI existingPath = URI.create(DEFAULT_NAME + id + EXTENSION);
        return new File(REPOSITORY + existingPath);
    }

    public void saveImage(BufferedImage bufferedImage, String fileName) {
        try {
            ImageIO.write(bufferedImage, "bmp", new File(REPOSITORY + fileName + EXTENSION));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage saveFragment(MultipartFile multipartFile, String fileName) {
        try {
            File tmpFile = new File(REPOSITORY + fileName + EXTENSION);
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(tmpFile));
            stream.write(multipartFile.getBytes());
            stream.close();

            return ImageIO.read(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkImageInStorage(int id) {
        try {
            URI existingPath = URI.create(DEFAULT_NAME + id + EXTENSION);
            ImageIO.read(new File(REPOSITORY + existingPath));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public BufferedImage getBufferedImage(int id) {
        try {
            return ImageIO.read(getImageByID(id));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
