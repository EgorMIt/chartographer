package com.example.charta;

import com.example.charta.errors.BadRequestException;
import com.example.charta.errors.NotFoundException;
import com.example.charta.model.FragmentDto;
import com.example.charta.model.ImageDto;
import com.example.charta.service.impl.ChartaServiceImpl;
import com.example.charta.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульное тестирование сервисной логики
 *
 * @author Egor Mitrofanov
 */
@SpringBootTest
class ChartaServiceImplTest {

    /**
     * {@link ChartaServiceImpl}.
     */
    @Autowired
    private ChartaServiceImpl chartaService;

    private final FileUtils fileUtils = new FileUtils();

    @Mock
    private ImageDto imageDto;

    /*
     * Проверка создания и удаления изображений
     */
    @Test
    void createAndDeleteNewImage() {
        imageDto = chartaService.createNewImage(100, 101);
        assertEquals(100, imageDto.getWidth());
        assertEquals(101, imageDto.getHeight());

        ImageDto imageDto2 = chartaService.createNewImage(101, 102);
        assertEquals(imageDto.getId() + 1, imageDto2.getId());

        chartaService.deleteImageByID(imageDto.getId());
        chartaService.deleteImageByID(imageDto2.getId());
    }

    /*
     * Проверка исключений при некорректных данных
     */
    @Test
    void createNewImageWithBadRequest() throws BadRequestException {
        try {
            imageDto = chartaService.createNewImage(20001, 500001);
        } catch (BadRequestException e) {
            assertEquals("Некорректные данные: Размер изображения превышает ограничения", e.getError());
        }
    }

    /*
     * Проверка исключений при отсутсвии ID
     */
    @Test
    void deleteImageWithBadRequest() throws NotFoundException {
        try {
            chartaService.deleteImageByID(1000);
        } catch (NotFoundException e) {
            assertEquals("Image с таким ID не найден", e.getError());
        }
    }

    /*
     * Проверка получения изображения
     */
    @Test
    void getImageTest() {
        imageDto = chartaService.createNewImage(100, 101);
        int id = imageDto.getId();
        assertNotNull(fileUtils.getImageByID(id));
        assertTrue(fileUtils.checkImageInStorage(id));
        chartaService.deleteImageByID(id);
    }

    /*
     * Проверка сервисов вставки и получения фрагментов
     */
    @Test
    void fragmentTest() throws IOException {
        File file1 = new File("src/test/java/com/example/charta/resources/images/testInsertFragment.bmp");
        File file2 = new File("src/test/java/com/example/charta/resources/images/testImage.bmp");
        File file3 = new File("src/test/java/com/example/charta/resources/images/testGetFragment.bmp");

        FileInputStream input = new FileInputStream(file1);
        MultipartFile multipartFile = new MockMultipartFile("testFragment",
                file1.getName(), "image/bmp", IOUtils.toByteArray(input));

        FragmentDto fragmentDto1 = new FragmentDto(multipartFile, 5, 5, 5, 5);
        imageDto = chartaService.createNewImage(15, 15);
        chartaService.insertFragment(imageDto.getId(), fragmentDto1);

        BufferedImage testImage = ImageIO.read(file2);
        BufferedImage realImage = fileUtils.getBufferedImage(imageDto.getId());

        int imageColumns = testImage.getWidth();
        int imageRows = testImage.getHeight();
        boolean areSameImages = true;

        if (testImage.getWidth() != realImage.getWidth() || testImage.getHeight() != realImage.getHeight()) {
            areSameImages = false;
        } else {
            for (int row = 0; row < imageRows; row++) {
                for (int col = 0; col < imageColumns; col++) {
                    int rgb = testImage.getRGB(col, row);
                    int rgb2 = testImage.getRGB(col, row);

                    if (rgb != rgb2) {
                        areSameImages = false;
                        break;
                    }
                }
            }
        }

        assertTrue(areSameImages);

        BufferedImage realGetFragment = chartaService.getFragment(imageDto.getId(), new FragmentDto(7, 7, 0, 0));
        BufferedImage testGetFragment = ImageIO.read(file3);

        int fragmentColumns = testGetFragment.getWidth();
        int fragmentRows = testGetFragment.getHeight();
        boolean areSameFragments = true;

        if (testGetFragment.getWidth() != realGetFragment.getWidth() || testGetFragment.getHeight() != realGetFragment.getHeight()) {
            areSameFragments = false;
        } else {
            for (int row = 0; row < fragmentRows; row++) {
                for (int col = 0; col < fragmentColumns; col++) {
                    int rgb = testGetFragment.getRGB(col, row);
                    int rgb2 = realGetFragment.getRGB(col, row);

                    if (rgb != rgb2) {
                        areSameFragments = false;
                        break;
                    }
                }
            }
        }
        assertTrue(areSameFragments);
        chartaService.deleteImageByID(imageDto.getId());
    }
}