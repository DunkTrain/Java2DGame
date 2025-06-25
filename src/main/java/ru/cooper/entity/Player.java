package ru.cooper.entity;

import ru.cooper.GamePanel;
import ru.cooper.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Игрок — управляемый пользователем игровой объект.
 * Наследуется от {@link Entity}, использует обработчик клавиш для движения.
 */
public class Player extends Entity {

    /** Ссылка на панель игры для доступа к параметрам, например, размеру тайла */
    private final GamePanel gp;

    /** Обработчик ввода с клавиатуры */
    private final KeyHandler keyH;

    /**
     * Конструктор игрока.
     *
     * @param gp   панель игры
     * @param keyH обработчик клавиш
     */
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Устанавливает стартовое положение и скорость игрока.
     */
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            orc_down_left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_down_left.png")));
            orc_down_right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_down_right.png")));
            orc_down_stay = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_down_stay.png")));

            orc_left_left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_left_left.png")));
            orc_left_right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_left_right.png")));

            orc_right_left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_right_left.png")));
            orc_right_right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_right_right.png")));

            orc_up_left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_up_left.png")));
            orc_up_right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_up_right.png")));
            orc_up_stay = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/orc_up_stay.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Обновляет положение игрока на основе текущего ввода.
     */
    public void update() {
        boolean moving = false;

        if (keyH.upPressed) {
            y -= speed;
            direction = "up";
            moving = true;
        } else if (keyH.downPressed) {
            y += speed;
            direction = "down";
            moving = true;
        } else if (keyH.leftPressed) {
            x -= speed;
            direction = "left";
            moving = true;
        } else if (keyH.rightPressed) {
            x += speed;
            direction = "right";
            moving = true;
        }

        if (moving) {
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        } else {
            spriteNum = 0; // стоим
        }
    }


    /**
     * Отрисовывает игрока в виде белого квадрата.
     *
     * @param g2 графический контекст
     */
    public void draw(Graphics2D g2) {
        BufferedImage image = switch (direction) {
            case "up" -> (spriteNum == 1) ? orc_up_left :
                    (spriteNum == 2) ? orc_up_right : orc_up_stay;
            case "down" -> (spriteNum == 1) ? orc_down_left :
                    (spriteNum == 2) ? orc_down_right : orc_down_stay;
            case "left" -> (spriteNum == 1) ? orc_left_left : orc_left_right;
            case "right" -> (spriteNum == 1) ? orc_right_left : orc_right_right;
            default -> null;
        };

        int size = (int) (gp.tileSize * 2);
        g2.drawImage(image, x, y, size, size, null);
    }
}
