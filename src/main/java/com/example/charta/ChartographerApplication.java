package com.example.charta;

import org.apache.commons.io.FileUtils;
import com.example.charta.utils.GlobalVariable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Запуск Spring Boot
 *
 * @author Egor Mitrofanov
 */
@SpringBootApplication
public class ChartographerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ChartographerApplication.class, args);

        /*
         * Парсинг path до репозитория с изображения
         */
        if (!CollectionUtils.isEmpty(Arrays.asList(args))) {
            String string = args[0];
            String[] directories = string.split("/");
            StringBuilder outputString = new StringBuilder();

            for (int i = Objects.equals(directories[0], "") ? 1 : 0; i < directories.length; i++) {
                outputString.append(directories[i]);
                outputString.append("/");
            }

            GlobalVariable.path = String.valueOf(outputString);
            FileUtils.forceMkdir(new File(GlobalVariable.path));
        }
    }
}
