package com.example.charta.model;

import lombok.Data;

/**
 * Родительская модель для работы с высотой и шириной объектов
 *
 * @author Egor Mitrofanov
 */
@Data
public class Params {
    protected int width;
    protected int height;

    public Params(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
