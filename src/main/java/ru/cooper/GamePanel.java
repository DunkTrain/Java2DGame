package ru.cooper;

import ru.cooper.entity.Player;
import ru.cooper.tile.TileManager;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Основная панель игры, отвечающая за отрисовку и игровой цикл.
 * Реализует {@link Runnable} для выполнения цикла игры в отдельном потоке.
 */
public class GamePanel extends JPanel implements Runnable {

    // Настройки экрана
    /** Исходный размер тайла в пикселях (до масштабирования) */
    private final int originalTileSize = 16;

    /** Коэффициент масштабирования тайлов */
    private final int scale = 5;

    /** Размер тайла с учётом масштабирования */
    public final int tileSize = originalTileSize * scale;

    /** Количество колонок на экране */
    public final int maxScreenCol = 16;

    /** Количество строк на экране */
    public final int maxScreenRow = 12;

    /** Общая ширина экрана в пикселях */
    public final int screenWidth = tileSize * maxScreenCol;

    /** Общая высота экрана в пикселях */
    public final int screenHeight = tileSize * maxScreenRow;

    /** Целевая частота кадров */
    private static final int FPS = 60;

    private static final long NANOS_PER_SECOND = 1_000_000_000L;
    private static final double DRAW_INTERNAL = NANOS_PER_SECOND / (double) FPS;

    TileManager tileManager = new TileManager(this);
    private final KeyHandler keyH = new KeyHandler();
    private final Player player = new Player(this, keyH);
    private Thread gameThread;

    // Переменные цикла игры
    private double delta = 0;
    private long lastTime;
    private long currentTime;
    private long timer = 0;
    private int drawCount = 0;

    /**
     * Конструктор инициализирует параметры панели:
     * размер, фон, буферизацию и обработчик ввода.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    /**
     * Запускает игровой поток и инициализирует расчёт времени кадра.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        lastTime = System.nanoTime();
        gameThread.start();
    }

    /**
     * Игровой цикл: обновляет состояние и перерисовывает экран с заданной частотой кадров.
     * Использует дельта-время для обеспечения стабильной скорости игры.
     */
    @Override
    public void run() {
        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / DRAW_INTERNAL;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= NANOS_PER_SECOND) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Обновляет игровое состояние. Вызывается каждый кадр.
     */
    public void update() {
        player.update();
    }

    /**
     * Отрисовывает текущее состояние игры на экран.
     *
     * @param g графический контекст
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        tileManager.draw(g2);
        player.draw(g2);

        g2.dispose();
    }
}
