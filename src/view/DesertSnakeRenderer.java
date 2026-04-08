package view;

import model.Tile;

import java.awt.Graphics2D;
import java.util.List;

public final class DesertSnakeRenderer {

    private DesertSnakeRenderer() {
    }

    public static void drawFood(Graphics2D graphics, Tile food, int tileSize) {
        StyledSnakeRenderer.drawFood(graphics, food, tileSize, StyledSnakeRenderer.DESERT);
    }

    public static void drawSnake(Graphics2D graphics, Tile head, List<Tile> body, int tileSize, int velocityX, int velocityY) {
        StyledSnakeRenderer.drawSnake(graphics, head, body, tileSize, velocityX, velocityY, StyledSnakeRenderer.DESERT);
    }
}
