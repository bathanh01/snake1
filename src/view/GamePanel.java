package view;

import controller.GameController;
import model.DesertSinglePlayerMap;
import model.SnakeGameModel;
import model.Tile;

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
import java.awt.RenderingHints;

public class GamePanel extends JPanel {

    private static final Color CLASSIC_BACKGROUND = new Color(13, 24, 30);
    private static final Color CLASSIC_CELL_LIGHT = new Color(24, 40, 47);
    private static final Color CLASSIC_CELL_DARK = new Color(18, 31, 37);
    private static final Color CLASSIC_GRID = new Color(255, 255, 255, 18);
    private static final Color CLASSIC_WALL = new Color(79, 100, 112);
    private static final Color CLASSIC_WALL_HIGHLIGHT = new Color(188, 206, 215);
    private static final Color CLASSIC_WALL_SHADOW = new Color(6, 15, 20, 110);
    private static final Color CLASSIC_HUD = new Color(10, 18, 24, 190);
    private static final Color CLASSIC_HUD_BORDER = new Color(102, 170, 136, 130);

    private static final Color DESERT_BACKGROUND = new Color(243, 209, 88);
    private static final Color DESERT_CELL_LIGHT = new Color(250, 221, 116);
    private static final Color DESERT_CELL_DARK = new Color(235, 197, 74);
    private static final Color DESERT_GRID = new Color(120, 88, 24, 24);
    private static final Color DESERT_HUD = new Color(86, 54, 30, 185);
    private static final Color DESERT_HUD_BORDER = new Color(242, 208, 154, 120);

    private final SnakeGameModel model;
    private final JButton restartButton;
    private final JButton menuButton;

    public GamePanel(SnakeGameModel model) {
        this.model = model;

        setPreferredSize(new Dimension(model.getBoardWidth(), model.getBoardHeight()));
        setBackground(CLASSIC_BACKGROUND);
        setFocusable(true);
        setLayout(null);

        restartButton = new JButton("Play Again");
        styleActionButton(restartButton, new Color(62, 140, 89));
        restartButton.setBounds(model.getBoardWidth() / 2 - 75, model.getBoardHeight() / 2 + 80, 150, 40);
        restartButton.setVisible(false);
        add(restartButton);

        menuButton = new JButton("Back to Menu");
        styleActionButton(menuButton, new Color(146, 98, 54));
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

        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (isDesertMap()) {
            drawDesertBoard(graphics);
        } else {
            drawClassicBoard(graphics);
        }

        drawWalls(graphics);
        if (!model.isGameOver()) {
            drawFood(graphics);
        }
        drawSnake(graphics);
        drawScore(graphics);
        graphics.dispose();
    }

    private void drawClassicBoard(Graphics2D graphics) {
        graphics.setColor(CLASSIC_BACKGROUND);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        drawCheckerboard(graphics, CLASSIC_CELL_LIGHT, CLASSIC_CELL_DARK, CLASSIC_GRID);
    }

    private void drawDesertBoard(Graphics2D graphics) {
        graphics.setColor(DESERT_BACKGROUND);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        drawCheckerboard(graphics, DESERT_CELL_LIGHT, DESERT_CELL_DARK, DESERT_GRID);
    }

    private void drawFood(Graphics2D graphics) {
        Graphics2D foodGraphics = (Graphics2D) graphics.create();
        if (isDesertMap()) {
            DesertSnakeRenderer.drawFood(foodGraphics, model.getFood(), model.getTileSize());
        } else {
            ClassicSnakeRenderer.drawFood(foodGraphics, model.getFood(), model.getTileSize());
        }
        foodGraphics.dispose();
    }

    private void drawWalls(Graphics2D graphics) {
        if (isDesertMap()) {
            return;
        }

        for (Tile wallTile : model.getMap().getWallTiles()) {
            drawClassicWallTile(graphics, wallTile);
        }
    }

    private void drawSnake(Graphics2D graphics) {
        Graphics2D snakeGraphics = (Graphics2D) graphics.create();
        if (isDesertMap()) {
            DesertSnakeRenderer.drawSnake(
                    snakeGraphics,
                    model.getSnakeHead(),
                    model.getSnakeBody(),
                    model.getTileSize(),
                    model.getVelocityX(),
                    model.getVelocityY()
            );
        } else {
            ClassicSnakeRenderer.drawSnake(
                    snakeGraphics,
                    model.getSnakeHead(),
                    model.getSnakeBody(),
                    model.getTileSize(),
                    model.getVelocityX(),
                    model.getVelocityY()
            );
        }
        snakeGraphics.dispose();
    }

