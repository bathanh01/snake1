package view;

import controller.TwoPlayerGameController;
import model.Tile;
import model.TwoPlayerSnakeGameModel;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class TwoPlayerGamePanel extends JPanel {

    private final TwoPlayerSnakeGameModel model;
    private final JButton restartButton;
    private final JButton menuButton;

    public TwoPlayerGamePanel(TwoPlayerSnakeGameModel model) {
        this.model = model;

        setPreferredSize(new Dimension(model.getBoardWidth(), model.getBoardHeight()));
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);

        restartButton = new JButton("Play Again");
        restartButton.setFocusable(false);
        restartButton.setBounds(model.getBoardWidth() / 2 - 75, model.getBoardHeight() / 2 + 80, 150, 40);
        restartButton.setVisible(false);
        add(restartButton);

        menuButton = new JButton("Back to Menu");
        menuButton.setFocusable(false);
        menuButton.setBounds(model.getBoardWidth() / 2 - 75, model.getBoardHeight() / 2 + 130, 150, 40);
        menuButton.setVisible(false);
        add(menuButton);
    }

    public void setController(TwoPlayerGameController controller) {
        restartButton.addActionListener(e -> controller.resetGame());
    }

    public void setMenuAction(Runnable menuAction) {
        menuButton.addActionListener(e -> menuAction.run());
    }

    public void showGameOverButtons(boolean visible) {
        restartButton.setVisible(visible);
        menuButton.setVisible(visible);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        if (model.shouldShowPlayerOneFood()) {
            drawFood(g, model.getPlayerOneFood(), new Color(144, 238, 144));
        }
        if (model.shouldShowPlayerTwoFood()) {
            drawFood(g, model.getPlayerTwoFood(), new Color(135, 206, 250));
        }
        if (model.isPlayerOneAlive() || model.isGameOver()) {
            drawSnake(g, model.getPlayerOneHead(), model.getPlayerOneBody(), new Color(57, 255, 20));
        }
        if (model.isPlayerTwoAlive() || model.isGameOver()) {
            drawSnake(g, model.getPlayerTwoHead(), model.getPlayerTwoBody(), new Color(0, 191, 255));
        }
        drawHud(g);
    }

    private void drawGrid(Graphics g) {
        int columns = model.getBoardWidth() / model.getTileSize();
        int rows = model.getBoardHeight() / model.getTileSize();
        g.setColor(new Color(45, 45, 45));

        for (int column = 0; column <= columns; column++) {
            int x = column * model.getTileSize();
            g.drawLine(x, 0, x, model.getBoardHeight());
        }

        for (int row = 0; row <= rows; row++) {
            int y = row * model.getTileSize();
            g.drawLine(0, y, model.getBoardWidth(), y);
        }
    }

    private void drawFood(Graphics g, Tile food, Color color) {
        g.setColor(color);
        g.fillRect(
                food.getX() * model.getTileSize(),
                food.getY() * model.getTileSize(),
                model.getTileSize(),
                model.getTileSize()
        );
    }

    private void drawSnake(Graphics g, Tile head, java.util.List<Tile> body, Color color) {
        g.setColor(color);
        g.fillRect(
                head.getX() * model.getTileSize(),
                head.getY() * model.getTileSize(),
                model.getTileSize(),
                model.getTileSize()
        );

        for (Tile segment : body) {
            g.fillRect(
                    segment.getX() * model.getTileSize(),
                    segment.getY() * model.getTileSize(),
                    model.getTileSize(),
                    model.getTileSize()
            );
        }
    }

    private void drawHud(Graphics g) {
        if (model.isGameOver()) {
            drawGameOver(g);
            return;
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("P1 Score: " + model.getPlayerOneScore(), 10, 20);

        String playerTwoScore = "P2 Score: " + model.getPlayerTwoScore();
        int playerTwoWidth = g.getFontMetrics().stringWidth(playerTwoScore);
        g.drawString(playerTwoScore, model.getBoardWidth() - playerTwoWidth - 10, 20);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));

        String text = "GAME OVER";
        FontMetrics titleMetrics = g.getFontMetrics();
        int titleX = (model.getBoardWidth() - titleMetrics.stringWidth(text)) / 2;
        int titleY = model.getBoardHeight() / 2;
        g.drawString(text, titleX, titleY);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String playerOneScore = "P1: " + model.getPlayerOneScore();
        String playerTwoScore = "P2: " + model.getPlayerTwoScore();
        int scoreY = titleY + 30;
        int centerX = model.getBoardWidth() / 2;
        int gap = 30;
        FontMetrics scoreMetrics = g.getFontMetrics();
        int playerOneX = centerX - gap - scoreMetrics.stringWidth(playerOneScore);
        int playerTwoX = centerX + gap;
        g.drawString(playerOneScore, playerOneX, scoreY);
        g.drawString(playerTwoScore, playerTwoX, scoreY);

        String winnerText = model.getWinnerText();
        int winnerX = (model.getBoardWidth() - scoreMetrics.stringWidth(winnerText)) / 2;
        g.drawString(winnerText, winnerX, scoreY + 30);
    }
}
