package ru.cooper.tile;

import java.awt.image.BufferedImage;

/**
 * Класс тайла - представляет один элемент игрового поля.
 * Содержит изображение и информацию о коллизии.
 */
public class Tile {

    /** Изображение тайла */
    public BufferedImage image;

    /** Флаг коллизии - определяет, можно ли пройти через тайл */
    public boolean collision = false;
}
