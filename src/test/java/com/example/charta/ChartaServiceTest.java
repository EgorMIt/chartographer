package com.example.charta;

import com.example.charta.errors.BadRequestException;
import com.example.charta.errors.NotFoundException;
import com.example.charta.model.FragmentDto;
import com.example.charta.service.impl.ChartaServiceImpl;
import com.example.charta.utils.FileUtils;
import com.example.charta.utils.GlobalVariable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Модульное тестирование сервисной логики
 *
 * @author Egor Mitrofanov
 */
@SpringBootTest(args = "images/")
class ChartaServiceTest {

    @InjectMocks
    private ChartaServiceImpl chartaService;

    @Mock
    private GlobalVariable globalVariable = Mockito.mock(GlobalVariable.class);

    private MockedStatic<FileUtils> fileUtils;

    @BeforeEach
    void init() {
        fileUtils = mockStatic(FileUtils.class);
        when(globalVariable.getImageWidthMax()).thenReturn("20000");
        when(globalVariable.getImageHeightMax()).thenReturn("50000");
        when(globalVariable.getFragmentWidthMax()).thenReturn("2000");
        when(globalVariable.getFragmentHeightMax()).thenReturn("5000");
    }

    @AfterEach
    void tearDown() {
        fileUtils.close();
    }

    @Test
    @DisplayName("Создание изображения")
    void createImageTest() {
        chartaService.createNewImage(100, 101);
        fileUtils.verify(() -> FileUtils.saveImage(any(), any()));
    }

    @Test
    @DisplayName("Исключение BadRequest при создании изображения")
    void createNewImageWithBadRequest() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> chartaService.createNewImage(20001, 500001));
        assertEquals("Некорректные данные: Размер изображения превышает ограничения", exception.getError());
    }

    @Test
    @DisplayName("Удаление изображения")
    void deleteImageTest() {
        fileUtils.when(() -> FileUtils.checkImageInStorage(anyInt())).thenReturn(true);
        fileUtils.when(() -> FileUtils.getImageByID(anyInt())).thenReturn(new File(""));
        chartaService.deleteImageByID(anyInt());
        fileUtils.verify(() -> FileUtils.getImageByID(anyInt()));
    }

    @Test
    @DisplayName("Исключение BadRequest при удалении изображения")
    void deleteImageWithBadRequest() {
        fileUtils.when(() -> FileUtils.checkImageInStorage(anyInt())).thenReturn(false);
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> chartaService.deleteImageByID(1000));
        assertEquals("Image с таким ID не найден", exception.getError());
    }

    @Test
    @DisplayName("Вставка фрагмента")
    void insertFragmentTest() {
        FragmentDto fragmentDto = new FragmentDto(new MockMultipartFile("testFragment",
                "testFragment", "image/bmp", new byte[]{0}), 1, 1, 1, 1);
        fileUtils.when(() -> FileUtils.getBufferedImage(anyInt()))
                .thenReturn(new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR));
        fileUtils.when(() -> FileUtils.checkImageInStorage(anyInt())).thenReturn(true);

        chartaService.insertFragment(1, fragmentDto);
        fileUtils.verify(() -> FileUtils.saveImage(any(), any()));
    }

    @Test
    @DisplayName("Исключение NotFound при вставке фрагмента")
    void insertFragmentTestNotFound() {
        FragmentDto fragmentDto = new FragmentDto(new MockMultipartFile("testFragment",
                "testFragment", "image/bmp", new byte[]{0}), 1, 1, 1, 1);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> chartaService.insertFragment(1, fragmentDto));
        assertEquals("Image с таким ID не найден", exception.getError());
    }

    @Test
    @DisplayName("Исключение BadRequest при вставке фрагмента")
    void insertFragmentTestBadRequest() {
        FragmentDto fragmentDto = new FragmentDto(new MockMultipartFile("testFragment",
                "testFragment", "image/bmp", new byte[]{0}), 2001, 5001, 1, 1);
        fileUtils.when(() -> FileUtils.checkImageInStorage(anyInt())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> chartaService.insertFragment(1, fragmentDto));
        assertEquals("Некорректные данные: Размер изображения превышает ограничения", exception.getError());
    }

    @Test
    @DisplayName("Получение фрагмента")
    void getFragmentTest() {
        FragmentDto fragmentDto = new FragmentDto(1, 1, 1, 1);
        fileUtils.when(() -> FileUtils.getBufferedImage(anyInt()))
                .thenReturn(new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR));
        fileUtils.when(() -> FileUtils.checkImageInStorage(anyInt())).thenReturn(true);

        chartaService.getFragment(1, fragmentDto);
        fileUtils.verify(() -> FileUtils.saveImage(any(), any()));
    }

    @Test
    @DisplayName("Исключение NotFound при получении фрагмента")
    void getFragmentTestNotFound() {
        FragmentDto fragmentDto = new FragmentDto(1, 1, 1, 1);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> chartaService.getFragment(1, fragmentDto));
        assertEquals("Image с таким ID не найден", exception.getError());
    }

    @Test
    @DisplayName("Исключение BadRequest при получении фрагмента")
    void getFragmentTestBadRequest() {
        FragmentDto fragmentDto = new FragmentDto(new MockMultipartFile("testFragment",
                "testFragment", "image/bmp", new byte[]{0}), 2001, 5001, 1, 1);
        fileUtils.when(() -> FileUtils.checkImageInStorage(anyInt())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> chartaService.getFragment(1, fragmentDto));
        assertEquals("Некорректные данные: Размер изображения превышает ограничения", exception.getError());
    }
}