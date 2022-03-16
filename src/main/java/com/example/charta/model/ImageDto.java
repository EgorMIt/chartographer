package com.example.charta.model;

import lombok.*;

import java.net.URI;

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
