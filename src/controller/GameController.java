package controller;

import model.SnakeGameModel;
import view.GamePanel;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController implements ActionListener, KeyListener {

    private final SnakeGameModel model;
    private final GamePanel gamePanel;
    private final Timer gameLoop;

    public GameController(SnakeGameModel model, GamePanel gamePanel) {
        this.model = model;
        this.gamePanel = gamePanel;
        this.gameLoop = new Timer(100, this);
        this.gamePanel.setController(this);
        this.gamePanel.addKeyListener(this);
    }

    public void startGame() {
        if (model.isGameOver()) {
            model.resetGame();
        }
        gamePanel.showRestartButton(false);
        gamePanel.repaint();
        gameLoop.start();
    }

    public void resetGame() {
        model.resetGame();
        gamePanel.showRestartButton(false);
        gameLoop.setDelay(100);
        gameLoop.start();
        gamePanel.repaint();
        gamePanel.requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int delay = model.move();
        gameLoop.setDelay(delay);
        gamePanel.repaint();

        if (model.isGameOver()) {
            gameLoop.stop();
            gamePanel.showRestartButton(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_UP -> model.changeDirection(0, -1);
//            case KeyEvent.VK_DOWN -> model.changeDirection(0, 1);
//            case KeyEvent.VK_LEFT -> model.changeDirection(-1, 0);
//            case KeyEvent.VK_RIGHT -> model.changeDirection(1, 0);
//            default -> {
//            }
//        }
        switch (e.getKeyCode()) {
            // Arrow keys
            case KeyEvent.VK_UP -> model.changeDirection(0, -1);
            case KeyEvent.VK_DOWN -> model.changeDirection(0, 1);
            case KeyEvent.VK_LEFT -> model.changeDirection(-1, 0);
            case KeyEvent.VK_RIGHT -> model.changeDirection(1, 0);

            // WASD keys
            case KeyEvent.VK_W -> model.changeDirection(0, -1);
            case KeyEvent.VK_S -> model.changeDirection(0, 1);
            case KeyEvent.VK_A -> model.changeDirection(-1, 0);
            case KeyEvent.VK_D -> model.changeDirection(1, 0);

            default -> {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
