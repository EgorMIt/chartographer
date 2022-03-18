package com.example.charta.utils;

import com.example.charta.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.util.Objects;

/**
 * Логика работы с сохраненим и чтением файлов
 *
 * @author Egor Mitrofanov
 */
public class FileUtils {

    private static final String EXTENSION = ".bmp";
    private static final String DEFAULT_NAME = "Image";

    /**
     * Получение файла изображения по ID
     *
     * @param id ID изображения
     * @return file нового изображения
     */
    public static File getImageByID(int id) {
        if (checkImageInStorage(id)) {
            URI existingPath = URI.create(DEFAULT_NAME + id + EXTENSION);
            return new File(GlobalVariable.path + existingPath);
        } else {
            throw new NotFoundException("Image с таким ID не найден");
        }
    }

    /**
     * Сохранение BufferedImage в файл
     *
     * @param bufferedImage BufferedImage изображения
     * @param fileName      имя файла
     */
    public static void saveImage(BufferedImage bufferedImage, String fileName) {
        try {
            ImageIO.write(bufferedImage, "bmp", new File(GlobalVariable.path + fileName + EXTENSION));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Локальное сохранение фрагмента и перевод в BufferedImage
     *
     * @param multipartFile MultipartFile фрагмента
     * @param fileName      имя файла
     * @return BufferedImage  фрагмента
     */
    public static BufferedImage saveFragment(MultipartFile multipartFile, String fileName) {
        try {
            File tmpFile = new File(GlobalVariable.path + fileName + EXTENSION);
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

    /**
     * Проверка на существование файла с таким ID
     *
     * @param id ID изображения
     * @return boolean существует ли изображение
     */
    public static boolean checkImageInStorage(int id) {
        try {
            URI existingPath = URI.create(DEFAULT_NAME + id + EXTENSION);
            ImageIO.read(new File(GlobalVariable.path + existingPath));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Получение BufferedImage изображения по ID
     *
     * @param id ID изображения
     * @return BufferedImage изображения
     */
    public static BufferedImage getBufferedImage(int id) {
        if (checkImageInStorage(id)) {
            try {
                return ImageIO.read(getImageByID(id));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new NotFoundException("Image с таким ID не найден");
        }
        return null;
    }
}
