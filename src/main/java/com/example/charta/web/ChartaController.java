package com.example.charta.web;

import com.example.charta.common.Endpoints;
import com.example.charta.model.FragmentDto;
import com.example.charta.service.impl.ChartaServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "Создание нового черного изображения", notes = "Создать новое черное изображение с заданной шириной и высотой")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "width", value = "Ширина изображения", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "height", value = "Высота изображения", required = true, dataType = "Integer")
    })
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
    @ApiOperation(value = "Вставка фрагмента в изображение", notes = "Вставить полученный фрагмент в изображение по ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "chartId", value = "ID изображения", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "x", value = "Начальная координата X фрагмента", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "y", value = "Начальная координата Y фрагмента", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "width", value = "Ширина фрагмента", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "height", value = "Высота фрагмента", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "file", value = "BMP изображение в теле запроса", required = true, dataType = "MultipartFile")
    })
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
    @ApiOperation(value = "Получение фрагмента изображения", notes = "Получить заданный фрагмент изображения по ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "chartId", value = "ID изображения", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "x", value = "Начальная координата X фрагмента", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "y", value = "Начальная координата Y фрагмента", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "width", value = "Ширина фрагмента", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "height", value = "Высота фрагмента", required = true, dataType = "Integer")
    })
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
    @ApiOperation(value = "Удаление изображение", notes = "Удалить выбранное изображение по ID")
    @ApiImplicitParam(name = "chartId", value = "ID изображения", required = true, dataType = "Integer")
    @DeleteMapping(value = Endpoints.CHARTA_WITH_ID)
    public ResponseEntity<Object> deleteImageByID(@PathVariable int chartId) {

        chartaServiceImpl.deleteImageByID(chartId);
        return ResponseEntity.ok().build();
    }
}
