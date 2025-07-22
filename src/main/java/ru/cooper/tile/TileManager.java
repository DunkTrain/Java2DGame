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

    /** Путь к изображению тайла: земля */
    private static final String TILE_PATH_EARTH = "/tiles/earth.png";

    /** Путь к изображению тайла: песок */
    private static final String TILE_PATH_SAND = "/tiles/sand.png";

    /** Путь к изображению тайла: дерево */
    private static final String TILE_PATH_TREE = "/tiles/tree.png";

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
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
//        loadMap("/maps/durotar.txt");
        loadMap("/maps/world_01.txt");
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

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(TILE_PATH_EARTH)));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(TILE_PATH_TREE)));

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(TILE_PATH_SAND)));
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

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                if (line == null) break;

                String[] numbers = line.split(" ");
                for (int col = 0; col < gp.maxWorldCol && col < numbers.length; col++) {
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
     * Отрисовывает только видимые тайлы карты на экране для оптимизации производительности.
     *
     * @param g2 графический контекст для отрисовки
     */
    public void draw(Graphics2D g2) {
        // Определяем видимую область в мировых координатах
        int cameraLeftX = gp.player.worldX - gp.player.screenX;
        int cameraRightX = gp.player.worldX + (gp.screenWidth - gp.player.screenX);
        int cameraTopY = gp.player.worldY - gp.player.screenY;
        int cameraBottomY = gp.player.worldY + (gp.screenHeight - gp.player.screenY);

        // Преобразуем в индексы тайлов с небольшим запасом для плавности
        int startCol = Math.max(0, cameraLeftX / gp.tileSize);
        int endCol = Math.min(gp.maxWorldCol - 1, cameraRightX / gp.tileSize + 1);
        int startRow = Math.max(0, cameraTopY / gp.tileSize);
        int endRow = Math.min(gp.maxWorldRow - 1, cameraBottomY / gp.tileSize + 1);

        // Отрисовываем только видимые тайлы
        for (int worldRow = startRow; worldRow <= endRow; worldRow++) {
            for (int worldCol = startCol; worldCol <= endCol; worldCol++) {
                int tileNum = mapTileNum[worldCol][worldRow];

                // Пропускаем, если тайл не существует
                if (tileNum < 0 || tileNum >= tile.length || tile[tileNum] == null) {
                    continue;
                }

                // Мировые координаты тайла
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;

                // Преобразуем в экранные координаты
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                // Дополнительная проверка видимости (на случай неточностей в расчетах)
                if (screenX + gp.tileSize >= 0 && screenX <= gp.screenWidth &&
                        screenY + gp.tileSize >= 0 && screenY <= gp.screenHeight) {

                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }
}