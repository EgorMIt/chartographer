package com.example.charta.service.impl;

import com.example.charta.errors.BadRequestException;
import com.example.charta.errors.NotFoundException;
import com.example.charta.model.FragmentDto;
import com.example.charta.model.ImageDto;
import com.example.charta.service.ChartaService;
import com.example.charta.utils.FileUtils;
import com.example.charta.utils.GlobalVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Логика сервиса работы с изображениями
 *
 * @author Egor Mitrofanov
 */
@Service
@ConfigurationPropertiesScan("com.example.charta.utils")
@RequiredArgsConstructor
public class ChartaServiceImpl implements ChartaService {

    private static int idCount = 1;
    private static final String DEFAULT_NAME = "Image";

    private final GlobalVariable globalVariable;

    /**
     * Создание нового пустого изображения
     *
     * @param width  ширина изображения
     * @param height высота изображения
     * @return ImageDto нового изображения
     * @throws BadRequestException при невыполнении условия
     */
    @Override
    public ImageDto createNewImage(int width, int height) {
        if (width <= Integer.parseInt(globalVariable.getImageWidthMax())
                && height <= Integer.parseInt(globalVariable.getImageHeightMax())) {
            BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

            Graphics2D g2d = (Graphics2D) newBufferedImage.getGraphics();
            g2d.setBackground(Color.BLACK);
            g2d.dispose();

            while (FileUtils.checkImageInStorage(idCount)) {
                idCount++;
            }

            String fileName = DEFAULT_NAME + idCount;
            ImageDto newImage = new ImageDto(idCount, fileName, width, height);
            idCount++;

            FileUtils.saveImage(newBufferedImage, fileName);

            return newImage;
        } else {
            throw new BadRequestException("Некорректные данные: Размер изображения превышает ограничения");
        }
    }

