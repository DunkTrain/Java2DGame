package ru.cooper.entity;

import java.awt.image.BufferedImage;

/**
 * Базовый абстрактный класс для игровых объектов.
 * Содержит координаты и скорость перемещения.
 * Используется как родительский класс, например, для {@link Player}.
 */
public class Entity {

    /** Положение по оси X (в пикселях) */
    public int x;

    /** Положение по оси Y (в пикселях) */
    public int y;

    /** Скорость перемещения (пикселей за кадр) */
    public int speed;

    /** Текущий кадр (для смены между левой/правой ногой) */
    public int spriteCounter = 0;

    /** Индекс текущего кадра (1 или 2) */
    public int spriteNum = 1;

    /** Текущая сторона взгляда персонажа */
    public String direction;

    // ↓↓↓ СПРАЙТЫ ↓↓↓
    public BufferedImage orc_down_left, orc_down_right, orc_down_stay;
    public BufferedImage orc_left_left, orc_left_right;
    public BufferedImage orc_right_left, orc_right_right;
    public BufferedImage orc_up_left, orc_up_right, orc_up_stay;
}