    private void drawScore(Graphics2D graphics) {
        if (model.isGameOver()) {
            drawGameOver(graphics);
            return;
        }

        String scoreText = "Score " + model.getScore();
        Font font = new Font("Arial", Font.BOLD, 16);
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics(font);
        int width = metrics.stringWidth(scoreText) + 26;
        int height = 34;

        graphics.setColor(isDesertMap() ? DESERT_HUD : CLASSIC_HUD);
        graphics.fillRoundRect(12, 12, width, height, 18, 18);
        graphics.setColor(isDesertMap() ? DESERT_HUD_BORDER : CLASSIC_HUD_BORDER);
        graphics.setStroke(new BasicStroke(1.6f));
        graphics.drawRoundRect(12, 12, width, height, 18, 18);
        graphics.setColor(isDesertMap() ? new Color(255, 245, 220) : new Color(232, 245, 236));
        graphics.drawString(scoreText, 25, 34);
    }

    private void drawGameOver(Graphics2D graphics) {
        graphics.setColor(new Color(0, 0, 0, isDesertMap() ? 120 : 150));
        graphics.fillRect(0, 0, getWidth(), getHeight());

        int cardWidth = 280;
        int cardHeight = 150;
        int cardX = (getWidth() - cardWidth) / 2;
        int cardY = (getHeight() - cardHeight) / 2 - 20;

        graphics.setColor(isDesertMap() ? new Color(88, 58, 34, 220) : new Color(18, 30, 37, 220));
        graphics.fillRoundRect(cardX, cardY, cardWidth, cardHeight, 26, 26);
        graphics.setColor(isDesertMap() ? new Color(239, 211, 164, 110) : new Color(129, 189, 162, 110));
        graphics.setStroke(new BasicStroke(2f));
        graphics.drawRoundRect(cardX, cardY, cardWidth, cardHeight, 26, 26);

        graphics.setFont(new Font("Arial", Font.BOLD, 34));
        graphics.setColor(Color.WHITE);

        String text = "GAME OVER";
        FontMetrics metrics = graphics.getFontMetrics();
        int x = (model.getBoardWidth() - metrics.stringWidth(text)) / 2;
        int y = cardY + 62;
        graphics.drawString(text, x, y);

        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        String scoreText = "Score: " + model.getScore();
        int scoreX = (model.getBoardWidth() - graphics.getFontMetrics().stringWidth(scoreText)) / 2;
        graphics.drawString(scoreText, scoreX, y + 32);
    }

    private boolean isDesertMap() {
        return model.getMap() instanceof DesertSinglePlayerMap;
    }

    private void drawCheckerboard(Graphics2D graphics, Color firstColor, Color secondColor, Color gridColor) {
        int tileSize = model.getTileSize();
        int columns = model.getBoardWidth() / tileSize;
        int rows = model.getBoardHeight() / tileSize;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                graphics.setColor(((x + y) & 1) == 0 ? firstColor : secondColor);
                graphics.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }

        graphics.setColor(gridColor);
        for (int column = 0; column <= columns; column++) {
            int x = column * tileSize;
            graphics.drawLine(x, 0, x, model.getBoardHeight());
        }
        for (int row = 0; row <= rows; row++) {
            int y = row * tileSize;
            graphics.drawLine(0, y, model.getBoardWidth(), y);
        }
    }

    private void drawClassicWallTile(Graphics2D graphics, Tile tile) {
        int tileSize = model.getTileSize();
        int x = tile.getX() * tileSize;
        int y = tile.getY() * tileSize;
        int inset = Math.max(2, tileSize / 8);
        int size = tileSize - inset * 2;

        graphics.setColor(CLASSIC_WALL_SHADOW);
        graphics.fillRoundRect(x + inset + 1, y + inset + 2, size, size, 8, 8);
        graphics.setColor(CLASSIC_WALL);
        graphics.fillRoundRect(x + inset, y + inset, size, size, 8, 8);
        graphics.setColor(CLASSIC_WALL_HIGHLIGHT);
        graphics.setStroke(new BasicStroke(1.4f));
        graphics.drawRoundRect(x + inset, y + inset, size, size, 8, 8);
        graphics.drawLine(x + inset + 3, y + inset + 4, x + inset + size - 4, y + inset + 4);
    }

    private void styleActionButton(JButton button, Color background) {
        button.setFocusable(false);
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setFocusPainted(false);
    }
}
