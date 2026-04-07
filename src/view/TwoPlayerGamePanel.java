package view;

import controller.TwoPlayerGameController;
import model.Tile;
import model.TwoPlayerSnakeGameModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
        styleActionButton(restartButton, new Color(48, 138, 92));
        restartButton.setBounds(model.getBoardWidth() / 2 - 75, model.getBoardHeight() / 2 + 80, 150, 40);
        restartButton.setVisible(false);
        add(restartButton);

        menuButton = new JButton("Back to Menu");
        styleActionButton(menuButton, new Color(128, 92, 54));
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
        drawTile(g, food, color);
    }

    private void drawSnake(Graphics g, Tile head, java.util.List<Tile> body, Color color) {
        drawTile(g, head, color);

        for (Tile segment : body) {
            drawTile(g, segment, color);
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
        Graphics2D graphics = (Graphics2D) g;
        graphics.setColor(new Color(0, 0, 0, 150));
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setColor(new Color(46, 52, 58, 220));
        graphics.fillRoundRect(model.getBoardWidth() / 2 - 150, model.getBoardHeight() / 2 - 80, 300, 170, 24, 24);
        graphics.setColor(new Color(180, 200, 214, 110));
        graphics.setStroke(new BasicStroke(2f));
        graphics.drawRoundRect(model.getBoardWidth() / 2 - 150, model.getBoardHeight() / 2 - 80, 300, 170, 24, 24);

        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Arial", Font.BOLD, 40));

        String text = "GAME OVER";
        FontMetrics titleMetrics = graphics.getFontMetrics();
        int titleX = (model.getBoardWidth() - titleMetrics.stringWidth(text)) / 2;
        int titleY = model.getBoardHeight() / 2 - 10;
        graphics.drawString(text, titleX, titleY);

        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        String playerOneScore = "P1: " + model.getPlayerOneScore();
        String playerTwoScore = "P2: " + model.getPlayerTwoScore();
        int scoreY = titleY + 30;
        int centerX = model.getBoardWidth() / 2;
        int gap = 30;
        FontMetrics scoreMetrics = graphics.getFontMetrics();
        int playerOneX = centerX - gap - scoreMetrics.stringWidth(playerOneScore);
        int playerTwoX = centerX + gap;
        graphics.drawString(playerOneScore, playerOneX, scoreY);
        graphics.drawString(playerTwoScore, playerTwoX, scoreY);

        String winnerText = model.getWinnerText();
        int winnerX = (model.getBoardWidth() - scoreMetrics.stringWidth(winnerText)) / 2;
        graphics.drawString(winnerText, winnerX, scoreY + 30);
    }

    private void drawTile(Graphics g, Tile tile, Color color) {
        g.setColor(color);
        g.fillRect(
                tile.getX() * model.getTileSize(),
                tile.getY() * model.getTileSize(),
                model.getTileSize(),
                model.getTileSize()
        );
    }

    private void styleActionButton(JButton button, Color background) {
        button.setFocusable(false);
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setFocusPainted(false);
    }
}
