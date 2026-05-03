package view;

import model.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.ImageIcon;
public final class ClassicSnakeRenderer {

    private static final String HEAD_PATH = "src/assets/classic_snake_head.png";
    private static final String BODY_PATH = "src/assets/classic_snake.png";
    private static final String FOOD_PATH = "src/assets/apple.png";

    private static java.awt.Image appleImage;       // táo đỏ
    private static java.awt.Image appleGreenImage;  // táo xanh
   //private static java.awt.Image blackAppleImage;  // táo đen

    private static final BufferedImage HEAD_IMAGE = loadImage(HEAD_PATH);
    private static final BufferedImage BODY_IMAGE = loadImage(BODY_PATH);
    private static final BufferedImage FOOD_IMAGE = loadImage(FOOD_PATH);

    private static final BufferedImage HEAD_TINTED_STANDARD =
            multiplyTint(HEAD_IMAGE, 0.45f, 0.98f, 0.68f);

    private static final BufferedImage BODY_TINTED_STANDARD =
            multiplyTint(BODY_IMAGE, 0.45f, 0.98f, 0.68f);

    /** Desert: warm earth brown — head lighter than body so it pops on beige sand */
    private static final BufferedImage HEAD_TINTED_DESERT =
            multiplyTint(HEAD_IMAGE, 0.65f, 0.46f, 0.30f);

    private static final BufferedImage BODY_TINTED_DESERT =
            multiplyTint(BODY_IMAGE, 0.48f, 0.34f, 0.22f);

    private ClassicSnakeRenderer() {
    }

    public static void drawFood(Graphics2D graphics, Tile food, int tileSize) {
        drawImage(graphics, FOOD_IMAGE, food.getX(), food.getY(), tileSize, 0);
    }

    static {

        appleGreenImage = new ImageIcon("src/assets/apple1.png").getImage();

//

        appleImage = new ImageIcon("src/assets/apple.png").getImage();
    }

    public static java.awt.Image getAppleImage() {
        return appleImage;
    }
//    public static java.awt.Image getBlackAppleImage() {
//        return blackAppleImage;
//    }
    public static java.awt.Image getAppleGreenImage() {
        return appleGreenImage;
    }
    public static void drawSnake(Graphics2D graphics, Tile head, List<Tile> body, int tileSize, int velocityX, int velocityY,
                                 boolean desertVisual) {
        if (head == null) {
            return;
        }

        BufferedImage bodyImg = desertVisual ? BODY_TINTED_DESERT : BODY_TINTED_STANDARD;
        BufferedImage headImg = desertVisual ? HEAD_TINTED_DESERT : HEAD_TINTED_STANDARD;

        Tile previous = head;
        for (int i = 0; i < body.size(); i++) {
            Tile segment = body.get(i);
            drawBodyConnector(graphics, previous, segment, tileSize, bodyImg, 0, 0);
            drawImageAtCenter(graphics, bodyImg, centerX(segment, tileSize), centerY(segment, tileSize), tileSize, 0);
            previous = segment;
        }

        drawImageAtCenter(graphics, headImg, centerX(head, tileSize), centerY(head, tileSize), tileSize, headRotation(velocityX, velocityY));
    }

    private static void drawImage(Graphics2D graphics, BufferedImage image, int gridX, int gridY, int tileSize, double rotation) {
        drawImageAtCenter(graphics, image, gridX * tileSize + tileSize / 2.0, gridY * tileSize + tileSize / 2.0, tileSize, rotation);
    }

    private static void drawImageAtCenter(Graphics2D graphics, BufferedImage image, double centerX, double centerY, int tileSize, double rotation) {
        AffineTransform previousTransform = graphics.getTransform();

        graphics.translate(centerX, centerY);
        graphics.rotate(rotation);
        graphics.drawImage(image, -tileSize / 2, -tileSize / 2, tileSize, tileSize, null);
        graphics.setTransform(previousTransform);
    }

    private static void drawBodyConnector(Graphics2D graphics, Tile start, Tile end, int tileSize, BufferedImage bodyImage,
                                          double endOffsetX, double endOffsetY) {
        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();

        if (Math.abs(deltaX) + Math.abs(deltaY) != 1) {
            return;
        }

        double centerStartX = centerX(start, tileSize);
        double centerStartY = centerY(start, tileSize);
        double centerEndX = centerX(end, tileSize) + endOffsetX;
        double centerEndY = centerY(end, tileSize) + endOffsetY;

        int connectorCenterX = (int) Math.round((centerStartX + centerEndX) / 2.0);
        int connectorCenterY = (int) Math.round((centerStartY + centerEndY) / 2.0);
        int connectorWidth = deltaX != 0 ? tileSize + tileSize / 2 : tileSize;
        int connectorHeight = deltaY != 0 ? tileSize + tileSize / 2 : tileSize;

        graphics.drawImage(
                bodyImage,
                connectorCenterX - connectorWidth / 2,
                connectorCenterY - connectorHeight / 2,
                connectorWidth,
                connectorHeight,
                null
        );
    }

    private static double centerX(Tile tile, int tileSize) {
        return tile.getX() * tileSize + tileSize / 2.0;
    }

    private static double centerY(Tile tile, int tileSize) {
        return tile.getY() * tileSize + tileSize / 2.0;
    }

    private static BufferedImage multiplyTint(BufferedImage source, float red, float green, float blue) {
        float[] scales = { red, green, blue, 1f };
        float[] offsets = new float[4];
        return new RescaleOp(scales, offsets, null).filter(source, null);
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
