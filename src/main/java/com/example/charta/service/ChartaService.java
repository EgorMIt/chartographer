package com.example.charta.service;

import com.example.charta.model.FragmentDto;
import com.example.charta.model.ImageDto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Сервис работы с изображениями
 *
 * @author Egor Mitrofanov
 */
public interface ChartaService {

    /**
     * Создание нового пустого изображения
     *
     * @param width  ширина изображения
     * @param height высота изображения
     * @return ImageDto нового изображения
     */
    ImageDto createNewImage(int width, int height) throws IOException;

    /**
     * Удаление изображения
     *
     * @param id ID изображения
     */
    void deleteImageByID(int id);

    /**
     * Вставка фрагмента в изображения
     *
     * @param id          ID изображения
     * @param fragmentDto FragmentDto фрагмента
     */
    void insertFragment(int id, FragmentDto fragmentDto) throws IOException;

    /**
     * Получение фрагмента изображения
     *
     * @param id ID изображения
     * @param fragmentDto FragmentDto фрагмента
     * @return BufferedImage полученного фрагмента
     */
    BufferedImage getFragment(int id, FragmentDto fragmentDto);
}