    /**
     * Получение фрагмента изображения
     *
     * @param id   ID изображения
     * @param fDto FragmentDto фрагмент
     * @return BufferedImage полученного фрагмента
     * @throws BadRequestException при невыполнении условия
     * @throws NotFoundException   при неверном ID
     */
    @Override
    public BufferedImage getFragment(int id, FragmentDto fDto) {
        if (FileUtils.checkImageInStorage(id)) {
            /*
             * Проверка на размер фрагмента
             */
            if (fDto.getWidth() <= Integer.parseInt(globalVariable.getFragmentWidthMax())
                    && fDto.getHeight() <= Integer.parseInt(globalVariable.getFragmentHeightMax())) {
                BufferedImage currentImage = FileUtils.getBufferedImage(id);
                BufferedImage fragment;

                /*
                 * Проверка на то, входит ли часть изображения во фрагмент
                 */
                assert currentImage != null;
                if (fDto.getStartX() < currentImage.getWidth() && fDto.getStartY() < currentImage.getHeight()
                        && (fDto.getStartX() + fDto.getWidth() > 0) && (fDto.getStartY() + fDto.getHeight() > 0)) {
                    /*
                     * Обработка фрагмента, включающего изображение целиком
                     */
                    if (fDto.getStartX() < 0 && fDto.getStartY() < 0 && fDto.getStartX() + fDto.getWidth() > currentImage.getWidth()
                            && fDto.getStartY() + fDto.getHeight() > currentImage.getHeight()) {
                        String fileName = "tmpGetFragment";
                        FileUtils.saveImage(currentImage, fileName);
                        return currentImage;
                    } else {
                        /*
                         * Обработка фрагмента, захватывающего только правую или верхнюю сторону
                         */
                        if (fDto.getStartX() + fDto.getWidth() > currentImage.getWidth()
                                || fDto.getStartY() + fDto.getHeight() > currentImage.getHeight()) {
                            int startX = fDto.getStartX();
                            int startY = fDto.getStartY();

                            int inputX = 0;
                            int inputY = 0;

                            int newWidth = fDto.getStartX() + fDto.getWidth() > currentImage.getWidth() ? currentImage.getWidth() - fDto.getStartX() : fDto.getWidth();
                            int newHeight = fDto.getStartY() + fDto.getHeight() > currentImage.getHeight() ? currentImage.getHeight() - fDto.getStartY() : fDto.getHeight();

                            /*
                             * Обработка фрагмента, захватывающего левый верхний угол
                             */
                            if (fDto.getStartX() < 0) {
                                newWidth = fDto.getWidth() - Math.abs(fDto.getStartX());
                                startX = 0;
                                inputX = fDto.getWidth() - newWidth;
                            }

                            /*
                             * Обработка фрагмента, захватывающего правый нижний угол
                             */
                            if (fDto.getStartY() < 0) {
                                newHeight = fDto.getHeight() - Math.abs(fDto.getStartY());
                                startY = 0;
                                inputY = fDto.getHeight() - newHeight;
                            }
                            BufferedImage existingFragment = currentImage.getSubimage(startX,
                                    startY, newWidth, newHeight);

                            fragment = new BufferedImage(fDto.getWidth(), fDto.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                            Graphics2D g2d = (Graphics2D) fragment.getGraphics();
                            g2d.drawImage(existingFragment, inputX, inputY, null);

                        }
                        /*
                         * Обработка фрагмента, захватывающего только левую или нижнюю сторону
                         */
                        else if (fDto.getStartX() < 0 || fDto.getStartY() < 0) {
                            int newWidth = fDto.getWidth();
                            int newHeight = fDto.getHeight();
                            int startX = 0;
                            int startY = 0;
                            BufferedImage existingFragment;

                            /*
                             * Обработка фрагмента, захватывающего только левую сторону
                             */
                            if (fDto.getStartX() < 0 && fDto.getStartY() >= 0) {
                                newWidth = fDto.getWidth() - Math.abs(fDto.getStartX());
                                startX = fDto.getWidth() - newWidth;

                                existingFragment = currentImage.getSubimage(0, fDto.getStartY(), newWidth, newHeight);
                            }
                            /*
                             * Обработка фрагмента, захватывающего только нижнюю сторону
                             */
                            else if (fDto.getStartX() >= 0 && fDto.getStartY() < 0) {
                                newHeight = fDto.getHeight() - Math.abs(fDto.getStartY());
                                startY = fDto.getHeight() - newHeight;

                                existingFragment = currentImage.getSubimage(fDto.getStartX(), 0, newWidth, newHeight);
                            }
                            /*
                             * Обработка фрагмента, захватывающего нижний левый угол
                             */
                            else {
                                newWidth = fDto.getWidth() - Math.abs(fDto.getStartX());
                                newHeight = fDto.getHeight() - Math.abs(fDto.getStartY());
                                startX = fDto.getWidth() - newWidth;
                                startY = fDto.getHeight() - newHeight;

                                existingFragment = currentImage.getSubimage(0, 0, newWidth, newHeight);
                            }


                            fragment = new BufferedImage(fDto.getWidth(), fDto.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                            Graphics2D g2d = (Graphics2D) fragment.getGraphics();
                            g2d.drawImage(existingFragment, startX, startY, null);
                        }
                        /*
                         * Обработка фрагмента, не захватывающего части вне изображения
                         */
                        else {
                            fragment = currentImage.getSubimage(fDto.getStartX(),
                                    fDto.getStartY(), fDto.getWidth(), fDto.getHeight());
                        }
                        String fileName = "tmpGetFragment";
                        FileUtils.saveImage(fragment, fileName);

                        return fragment;
                    }
                } else throw new BadRequestException("Некорректные данные: Фрагмент за пределами изображения");
            } else throw new BadRequestException("Некорректные данные: Размер изображения превышает ограничения");
        } else throw new NotFoundException("Image с таким ID не найден");
    }

    /**
     * Удаление изображения
     *
     * @param id ID изображения
     * @throws NotFoundException при неверном ID
     */
    @Override
    public void deleteImageByID(int id) {
        if (FileUtils.checkImageInStorage(id)) {
            if (FileUtils.getImageByID(id).delete()) {
                idCount = id;
            }
        } else {
            throw new NotFoundException("Image с таким ID не найден");
        }
    }

    /**
     * Вставка фрагмента в изображения
     *
     * @param id   ID изображения
     * @param fDto FragmentDto фрагмента
     * @throws BadRequestException при невыполнении условия
     * @throws NotFoundException   при неверном ID
     */
    @Override
    public void insertFragment(int id, FragmentDto fDto) {
        if (FileUtils.checkImageInStorage(id)) {
            if (fDto.getWidth() <= Integer.parseInt(globalVariable.getFragmentWidthMax())
                    && fDto.getHeight() <= Integer.parseInt(globalVariable.getFragmentHeightMax())) {
                if (!fDto.getMultipartFile().isEmpty()) {
                    BufferedImage currentImage = FileUtils.getBufferedImage(id);

                    assert currentImage != null;
                    if (fDto.getStartX() < currentImage.getWidth()
                            && fDto.getStartY() < currentImage.getHeight()) {
                        String tmpFileName = "tmpFragment";
                        String fileName = DEFAULT_NAME + id;

                        BufferedImage fragment = FileUtils.saveFragment(fDto.getMultipartFile(), tmpFileName);

                        Graphics2D g2d = (Graphics2D) currentImage.getGraphics();
                        g2d.drawImage(fragment, fDto.getStartX(), fDto.getStartY(), null);

                        FileUtils.saveImage(currentImage, fileName);
                    } else throw new BadRequestException("Некорректные данные: Фрагмент за пределами изображения");
                } else throw new BadRequestException("Некорректные данные: Отсутствует тело запроса");
            } else throw new BadRequestException("Некорректные данные: Размер изображения превышает ограничения");
        } else throw new NotFoundException("Image с таким ID не найден");
    }
}