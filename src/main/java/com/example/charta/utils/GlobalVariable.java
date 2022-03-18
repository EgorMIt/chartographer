package com.example.charta.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * POJO класс для глобальных переменных
 *
 * @author Egor Mitrofanov
 */
@Data
@ConfigurationProperties(prefix = "global")
public class GlobalVariable {

    @Value("@{imageWidthMax}")
    private String imageWidthMax;

    @Value("@{imageHeightMax}")
    private String imageHeightMax;

    @Value("@{fragmentWidthMax}")
    private String fragmentWidthMax;

    @Value("@{fragmentHeightMax}")
    private String fragmentHeightMax;

    @Value("@{storagePath}")
    private String storagePath;

    @Value("@{defaultName}")
    private String defaultName;

    @Value("@{extension}")
    private String extension;

    public static String path = "images/";
}
