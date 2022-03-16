package com.example.charta.common;

/**
 * Эндпоинты приложения для взаимодействия
 *
 * @author Egor Mitrofanov
 */
public interface Endpoints {

    /**
     *  Эндпоинты взаимодействия с сервисом работы с изображениями
     */
    String CHARTA = "/chartas";
    String CHARTA_WITH_ID = "/chartas/{chartId}";
}
