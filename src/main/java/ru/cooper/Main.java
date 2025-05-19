package ru.cooper;

import javax.swing.JFrame;

/**
 * Main entry point for the 2D adventure game.
 * Sets up the game window and starts the game thread.
 */
public class Main {

    /**
     * Main method to start the application.
     * Creates the game window, adds the game panel, and starts the game thread.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {

        // Create main window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        // Create and add game panel
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        // Center window on screen and make visible
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Start the game loop
        gamePanel.startGameThread();
    }
}