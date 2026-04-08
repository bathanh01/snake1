package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TwoPlayerSnakeGameModel {

    private static final int BASE_DELAY = 100;
    private static final int MIN_DELAY = 35;

    private final int boardWidth;
    private final int boardHeight;
    private final int tileSize;
    private final Random random;

    private final SnakeState playerOne;
    private final SnakeState playerTwo;
    private Tile playerOneFood;
    private Tile playerTwoFood;
    private boolean playerOneAlive;
    private boolean playerTwoAlive;
    private boolean gameOver;
    private String winnerText;

    public TwoPlayerSnakeGameModel(int boardWidth, int boardHeight, int tileSize) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = tileSize;
        this.random = new Random();
        this.playerOne = new SnakeState();
        this.playerTwo = new SnakeState();
        resetGame();
    }

    public void resetGame() {
        int columns = boardWidth / tileSize;
        int rows = boardHeight / tileSize;

        playerOne.reset(new Tile(4, rows / 2), 0, 0);
        playerTwo.reset(new Tile(columns - 5, rows / 2), 0, 0);
        playerOneFood = new Tile(0, 0);
        playerTwoFood = new Tile(0, 0);
        playerOneAlive = true;
        playerTwoAlive = true;
        gameOver = false;
        winnerText = "";
        placeFood(playerOneFood, null);
        placeFood(playerTwoFood, playerOneFood);
    }

    public int move() {
        if (gameOver) {
            return BASE_DELAY;
        }

        StepState playerOneState = stepSnake(playerOne, playerOneAlive);
        StepState playerTwoState = stepSnake(playerTwo, playerTwoAlive);

        boolean playerOneAte = playerOneAlive && isCollision(playerOne.head, playerOneFood);
        boolean playerTwoAte = playerTwoAlive && isCollision(playerTwo.head, playerTwoFood);

        if (playerOneAte) {
            growSnake(playerOne, playerOneState);
            placeFood(playerOneFood, playerTwoFood);
        }

        if (playerTwoAte) {
            growSnake(playerTwo, playerTwoState);
            placeFood(playerTwoFood, playerOneFood);
        }

        if (playerOneAlive && hasLost(playerOne)) {
            playerOneAlive = false;
        }

        if (playerTwoAlive && hasLost(playerTwo)) {
            playerTwoAlive = false;
        }

        if (!playerOneAlive && !playerTwoAlive) {
            gameOver = true;
            winnerText = determineWinnerText();
        }

        int longestSnake = Math.max(playerOne.body.size(), playerTwo.body.size());
        return Math.max(MIN_DELAY, BASE_DELAY - longestSnake * 2);
    }

    public void changePlayerOneDirection(int velocityX, int velocityY) {
        playerOne.changeDirection(velocityX, velocityY);
    }

    public void changePlayerTwoDirection(int velocityX, int velocityY) {
        playerTwo.changeDirection(velocityX, velocityY);
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

    public Tile getPlayerOneFood() {
        return playerOneFood;
    }

    public Tile getPlayerTwoFood() {
        return playerTwoFood;
    }

    public Tile getPlayerOneHead() {
        return playerOne.head;
    }

    public Tile getPlayerTwoHead() {
        return playerTwo.head;
    }

    public List<Tile> getPlayerOneBody() {
        return Collections.unmodifiableList(playerOne.body);
    }

    public List<Tile> getPlayerTwoBody() {
        return Collections.unmodifiableList(playerTwo.body);
    }

    public int getPlayerOneScore() {
        return playerOne.body.size();
    }

    public int getPlayerTwoScore() {
        return playerTwo.body.size();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isPlayerOneAlive() {
        return playerOneAlive;
    }

    public boolean isPlayerTwoAlive() {
        return playerTwoAlive;
    }

    public boolean shouldShowPlayerOneFood() {
        return playerOneAlive && !gameOver;
    }

    public boolean shouldShowPlayerTwoFood() {
        return playerTwoAlive && !gameOver;
    }

    public String getWinnerText() {
        return winnerText;
    }

    private StepState stepSnake(SnakeState snake, boolean isAlive) {
        StepState state = new StepState(new Tile(snake.head), getTailAnchor(snake));

        if (!isAlive) {
            return state;
        }

        for (int i = snake.body.size() - 1; i >= 0; i--) {
            Tile segment = snake.body.get(i);
            if (i == 0) {
                segment.setX(snake.head.getX());
                segment.setY(snake.head.getY());
            } else {
                Tile previousSegment = snake.body.get(i - 1);
                segment.setX(previousSegment.getX());
                segment.setY(previousSegment.getY());
            }
        }

        snake.head.setX(snake.head.getX() + snake.velocityX);
        snake.head.setY(snake.head.getY() + snake.velocityY);
        return state;
    }

    private Tile getTailAnchor(SnakeState snake) {
        if (snake.body.isEmpty()) {
            return new Tile(snake.head);
        }
        return new Tile(snake.body.get(snake.body.size() - 1));
    }

    private void growSnake(SnakeState snake, StepState state) {
        snake.body.add(new Tile(state.tailAnchor));
    }

    private boolean hasLost(SnakeState snake) {
        return isOutOfBounds(snake.head)
                || hitsOwnBody(snake);
    }

    private boolean hitsOwnBody(SnakeState snake) {
        for (Tile segment : snake.body) {
            if (isCollision(snake.head, segment)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOutOfBounds(Tile tile) {
        int columns = boardWidth / tileSize;
        int rows = boardHeight / tileSize;
        return tile.getX() < 0 || tile.getY() < 0 || tile.getX() >= columns || tile.getY() >= rows;
    }

    private boolean isCollision(Tile first, Tile second) {
        return first.getX() == second.getX() && first.getY() == second.getY();
    }

    private void placeFood(Tile targetFood, Tile otherFood) {
        int columns = boardWidth / tileSize;
        int rows = boardHeight / tileSize;

        do {
            targetFood.setX(random.nextInt(columns));
            targetFood.setY(random.nextInt(rows));
        } while (isOccupiedByAnySnake(targetFood) || overlapsOtherFood(targetFood, otherFood));
    }

    private boolean isOccupiedByAnySnake(Tile tile) {
        return occupiesTile(playerOne, tile) || occupiesTile(playerTwo, tile);
    }

    private boolean occupiesTile(SnakeState snake, Tile tile) {
        if (isCollision(tile, snake.head)) {
            return true;
        }
        for (Tile segment : snake.body) {
            if (isCollision(tile, segment)) {
                return true;
            }
        }
        return false;
    }

    private boolean overlapsOtherFood(Tile targetFood, Tile otherFood) {
        return otherFood != null && isCollision(targetFood, otherFood);
    }

// người win
    private String determineWinnerText() {
        int playerOneScore = getPlayerOneScore();
        int playerTwoScore = getPlayerTwoScore();

        if (playerOneScore == playerTwoScore) {
            return "Draw";
        }
        return playerOneScore > playerTwoScore ? "Player 1 wins" : "Player 2 wins";
    }

    private static final class SnakeState {
        private Tile head;
        private final List<Tile> body = new ArrayList<>();
        private int velocityX;
        private int velocityY;

        private void reset(Tile startHead, int startVelocityX, int startVelocityY) {
            head = startHead;
            body.clear();
            velocityX = startVelocityX;
            velocityY = startVelocityY;
        }

        private void changeDirection(int newVelocityX, int newVelocityY) {
            if (newVelocityX != 0 && velocityX == -newVelocityX) {
                return;
            }
            if (newVelocityY != 0 && velocityY == -newVelocityY) {
                return;
            }

            velocityX = newVelocityX;
            velocityY = newVelocityY;
        }
    }

    private static final class StepState {
        private final Tile previousHead;
        private final Tile tailAnchor;

        private StepState(Tile previousHead, Tile tailAnchor) {
            this.previousHead = previousHead;
            this.tailAnchor = tailAnchor;
        }
    }
}
