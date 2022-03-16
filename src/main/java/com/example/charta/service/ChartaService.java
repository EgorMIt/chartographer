package com.example.charta.service;

import com.example.charta.model.FragmentDto;
import com.example.charta.model.ImageDto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface ChartaService {
    ImageDto createNewImage(int width, int height) throws IOException;

    void deleteImageByID(int id);

    void insertFragment(int id, FragmentDto fragmentDto) throws IOException;

    BufferedImage getFragment(int id, FragmentDto fragmentDto);

    ImageDto getImageDtoByID(int id);
}
