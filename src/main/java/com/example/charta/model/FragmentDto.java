package com.example.charta.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class FragmentDto extends Params {

    MultipartFile multipartFile;
    private int startX;
    private int startY;

    public FragmentDto(MultipartFile multipartFile, int width, int height, int startX, int startY) {
        super(width, height);
        this.multipartFile = multipartFile;
        this.startX = startX;
        this.startY = startY;
    }

    public FragmentDto(int width, int height, int startX, int startY) {
        super(width, height);
        this.startX = startX;
        this.startY = startY;
    }
}
