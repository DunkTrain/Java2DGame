package ru.cooper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Обработчик нажатий клавиш.
 * Реализует интерфейс {@link KeyListener} для отслеживания ввода с клавиатуры.
 * Управляет флагами движения персонажа по направлениям: вверх, вниз, влево, вправо.
 */
public class KeyHandler implements KeyListener {

    /**
     * Флаг движения вверх (W)
     */
    public boolean upPressed;

    /**
     * Флаг движения вниз (S)
     */
    public boolean downPressed;

    /**
     * Флаг движения влево (A)
     */
    public boolean leftPressed;

    /**
     * Флаг движения вправо (D)
     */
    public boolean rightPressed;

    /**
     * Вызывается при вводе символа с клавиатуры.
     * В данной реализации не используется.
     *
     * @param e событие нажатия клавиши
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Не используется
    }

    /**
     * Вызывается при нажатии клавиши.
     * Устанавливает флаги движения в соответствующее направление.
     *
     * @param e событие нажатия клавиши
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
        }
    }

    /**
     * Вызывается при отпускании клавиши.
     * Сбрасывает флаги движения.
     *
     * @param e событие отпускания клавиши
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
        }
    }
}
