package com.example.charta.service.impl;

import com.example.charta.errors.BadRequestException;
import com.example.charta.errors.NotFoundException;
import com.example.charta.model.FragmentDto;
import com.example.charta.model.ImageDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ChartaServiceImplTest {

    @Autowired
    private ChartaServiceImpl chartaService;

    @Mock
    private FragmentDto fragmentDto;

    @Mock
    private ImageDto imageDto;

    @Test
    void createAndDeleteNewImage() {
        ImageDto imageDto = chartaService.createNewImage(100, 101);
        assertEquals(100, imageDto.getWidth());
        assertEquals(101, imageDto.getHeight());

        ImageDto imageDto2 = chartaService.createNewImage(101, 102);
        assertEquals(imageDto.getId() + 1, imageDto2.getId());

        chartaService.deleteImageByID(imageDto.getId());
        chartaService.deleteImageByID(imageDto2.getId());
    }

    @Test
    void createNewImageWithBadRequest() throws BadRequestException {
        try {
            ImageDto imageDto = chartaService.createNewImage(20001, 500001);
        } catch (BadRequestException e) {
            assertEquals("Некорректные данные: Размер изображения превышает ограничения", e.getError());
        }
    }

    @Test
    void deleteImageWithBadRequest() throws NotFoundException {
        try {
            chartaService.deleteImageByID(1000);
        } catch (NotFoundException e) {
            assertEquals("Image с таким ID не найден", e.getError());
        }
    }

    @Test
    void insertFragment() {

    }
}