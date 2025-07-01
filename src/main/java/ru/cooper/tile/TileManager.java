package ru.cooper.tile;

import ru.cooper.GamePanel;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Менеджер Тайлов - управляет загрузкой, хранением и отрисовкой Тайлов карты.
 * Отвечает за загрузку текстур Тайлов и данных карты из файлов.
 */
public class TileManager {

    private static final Logger LOGGER = Logger.getLogger(TileManager.class.getName());

    /**
     * Общее количество доступных тайлов.
     */
    private static final int TILE_COUNT = 10;

    /**
     * Путь к изображению тайла: проходимое поле.
     */
    private static final String TILE_PATH_WALK = "/tiles/field_to_walk.png";

    /**
     * Путь к изображению тайла: граница
     */
    private static final String TILE_PATH_BORDER = "/tiles/border.png";

    /** Путь к изображению тайла: вода (граница) */
    private static final String TILE_PATH_WATER = "/tiles/water.png";

    /**
     * Ссылка на игровую панель
     */
    private final GamePanel gp;

    /**
     * Массив доступных Тайлов
     */
    private final Tile[] tile;

    /**
     * Двумерный массив с номерами Тайлов для карты
     */
    private final int[][] mapTileNum;

    /**
     * Конструктор менеджера Тайлов.
     *
     * @param gp ссылка на игровую панель
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[TILE_COUNT];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap("/maps/durotar.txt");
    }

    /**
     * Загружаем изображения Тайлов из ресурсов
     */
    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(TILE_PATH_WALK)));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(TILE_PATH_BORDER)));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(TILE_PATH_WATER)));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка загрузки текстур тайлов", e);
        }
    }

    /**
     * Загружает карту из текстового файла.
     *
     * @param filePath путь к файлу карты в ресурсах
     */
    public void loadMap(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            for (int row = 0; row < gp.maxScreenRow; row++) {
                String line = br.readLine();
                if (line == null) break;

                String[] numbers = line.split(" ");
                for (int col = 0; col < gp.maxScreenCol && col < numbers.length; col++) {
                    try {
                        mapTileNum[col][row] = Integer.parseInt(numbers[col].trim());
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Некорректный номер тайла в строке " + row + ", колонке " + col);
                        mapTileNum[col][row] = 0; // Значение по умолчанию
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка загрузки карты: " + filePath, e);
        }
    }

    /**
     * Отрисовывает все тайлы карты на экране.
     *
     * @param g2 графический контекст для отрисовки
     */
    public void draw(Graphics2D g2) {
        int x = 0;
        int y = 0;

        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                int tileNum = mapTileNum[col][row];

                if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
                    g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
                }

                x += gp.tileSize;
            }
            x = 0;
            y += gp.tileSize;
        }
    }
}
