package view;

import model.Tile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public final class StyledSnakeRenderer {

    public static final Theme CLASSIC = new Theme(
            new Color(39, 141, 88),
            new Color(24, 96, 58),
            new Color(11, 49, 30),
            new Color(151, 233, 130),
            new Color(5, 20, 12, 90),
            new Color(205, 46, 58),
            new Color(255, 127, 102),
            new Color(84, 123, 41),
            new Color(16, 72, 46, 170),
            new Color(188, 245, 181, 120),
            Color.WHITE,
            new Color(28, 28, 28)
    );

    public static final Theme DESERT = new Theme(
            new Color(136, 92, 54),
            new Color(102, 64, 35),
            new Color(67, 37, 18),
            new Color(198, 153, 98),
            new Color(28, 16, 8, 95),
            new Color(214, 83, 52),
            new Color(255, 162, 126),
            new Color(111, 122, 55),
            new Color(82, 48, 25, 185),
            new Color(220, 190, 146, 120),
            new Color(255, 247, 220),
            new Color(38, 25, 20)
    );

    private StyledSnakeRenderer() {
    }

    public static void drawFood(Graphics2D graphics, Tile food, int tileSize, Theme theme) {
        Graphics2D g = (Graphics2D) graphics.create();
        applyQualityHints(g);

        double centerX = food.getX() * tileSize + tileSize / 2.0;
        double centerY = food.getY() * tileSize + tileSize / 2.0;
        double fruitSize = tileSize * 0.58;

        g.setColor(new Color(0, 0, 0, 55));
        g.fill(new Ellipse2D.Double(centerX - fruitSize * 0.45, centerY + tileSize * 0.14, fruitSize * 0.9, tileSize * 0.18));

        g.setColor(theme.foodColor);
        g.fill(new Ellipse2D.Double(centerX - fruitSize * 0.68, centerY - fruitSize * 0.38, fruitSize * 0.76, fruitSize * 0.76));
        g.fill(new Ellipse2D.Double(centerX - fruitSize * 0.08, centerY - fruitSize * 0.42, fruitSize * 0.76, fruitSize * 0.76));

        g.setColor(theme.foodHighlightColor);
        g.fill(new Ellipse2D.Double(centerX - fruitSize * 0.42, centerY - fruitSize * 0.18, fruitSize * 0.2, fruitSize * 0.2));

        g.setColor(new Color(90, 59, 29));
        g.setStroke(new BasicStroke(Math.max(2f, tileSize * 0.08f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Line2D.Double(centerX, centerY - fruitSize * 0.45, centerX + fruitSize * 0.06, centerY - fruitSize * 0.72));

        g.translate(centerX + fruitSize * 0.12, centerY - fruitSize * 0.6);
        g.rotate(-Math.PI / 4.5);
        g.setColor(theme.leafColor);
        g.fill(new Ellipse2D.Double(0, -fruitSize * 0.12, fruitSize * 0.45, fruitSize * 0.24));

        g.dispose();
    }

    public static void drawSnake(Graphics2D graphics, Tile head, List<Tile> body, int tileSize, int velocityX, int velocityY, Theme theme) {
        if (head == null) {
            return;
        }

        Graphics2D g = (Graphics2D) graphics.create();
        applyQualityHints(g);

        if (!body.isEmpty()) {
            Path2D bodyPath = buildBodyPath(head, body, tileSize);
            drawBodyPath(g, bodyPath, tileSize, theme);

            for (int index = 0; index < body.size(); index++) {
                drawBandMark(g, head, body, index, tileSize, theme);
            }

            Tile tail = body.get(body.size() - 1);
            Tile previousSegment = body.size() == 1 ? head : body.get(body.size() - 2);
            drawTail(g, tail, previousSegment, tileSize, theme);
        }

        drawHead(g, head, tileSize, velocityX, velocityY, theme);
        g.dispose();
    }

    private static void drawBodyPath(Graphics2D graphics, Path2D bodyPath, int tileSize, Theme theme) {
        Graphics2D shadowGraphics = (Graphics2D) graphics.create();
        shadowGraphics.translate(1.8, 2.2);
        shadowGraphics.setColor(theme.shadowColor);
        shadowGraphics.setStroke(new BasicStroke(tileSize * 0.66f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        shadowGraphics.draw(bodyPath);
        shadowGraphics.dispose();

        graphics.setColor(theme.outlineColor);
        graphics.setStroke(new BasicStroke(tileSize * 0.58f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(bodyPath);

        graphics.setColor(theme.bodyColor);
        graphics.setStroke(new BasicStroke(tileSize * 0.48f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(bodyPath);

        graphics.setColor(theme.highlightColor);
        graphics.setStroke(new BasicStroke(tileSize * 0.16f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(bodyPath);
    }

    private static void drawBandMark(Graphics2D graphics, Tile head, List<Tile> body, int index, int tileSize, Theme theme) {
        Tile segment = body.get(index);
        double centerX = segment.getX() * tileSize + tileSize / 2.0;
        double centerY = segment.getY() * tileSize + tileSize / 2.0;
        double angle = tangentAngle(head, body, index);

        Graphics2D g = (Graphics2D) graphics.create();
        g.translate(centerX, centerY);
        g.rotate(angle);

        float outerStroke = Math.max(2.6f, tileSize * 0.13f);
        float innerStroke = Math.max(1.2f, tileSize * 0.06f);
        g.setStroke(new BasicStroke(outerStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(theme.bandColor);
        g.draw(new Line2D.Double(-tileSize * 0.12, -tileSize * 0.18, -tileSize * 0.12, tileSize * 0.18));
        g.draw(new Line2D.Double(tileSize * 0.04, -tileSize * 0.2, tileSize * 0.04, tileSize * 0.2));
        g.draw(new Line2D.Double(tileSize * 0.2, -tileSize * 0.17, tileSize * 0.2, tileSize * 0.17));

        g.setStroke(new BasicStroke(innerStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(theme.bandHighlightColor);
        g.draw(new Line2D.Double(-tileSize * 0.12, -tileSize * 0.14, -tileSize * 0.12, tileSize * 0.14));
        g.draw(new Line2D.Double(tileSize * 0.04, -tileSize * 0.16, tileSize * 0.04, tileSize * 0.16));
        g.dispose();
    }

    private static Path2D buildBodyPath(Tile head, List<Tile> body, int tileSize) {
        List<Point2D.Double> points = new ArrayList<>();
        points.add(center(head, tileSize));
        for (Tile segment : body) {
            points.add(center(segment, tileSize));
        }

        Path2D.Double path = new Path2D.Double();
        Point2D.Double start = points.get(0);
        path.moveTo(start.x, start.y);

        if (points.size() == 2) {
            Point2D.Double end = points.get(1);
            path.lineTo(end.x, end.y);
            return path;
        }

        for (int index = 1; index < points.size() - 1; index++) {
            Point2D.Double current = points.get(index);
            Point2D.Double next = points.get(index + 1);
            double midX = (current.x + next.x) / 2.0;
            double midY = (current.y + next.y) / 2.0;
            path.quadTo(current.x, current.y, midX, midY);
        }

        Point2D.Double last = points.get(points.size() - 1);
        path.lineTo(last.x, last.y);
        return path;
    }

    private static void drawTail(Graphics2D graphics, Tile tail, Tile previousSegment, int tileSize, Theme theme) {
        int deltaX = tail.getX() - previousSegment.getX();
        int deltaY = tail.getY() - previousSegment.getY();
        double rotation = rotationForDirection(deltaX, deltaY);

        Graphics2D g = (Graphics2D) graphics.create();
        double centerX = tail.getX() * tileSize + tileSize / 2.0;
        double centerY = tail.getY() * tileSize + tileSize / 2.0;
        g.translate(centerX, centerY);
        g.rotate(rotation);

        Path2D tailShadow = new Path2D.Double();
        tailShadow.moveTo(-tileSize * 0.18 + 1.5, -tileSize * 0.13 + 2);
        tailShadow.curveTo(tileSize * 0.1 + 1.5, -tileSize * 0.18 + 2, tileSize * 0.36 + 1.5, -tileSize * 0.1 + 2, tileSize * 0.5 + 1.5, 2);
        tailShadow.curveTo(tileSize * 0.36 + 1.5, tileSize * 0.1 + 2, tileSize * 0.1 + 1.5, tileSize * 0.18 + 2, -tileSize * 0.18 + 1.5, tileSize * 0.13 + 2);
        tailShadow.closePath();
        g.setColor(theme.shadowColor);
        g.fill(tailShadow);

        Path2D tailShape = new Path2D.Double();
        tailShape.moveTo(-tileSize * 0.18, -tileSize * 0.13);
        tailShape.curveTo(tileSize * 0.1, -tileSize * 0.18, tileSize * 0.36, -tileSize * 0.1, tileSize * 0.5, 0);
        tailShape.curveTo(tileSize * 0.36, tileSize * 0.1, tileSize * 0.1, tileSize * 0.18, -tileSize * 0.18, tileSize * 0.13);
        tailShape.closePath();
        g.setColor(theme.bodyColor);
        g.fill(tailShape);
        g.setColor(theme.outlineColor);
        g.draw(tailShape);
        g.setColor(theme.highlightColor);
        g.setStroke(new BasicStroke(Math.max(1.4f, tileSize * 0.06f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Line2D.Double(-tileSize * 0.02, -tileSize * 0.04, tileSize * 0.28, 0));
        g.dispose();
    }

    private static void drawHead(Graphics2D graphics, Tile head, int tileSize, int velocityX, int velocityY, Theme theme) {
        Graphics2D g = (Graphics2D) graphics.create();
        double centerX = head.getX() * tileSize + tileSize / 2.0;
        double centerY = head.getY() * tileSize + tileSize / 2.0;
        g.translate(centerX, centerY);
        g.rotate(rotationForDirection(velocityX, velocityY));

        RoundRectangle2D shadow = new RoundRectangle2D.Double(
                -tileSize * 0.38 + 1.5,
                -tileSize * 0.31 + 2,
                tileSize * 0.9,
                tileSize * 0.62,
                tileSize * 0.35,
                tileSize * 0.35
        );
        g.setColor(theme.shadowColor);
        g.fill(shadow);

        RoundRectangle2D headShape = new RoundRectangle2D.Double(
                -tileSize * 0.38,
                -tileSize * 0.31,
                tileSize * 0.9,
                tileSize * 0.62,
                tileSize * 0.35,
                tileSize * 0.35
        );
        RoundRectangle2D snoutShape = new RoundRectangle2D.Double(
                tileSize * 0.08,
                -tileSize * 0.19,
                tileSize * 0.28,
                tileSize * 0.38,
                tileSize * 0.14,
                tileSize * 0.14
        );

        g.setColor(theme.headColor);
        g.fill(headShape);
        g.fill(snoutShape);

        g.setColor(theme.outlineColor);
        g.setStroke(new BasicStroke(Math.max(1.8f, tileSize * 0.06f)));
        g.draw(headShape);
        g.draw(snoutShape);

        g.setColor(theme.highlightColor);
        g.fill(new Ellipse2D.Double(-tileSize * 0.1, -tileSize * 0.2, tileSize * 0.3, tileSize * 0.14));

        g.setColor(theme.eyeColor);
        g.fill(new Ellipse2D.Double(tileSize * 0.02, -tileSize * 0.17, tileSize * 0.14, tileSize * 0.14));
        g.fill(new Ellipse2D.Double(tileSize * 0.02, tileSize * 0.03, tileSize * 0.14, tileSize * 0.14));

        g.setColor(theme.pupilColor);
        g.fill(new Ellipse2D.Double(tileSize * 0.08, -tileSize * 0.13, tileSize * 0.05, tileSize * 0.07));
        g.fill(new Ellipse2D.Double(tileSize * 0.08, tileSize * 0.07, tileSize * 0.05, tileSize * 0.07));

        g.setColor(theme.outlineColor);
        g.fill(new Ellipse2D.Double(tileSize * 0.24, -tileSize * 0.08, tileSize * 0.04, tileSize * 0.04));
        g.fill(new Ellipse2D.Double(tileSize * 0.24, tileSize * 0.04, tileSize * 0.04, tileSize * 0.04));

        g.dispose();
    }

    private static void applyQualityHints(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    private static double rotationForDirection(int directionX, int directionY) {
        if (directionX < 0) {
            return Math.PI;
        }
        if (directionY < 0) {
            return -Math.PI / 2;
        }
        if (directionY > 0) {
            return Math.PI / 2;
        }
        return 0;
    }

    public static final class Theme {
        private final Color bodyColor;
        private final Color headColor;
        private final Color outlineColor;
        private final Color highlightColor;
        private final Color shadowColor;
        private final Color foodColor;
        private final Color foodHighlightColor;
        private final Color leafColor;
        private final Color bandColor;
        private final Color bandHighlightColor;
        private final Color eyeColor;
        private final Color pupilColor;

        private Theme(
                Color bodyColor,
                Color headColor,
                Color outlineColor,
                Color highlightColor,
                Color shadowColor,
                Color foodColor,
                Color foodHighlightColor,
                Color leafColor,
                Color bandColor,
                Color bandHighlightColor,
                Color eyeColor,
                Color pupilColor
        ) {
            this.bodyColor = bodyColor;
            this.headColor = headColor;
            this.outlineColor = outlineColor;
            this.highlightColor = highlightColor;
            this.shadowColor = shadowColor;
            this.foodColor = foodColor;
            this.foodHighlightColor = foodHighlightColor;
            this.leafColor = leafColor;
            this.bandColor = bandColor;
            this.bandHighlightColor = bandHighlightColor;
            this.eyeColor = eyeColor;
            this.pupilColor = pupilColor;
        }
    }

    private static Point2D.Double center(Tile tile, int tileSize) {
        return new Point2D.Double(
                tile.getX() * tileSize + tileSize / 2.0,
                tile.getY() * tileSize + tileSize / 2.0
        );
    }

    private static double tangentAngle(Tile head, List<Tile> body, int index) {
        Tile previous = index == 0 ? head : body.get(index - 1);
        Tile next = index == body.size() - 1 ? body.get(index) : body.get(index + 1);

        double deltaX = next.getX() - previous.getX();
        double deltaY = next.getY() - previous.getY();

        if (deltaX == 0 && deltaY == 0) {
            deltaX = body.get(index).getX() - previous.getX();
            deltaY = body.get(index).getY() - previous.getY();
        }

        return Math.atan2(deltaY, deltaX);
    }
}
