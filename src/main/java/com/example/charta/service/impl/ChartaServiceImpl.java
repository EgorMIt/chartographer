package com.example.charta.service.impl;

import com.example.charta.errors.BadRequestException;
import com.example.charta.errors.NotFoundException;
import com.example.charta.model.FragmentDto;
import com.example.charta.model.ImageDto;
import com.example.charta.service.ChartaService;
import com.example.charta.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;


@Service
@Slf4j
@ConfigurationPropertiesScan("com.example.charta.utils")
public class ChartaServiceImpl implements ChartaService {

    private static int idCount = 1;
    private static final String DEFAULT_NAME = "Image";
    private static final FileUtils fu = new FileUtils();

    @Override
    public ImageDto createNewImage(int width, int height)  {
        if (width <= 20000 && height <= 50000) {
            BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

            Graphics2D g2d = (Graphics2D) newBufferedImage.getGraphics();
            g2d.setBackground(Color.BLACK);
            g2d.dispose();

            while (fu.checkImageInStorage(idCount)) {
                idCount++;
            }

            String fileName = DEFAULT_NAME + idCount;
            ImageDto newImage = new ImageDto(idCount, fileName, width, height);
            idCount++;

            fu.saveImage(newBufferedImage, fileName);

            return newImage;
        } else {
            throw new BadRequestException("Условие не выполнено");
        }
    }


    @Override
    public BufferedImage getFragment(int id, FragmentDto fragmentDto) {
        if (fu.checkImageInStorage(id)) {
            if (fragmentDto.getWidth() <= 5000 && fragmentDto.getHeight() <= 5000) {
                BufferedImage currentImage = fu.getBufferedImage(id);

                BufferedImage fragment = currentImage.getSubimage(fragmentDto.getStartX(),
                        fragmentDto.getStartY(), fragmentDto.getWidth(), fragmentDto.getHeight());

                String fileName = "tmpGetFragment";
                fu.saveImage(fragment, fileName);
                return fragment;
            } else {
                throw new BadRequestException("Условие не выполнено иди пустое тело запроса");
            }
        } else throw new NotFoundException("Image с таким ID не найден");
    }

    @Override
    public ImageDto getImageDtoByID(int id) {
        return null;
    }

    @Override
    public void deleteImageByID(int id) {
        if (fu.checkImageInStorage(id)) {
            if (fu.getImageByID(id).delete()) idCount = id;
        } else throw new NotFoundException("Image с таким ID не найден");
    }

    @Override
    public void insertFragment(int id, FragmentDto fragmentDto) {
        System.out.println("!!!!!");
        System.out.println(fu.checkImageInStorage(id));
        if (fu.checkImageInStorage(id)) {
            if (fragmentDto.getWidth() <= 2000 && fragmentDto.getHeight() <= 5000 && !fragmentDto.getMultipartFile().isEmpty()) {
                BufferedImage currentImage = fu.getBufferedImage(id);

                String tmpFileName = "tmpFragment";
                String fileName = DEFAULT_NAME + id;

                BufferedImage fragment = fu.saveFragment(fragmentDto.getMultipartFile(), tmpFileName);

                Graphics2D g2d = (Graphics2D) currentImage.getGraphics();
                g2d.drawImage(fragment, fragmentDto.getStartX(), fragmentDto.getStartY(), null);

                fu.saveImage(currentImage, fileName);
            } else {
                throw new BadRequestException("Условие не выполнено иди пустое тело запроса");
            }
        } else throw new NotFoundException("Image с таким ID не найден");
    }
}
