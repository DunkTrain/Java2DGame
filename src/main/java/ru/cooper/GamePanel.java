package ru.cooper;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * The main game panel that handles rendering and game logic.
 * Implements Runnable to manage the game loop in a separate thread.
 */
public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTING
    /** Original size of a tile in pixels */
    final int originalTileSize = 16; // 16x16 title

    /** Scale factor for rendering on higher resolution displays */
    final int scale = 3;

    /** Actual tile size used for rendering (originalTileSize * scale) */
    final int tileSize = originalTileSize * scale; // 48x48 tile

    /** Number of columns in the game screen */
    final int maxScreenCol = 16;

    /** Number of rows in the game screen */
    final int maxScreenRow = 12;

    /** Total width of the game screen in pixels */
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels

    /** Total height of the game screen in pixels */
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // Target FPS
    int fps = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    // Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    // Game Loop variables
    double delta = 0;
    long lastTime;
    long currentTime;
    long timer = 0;
    int drawCount = 0;
    double drawInterval;

    /**
     * Initializes the game panel with proper settings.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    /**
     * Starts the game thread and initializes timing variables.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        lastTime = System.nanoTime();
        drawInterval = (double) 1_000_000_000 / fps;
        gameThread.start();
    }

    /**
     * The main game loop. Updates game state and renders at the specified FPS.
     * Uses delta timing to ensure consistent game speed regardless of frame rate.
     */
    @Override
    public void run() {
        while (gameThread != null) {
            currentTime = System.nanoTime();

            // Calculate time passed and accumulate delta
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            // Update and render when a full frame interval has passed
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            // Display FPS once per second
            if (timer >= 1_000_000_000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Updates the game state based on user input.
     * Called every frame when delta time accumulates to 1.
     */
    public void update() {
        if (keyH.upPressed) {
            playerY -= playerSpeed;
        } else if (keyH.downPressed) {
            playerY += playerSpeed;
        } else if (keyH.leftPressed) {
            playerX -= playerSpeed;
        } else if (keyH.rightPressed) {
            playerX += playerSpeed;
        }
    }

    /**
     * Renders the game state to the screen.
     * Called every frame when delta time accumulates to 1.
     *
     * @param g The Graphics context to render to
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }
}
