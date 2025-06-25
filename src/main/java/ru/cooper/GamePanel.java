package ru.cooper;

import ru.cooper.entity.Player;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Основная панель игры, отвечающая за отрисовку и игровой цикл.
 * Реализует {@link Runnable} для выполнения цикла игры в отдельном потоке.
 */
public class GamePanel extends JPanel implements Runnable {

    // Настройки экрана
    /** Исходный размер тайла в пикселях (до масштабирования) */
    private final int originalTileSize = 16;

    /** Коэффициент масштабирования тайлов */
    private final int scale = 3;

    /** Размер тайла с учётом масштабирования */
    public final int tileSize = originalTileSize * scale;

    /** Количество колонок на экране */
    private final int maxScreenCol = 16;

    /** Количество строк на экране */
    private final int maxScreenRow = 12;

    /** Общая ширина экрана в пикселях */
    private final int screenWidth = tileSize * maxScreenCol;

    /** Общая высота экрана в пикселях */
    private final int screenHeight = tileSize * maxScreenRow;

    /** Целевая частота кадров */
    private final int fps = 60;

    private final KeyHandler keyH = new KeyHandler();
    private final Player player = new Player(this, keyH);
    private Thread gameThread;

    // Переменные цикла игры
    private double delta = 0;
    private long lastTime;
    private long currentTime;
    private long timer = 0;
    private int drawCount = 0;
    private double drawInterval;

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
        drawInterval = 1_000_000_000.0 / fps;
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

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1_000_000_000) {
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
        player.draw(g2);
        g2.dispose();
    }
}
