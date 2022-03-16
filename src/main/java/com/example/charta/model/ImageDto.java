package com.example.charta.model;

import lombok.*;

/**
 * Модель для работы с изображениями
 *
 * @author Egor Mitrofanov
 */
@Getter
@Setter
public class ImageDto extends Params {

    private int id;
    private String fileName;

    public ImageDto(int id, String fileName, int width, int height) {
        super(width, height);
        this.id = id;
        this.fileName = fileName;
    }

}
