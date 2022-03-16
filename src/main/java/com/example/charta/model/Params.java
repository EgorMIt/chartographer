package com.example.charta.model;

import lombok.Data;

@Data
public class Params {
    protected int width;
    protected int height;

    public Params(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
