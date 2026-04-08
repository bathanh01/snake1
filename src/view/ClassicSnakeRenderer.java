package view;

import model.Tile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class ClassicSnakeRenderer {

    private static final String HEAD_PATH = "src/assets/classic_snake_head.png";
    private static final String BODY_PATH = "src/assets/classic_snake.png";
    private static final String FOOD_PATH = "src/assets/apple.png";

    private static final BufferedImage HEAD_IMAGE = loadImage(HEAD_PATH);
    private static final BufferedImage BODY_IMAGE = loadImage(BODY_PATH);
    private static final BufferedImage FOOD_IMAGE = loadImage(FOOD_PATH);

    private ClassicSnakeRenderer() {
    }

    public static void drawFood(Graphics2D graphics, Tile food, int tileSize) {
        drawImage(graphics, FOOD_IMAGE, food.getX(), food.getY(), tileSize, 0);
    }

    public static void drawSnake(Graphics2D graphics, Tile head, List<Tile> body, int tileSize, int velocityX, int velocityY) {
        if (head == null) {
            return;
        }

        Tile previous = head;
        for (Tile segment : body) {
            drawBodyConnector(graphics, previous, segment, tileSize);
            drawImage(graphics, BODY_IMAGE, segment.getX(), segment.getY(), tileSize, 0);
            previous = segment;
        }

        drawImage(graphics, HEAD_IMAGE, head.getX(), head.getY(), tileSize, headRotation(velocityX, velocityY));
    }

    private static void drawImage(Graphics2D graphics, BufferedImage image, int gridX, int gridY, int tileSize, double rotation) {
        int drawX = gridX * tileSize;
        int drawY = gridY * tileSize;
        AffineTransform previousTransform = graphics.getTransform();

        graphics.translate(drawX + tileSize / 2.0, drawY + tileSize / 2.0);
        graphics.rotate(rotation);
        graphics.drawImage(image, -tileSize / 2, -tileSize / 2, tileSize, tileSize, null);
        graphics.setTransform(previousTransform);
    }

    private static void drawBodyConnector(Graphics2D graphics, Tile start, Tile end, int tileSize) {
        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();

        if (Math.abs(deltaX) + Math.abs(deltaY) != 1) {
            return;
        }

        int centerStartX = start.getX() * tileSize + tileSize / 2;
        int centerStartY = start.getY() * tileSize + tileSize / 2;
        int centerEndX = end.getX() * tileSize + tileSize / 2;
        int centerEndY = end.getY() * tileSize + tileSize / 2;

        int connectorCenterX = (centerStartX + centerEndX) / 2;
        int connectorCenterY = (centerStartY + centerEndY) / 2;
        int connectorWidth = deltaX != 0 ? tileSize + tileSize / 2 : tileSize;
        int connectorHeight = deltaY != 0 ? tileSize + tileSize / 2 : tileSize;

        graphics.drawImage(
                BODY_IMAGE,
                connectorCenterX - connectorWidth / 2,
                connectorCenterY - connectorHeight / 2,
                connectorWidth,
                connectorHeight,
                null
        );
    }

    private static double headRotation(int directionX, int directionY) {
        if (directionX == 1) {
            return Math.PI;
        }
        if (directionY == -1) {
            return Math.PI / 2;
        }
        if (directionY == 1) {
            return -Math.PI / 2;
        }
        return 0;
    }

    private static BufferedImage loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            BufferedImage converted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = converted.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            return converted;
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot load image: " + path, exception);
        }
    }
}
