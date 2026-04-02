package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SnakeGameModel {

    private static final int BASE_DELAY = 100;
    private static final int MIN_DELAY = 30;

    private final int boardWidth;
    private final int boardHeight;
    private final int tileSize;
    private final Random random;

    private Tile snakeHead;
    private final List<Tile> snakeBody;
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
        resetGame();
    }

    public void resetGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        food = new Tile(10, 10);
        velocityX = 0;
        velocityY = 0;
        gameOver = false;
        placeFood();
    }

    public int move() {
        if (gameOver) {
            return BASE_DELAY;
        }

        if (isCollision(snakeHead, food)) {
            snakeBody.add(new Tile(food));
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.setX(snakeHead.getX());
                snakePart.setY(snakeHead.getY());
            } else {
                Tile previousSnakePart = snakeBody.get(i - 1);
                snakePart.setX(previousSnakePart.getX());
                snakePart.setY(previousSnakePart.getY());
            }
        }

        snakeHead.setX(snakeHead.getX() + velocityX);
        snakeHead.setY(snakeHead.getY() + velocityY);

        if (isOutOfBounds() || hasSelfCollision()) {
            gameOver = true;
        }

        return Math.max(MIN_DELAY, BASE_DELAY - snakeBody.size() * 2);
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

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return snakeBody.size();
    }

    private void placeFood() {
        food.setX(random.nextInt(boardWidth / tileSize));
        food.setY(random.nextInt(boardHeight / tileSize));
    }

    private boolean isCollision(Tile firstTile, Tile secondTile) {
        return firstTile.getX() == secondTile.getX() && firstTile.getY() == secondTile.getY();
    }

    private boolean hasSelfCollision() {
        for (Tile snakePart : snakeBody) {
            if (isCollision(snakeHead, snakePart)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOutOfBounds() {
        return snakeHead.getX() < 0
                || snakeHead.getY() < 0
                || snakeHead.getX() >= boardWidth / tileSize
                || snakeHead.getY() >= boardHeight / tileSize;
    }
}
