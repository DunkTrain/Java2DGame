package ru.cooper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input for the game.
 * Implements KeyListener to capture key events.
 */
public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    /**
     * Invoked when a key has been typed.
     * Not used in this implementation.
     *
     * @param e The key event
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    /**
     * Invoked when a key has been pressed.
     * Sets the corresponding direction flag to true.
     *
     * @param e The key event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
    }

    /**
     * Invoked when a key has been released.
     * Sets the corresponding direction flag to false.
     *
     * @param e The key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}
