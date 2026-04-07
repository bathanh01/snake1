package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SnakeGameModel {

    private static final int BASE_DELAY = 150;
    private static final int MIN_DELAY = 80;

    private final int boardWidth;
    private final int boardHeight;
    private final int tileSize;
    private final Random random;
    private final List<Tile> snakeBody;

    private SinglePlayerMap map;
    private Tile snakeHead;
    private Tile food;
    private int velocityX;
    private int velocityY;
    private boolean gameOver;

    public SnakeGameModel(int boardWidth, int boardHeight, int tileSize) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = tileSize;
        this.random = new Random();
        this.snakeBody = new ArrayList<>();
        this.map = new DefaultSinglePlayerMap(boardWidth, boardHeight, tileSize);
        resetGame();
    }

    public void setMap(SinglePlayerMap map) {
        this.map = map;
        resetGame();
    }

    public void resetGame() {
        snakeHead = findValidSpawnTile();
        snakeBody.clear();
        food = new Tile(0, 0);
        velocityX = 0;
        velocityY = 0;
        gameOver = false;
        placeFood();
    }

    public int move() {
        if (gameOver) {
            return getCurrentDelay();
        }

        if (velocityX == 0 && velocityY == 0) {
            return getCurrentDelay();
        }

        Tile nextHead = createNextHead();
        if (map.isOutOfBounds(nextHead) || map.hitsWall(nextHead)) {
            gameOver = true;
            return getCurrentDelay();
        }

        boolean ateFood = isCollision(nextHead, food);
        Tile lastBodyPosition = moveBody();
        snakeHead.setPosition(nextHead);

        if (ateFood) {
            snakeBody.add(lastBodyPosition);
            placeFood();
        }

        if (hitsBody(snakeHead)) {
            gameOver = true;
        }

        return getCurrentDelay();
    }

    public void changeDirection(int newVelocityX, int newVelocityY) {
        if (newVelocityX != 0 && velocityX == -newVelocityX) {
            return;
        }
        if (newVelocityY != 0 && velocityY == -newVelocityY) {
            return;
        }

        velocityX = newVelocityX;
        velocityY = newVelocityY;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

    public Tile getSnakeHead() {
        return snakeHead;
    }

    public List<Tile> getSnakeBody() {
        return Collections.unmodifiableList(snakeBody);
    }

    public Tile getFood() {
        return food;
    }

    public SinglePlayerMap getMap() {
        return map;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public int getScore() {
        return snakeBody.size();
    }

    public int getBaseDelay() {
        return BASE_DELAY;
    }

    private int getCurrentDelay() {
        return Math.max(MIN_DELAY, BASE_DELAY - snakeBody.size() * 2);
    }

    private Tile createNextHead() {
        int nextX = snakeHead.getX() + velocityX;
        int nextY = snakeHead.getY() + velocityY;
        nextX = map.normalizeX(nextX);
        nextY = map.normalizeY(nextY);
        return new Tile(nextX, nextY);
    }

    private Tile moveBody() {
        int previousX = snakeHead.getX();
        int previousY = snakeHead.getY();
        int lastX = previousX;
        int lastY = previousY;

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile segment = snakeBody.get(i);
            int currentX = segment.getX();
            int currentY = segment.getY();

            segment.setPosition(previousX, previousY);

            previousX = currentX;
            previousY = currentY;
            lastX = currentX;
            lastY = currentY;
        }

        return new Tile(lastX, lastY);
    }

    private void placeFood() {
        do {
            food.setPosition(random.nextInt(map.getColumns()), random.nextInt(map.getRows()));
        } while (map.blocksSpawn(food) || occupiesSnake(food));
    }

    private Tile findValidSpawnTile() {
        Tile startTile = new Tile(map.getInitialSnakeHead());
        if (!map.blocksSpawn(startTile)) {
            return startTile;
        }

        for (int y = 0; y < map.getRows(); y++) {
            for (int x = 0; x < map.getColumns(); x++) {
                Tile tile = new Tile(x, y);
                if (!map.blocksSpawn(tile)) {
                    return tile;
                }
            }
        }

        throw new IllegalStateException("Map does not have an empty tile.");
    }

    private boolean hitsBody(Tile tile) {
        for (Tile segment : snakeBody) {
            if (isCollision(tile, segment)) {
                return true;
            }
        }
        return false;
    }

    private boolean occupiesSnake(Tile tile) {
        if (isCollision(tile, snakeHead)) {
            return true;
        }

        return hitsBody(tile);
    }

    private boolean isCollision(Tile firstTile, Tile secondTile) {
        return firstTile.getX() == secondTile.getX() && firstTile.getY() == secondTile.getY();
    }
}
