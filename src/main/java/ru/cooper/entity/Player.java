package ru.cooper.entity;

import ru.cooper.GamePanel;
import ru.cooper.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Игрок — управляемый пользователем игровой объект.
 * Наследуется от {@link Entity}, использует обработчик клавиш для движения.
 */
public class Player extends Entity {

    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());

    /** Ссылка на панель игры для доступа к параметрам, например, размеру тайла */
    private final GamePanel gp;

    /** Обработчик ввода с клавиатуры */
    private final KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    /** Константа для скорости анимации спрайтов */
    private static final int SPRITE_ANIMATION_SPEED = 12;

    /**
     * Конструктор игрока.
     *
     * @param gp   панель игры
     * @param keyH обработчик клавиш
     */
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Устанавливает стартовое положение и скорость игрока.
     */
    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    /**
     * Загружает все спрайты игрока из ресурсов.
     */
    public void getPlayerImage() {
        try {
            // Спрайты движения вниз
            orc_down_left = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_down_left.png")));
            orc_down_right = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_down_right.png")));
            orc_down_stay = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_down_stay.png")));

            // Спрайты движения влево
            orc_left_left = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_left_left.png")));
            orc_left_right = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_left_right.png")));

            // Спрайты движения вправо
            orc_right_left = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_right_left.png")));
            orc_right_right = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_right_right.png")));

            // Спрайты движения вверх
            orc_up_left = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_up_left.png")));
            orc_up_right = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_up_right.png")));
            orc_up_stay = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/player/orc_up_stay.png")));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка загрузки спрайтов игрока", e);
        }
    }

    /**
     * Обновляет положение игрока на основе текущего ввода.
     */
    public void update() {
        boolean moving = false;

        if (keyH.upPressed) {
            worldY -= speed;
            direction = "up";
            moving = true;
        } else if (keyH.downPressed) {
            worldY += speed;
            direction = "down";
            moving = true;
        } else if (keyH.leftPressed) {
            worldX -= speed;
            direction = "left";
            moving = true;
        } else if (keyH.rightPressed) {
            worldX += speed;
            direction = "right";
            moving = true;
        }

        if (moving) {
            spriteCounter++;
            if (spriteCounter > SPRITE_ANIMATION_SPEED) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        } else {
            spriteNum = 0; // стоим
        }
    }

    /**
     * Отрисовывает игрока с правильным спрайтом в зависимости от направления и анимации.
     *
     * @param g2 графический контекст для отрисовки
     */
    public void draw(Graphics2D g2) {
        // УЛУЧШЕНИЕ: Более читаемый выбор спрайта с использованием switch expression
        BufferedImage image = switch (direction) {
            case "up" -> switch (spriteNum) {
                case 1 -> orc_up_left;
                case 2 -> orc_up_right;
                default -> orc_up_stay;
            };
            case "down" -> switch (spriteNum) {
                case 1 -> orc_down_left;
                case 2 -> orc_down_right;
                default -> orc_down_stay;
            };
            case "left" -> (spriteNum == 1) ? orc_left_left : orc_left_right;
            case "right" -> (spriteNum == 1) ? orc_right_left : orc_right_right;
            default -> orc_down_stay; // Спрайт по умолчанию
        };

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
