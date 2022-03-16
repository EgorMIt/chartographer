package com.example.charta.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "restrictions")
public class Restrictions {
    // @Value("imageWidthMax")
    private int imageWidthMax = 20000;

    //@Value("imageHeightMax")
    private int imageHeightMax = 50000;

    //@Value("fragmentWidthMax")
    private int fragmentWidthMax = 2000;

    // @Value("fragmentHeightMax")
    private int fragmentHeightMax = 5000;

    // @Value("storagePath")
    private String storagePath;

}
