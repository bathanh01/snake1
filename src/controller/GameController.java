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
        this.gameLoop = new Timer(model.getBaseDelay(), this);
        this.gamePanel.setController(this);
        this.gamePanel.addKeyListener(this);
    }

    public void startGame() {
        if (model.isGameOver()) {
            model.resetGame();
        }
        gamePanel.showGameOverButtons(false);
        gamePanel.repaint();
        gameLoop.setDelay(model.getBaseDelay());
        gameLoop.stop();
        gamePanel.requestFocusInWindow();
    }

    public void resetGame() {
        model.resetGame();
        gamePanel.showGameOverButtons(false);
        gameLoop.setDelay(model.getBaseDelay());
        gameLoop.setDelay(100);
        gameLoop.stop();
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
            gamePanel.showGameOverButtons(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean directionChanged = handleKeyPress(e.getKeyCode());
        boolean directionChanged = true;
        switch (e.getKeyCode()) {
            // Arrow keys
            case KeyEvent.VK_UP -> model.changeDirection(0, -1);
            case KeyEvent.VK_DOWN -> model.changeDirection(0, 1);
            case KeyEvent.VK_LEFT -> model.changeDirection(-1, 0);
            case KeyEvent.VK_RIGHT -> model.changeDirection(1, 0);

        if (directionChanged && !gameLoop.isRunning() && !model.isGameOver()) {
            gameLoop.start();
        }
    }

    private boolean handleKeyPress(int keyCode) {
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
            model.changeDirection(0, -1);
            return true;
        }
        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
            model.changeDirection(0, 1);
            return true;
            default -> directionChanged = false;
        }

        if (directionChanged && !gameLoop.isRunning() && !model.isGameOver()) {
            gameLoop.start();
        }
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            model.changeDirection(-1, 0);
            return true;
        }
        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
            model.changeDirection(1, 0);
            return true;
        }

        return false;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
