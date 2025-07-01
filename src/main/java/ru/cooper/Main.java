package ru.cooper;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Точка входа в 2D-игру.
 * Отвечает за создание главного окна, добавление игровой панели и запуск игрового цикла.
 */
public class Main {

    /**
     * Главный метод запуска приложения.
     * Создаёт окно, инициализирует панель игры и запускает поток игры.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("2D Adventure");

            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);
            window.pack();

            window.setLocationRelativeTo(null); // Центрирование окна
            window.setVisible(true);

            gamePanel.startGameThread(); // Запуск игрового потока
        });
    }
}
