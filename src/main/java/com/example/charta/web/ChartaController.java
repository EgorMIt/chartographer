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

@RestController
@RequiredArgsConstructor
public class ChartaController {

    private final ChartaServiceImpl chartaServiceImpl;

    @PostMapping(value = Endpoints.CHARTA)
    public ResponseEntity<Integer> createNewImage(@RequestParam("width") int width, @RequestParam("height") int height) {
        return new ResponseEntity<>(chartaServiceImpl.createNewImage(width, height).getId(), HttpStatus.CREATED);
    }

    @PostMapping(value = Endpoints.CHARTA_WITH_ID)
    public ResponseEntity<Object> insertFragment(@PathVariable int chartId, @RequestParam("x") int x,
                                                 @RequestParam("y") int y, @RequestParam("width") int width,
                                                 @RequestParam("height") int height,
                                                 @RequestParam("file") MultipartFile multipartFile) {
        System.out.println("Я ТУТУТУТ");
        chartaServiceImpl.insertFragment(chartId, new FragmentDto(multipartFile, width, height, x, y));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = Endpoints.CHARTA_WITH_ID)
    public ResponseEntity<BufferedImage> getFragment(@PathVariable int chartId, @RequestParam("x") int x,
                                                     @RequestParam("y") int y, @RequestParam("width") int width,
                                                     @RequestParam("height") int height) {

        return new ResponseEntity<>(chartaServiceImpl.getFragment(chartId,
                new FragmentDto(width, height, x, y)), HttpStatus.OK);
    }

    @DeleteMapping(value = Endpoints.CHARTA_WITH_ID)
    public ResponseEntity<Object> deleteImageByID(@PathVariable int chartId) {

        chartaServiceImpl.deleteImageByID(chartId);
        return ResponseEntity.ok().build();
    }
}
