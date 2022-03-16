package com.example.charta.web;

import com.example.charta.common.Endpoints;
import com.example.charta.model.FragmentDto;
import com.example.charta.service.impl.ChartaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

/**
 * Контроллер запросов приложения
 *
 * @author Egor Mitrofanov
 */
@RestController
@RequiredArgsConstructor
public class ChartaController {

    /**
     * {@link ChartaServiceImpl}.
     */
    private final ChartaServiceImpl chartaServiceImpl;

    /**
     * Создание нового пустого изображения
     *
     * @param width  ширина изображения
     * @param height высота изображения
     * @return id нового изображения
     */
    @PostMapping(value = Endpoints.CHARTA)
    public ResponseEntity<Integer> createNewImage(@RequestParam("width") int width, @RequestParam("height") int height) {
        return new ResponseEntity<>(chartaServiceImpl.createNewImage(width, height).getId(), HttpStatus.CREATED);
    }

    /**
     * Вставка фрагмента в изображения
     *
     * @param chartId       ID изображения
     * @param x             координата X фрагмента
     * @param y             координата Y фрагмента
     * @param width         ширина фрагмента
     * @param height        высота фрагмента
     * @param multipartFile файл с изображением фрагмента
     * @return код ответа
     */
    @PostMapping(value = Endpoints.CHARTA_WITH_ID)
    public ResponseEntity<Object> insertFragment(@PathVariable int chartId, @RequestParam("x") int x,
                                                 @RequestParam("y") int y, @RequestParam("width") int width,
                                                 @RequestParam("height") int height,
                                                 @RequestParam("file") MultipartFile multipartFile) {
        chartaServiceImpl.insertFragment(chartId, new FragmentDto(multipartFile, width, height, x, y));
        return ResponseEntity.ok().build();
    }

    /**
     * Получение фрагмента изображения
     *
     * @param chartId ID изображения
     * @param x       координата X фрагмента
     * @param y       координата Y фрагмента
     * @param width   ширина фрагмента
     * @param height  высота фрагмента
     * @return BufferedImage полученного фрагмента
     */
    @GetMapping(value = Endpoints.CHARTA_WITH_ID)
    public ResponseEntity<BufferedImage> getFragment(@PathVariable int chartId, @RequestParam("x") int x,
                                                     @RequestParam("y") int y, @RequestParam("width") int width,
                                                     @RequestParam("height") int height) {

        return new ResponseEntity<>(chartaServiceImpl.getFragment(chartId,
                new FragmentDto(width, height, x, y)), HttpStatus.OK);
    }

    /**
     * Удаление изображения
     *
     * @param chartId ID изображения
     * @return код ответа
     */
    @DeleteMapping(value = Endpoints.CHARTA_WITH_ID)
    public ResponseEntity<Object> deleteImageByID(@PathVariable int chartId) {

        chartaServiceImpl.deleteImageByID(chartId);
        return ResponseEntity.ok().build();
    }
}
