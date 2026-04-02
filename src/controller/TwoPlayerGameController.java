package controller;

import model.TwoPlayerSnakeGameModel;
import view.TwoPlayerGamePanel;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TwoPlayerGameController implements ActionListener, KeyListener {

    private final TwoPlayerSnakeGameModel model;
    private final TwoPlayerGamePanel gamePanel;
    private final Timer gameLoop;

    public TwoPlayerGameController(TwoPlayerSnakeGameModel model, TwoPlayerGamePanel gamePanel) {
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
        gamePanel.showGameOverButtons(false);
        gamePanel.repaint();
        gameLoop.stop();
        gamePanel.requestFocusInWindow();
    }

    public void resetGame() {
        model.resetGame();
        gamePanel.showGameOverButtons(false);
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
        boolean directionChanged = true;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> model.changePlayerOneDirection(0, -1);
            case KeyEvent.VK_S -> model.changePlayerOneDirection(0, 1);
            case KeyEvent.VK_A -> model.changePlayerOneDirection(-1, 0);
            case KeyEvent.VK_D -> model.changePlayerOneDirection(1, 0);

            case KeyEvent.VK_UP -> model.changePlayerTwoDirection(0, -1);
            case KeyEvent.VK_DOWN -> model.changePlayerTwoDirection(0, 1);
            case KeyEvent.VK_LEFT -> model.changePlayerTwoDirection(-1, 0);
            case KeyEvent.VK_RIGHT -> model.changePlayerTwoDirection(1, 0);

            default -> directionChanged = false;
        }

        if (directionChanged && !gameLoop.isRunning() && !model.isGameOver()) {
            gameLoop.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
