package view;

import controller.GameController;
import model.SnakeGameModel;
import model.Tile;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel {

    private final SnakeGameModel model;
    private final JButton restartButton;
    private final JButton menuButton;

    public GamePanel(SnakeGameModel model) {
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

    public void setController(GameController controller) {
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
        drawWalls(g);
        if (!model.isGameOver()) {
            drawFood(g);
        }
        drawSnake(g);
        drawScore(g);
    }

    private void drawGrid(Graphics g) {
        int columns = model.getBoardWidth() / model.getTileSize();
        int rows = model.getBoardHeight() / model.getTileSize();

        for (int column = 0; column <= columns; column++) {
            int x = column * model.getTileSize();
            g.drawLine(x, 0, x, model.getBoardHeight());
        }

        for (int row = 0; row <= rows; row++) {
            int y = row * model.getTileSize();
            g.drawLine(0, y, model.getBoardWidth(), y);
        }
    }

    private void drawFood(Graphics g) {
        Graphics2D graphics = (Graphics2D) g.create();
        ClassicSnakeRenderer.drawFood(graphics, model.getFood(), model.getTileSize());
        graphics.dispose();
    }

    private void drawWalls(Graphics g) {
        g.setColor(new Color(120, 120, 120));
        for (Tile wallTile : model.getMap().getWallTiles()) {
            g.fillRect(
                    wallTile.getX() * model.getTileSize(),
                    wallTile.getY() * model.getTileSize(),
                    model.getTileSize(),
                    model.getTileSize()
            );
        }
    }

    private void drawSnake(Graphics g) {
        Graphics2D graphics = (Graphics2D) g.create();
        ClassicSnakeRenderer.drawSnake(
                graphics,
                model.getSnakeHead(),
                model.getSnakeBody(),
                model.getTileSize(),
                model.getVelocityX(),
                model.getVelocityY()
        );
        graphics.dispose();
    }

    private void drawScore(Graphics g) {
        if (model.isGameOver()) {
            drawGameOver(g);
            return;
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Score: " + model.getScore(), 10, 20);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));

        String text = "GAME OVER";
        FontMetrics metrics = g.getFontMetrics();
        int x = (model.getBoardWidth() - metrics.stringWidth(text)) / 2;
        int y = model.getBoardHeight() / 2;
        g.drawString(text, x, y);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String scoreText = "Score: " + model.getScore();
        int scoreX = (model.getBoardWidth() - g.getFontMetrics().stringWidth(scoreText)) / 2;
        g.drawString(scoreText, scoreX, y + 30);
    }
}
