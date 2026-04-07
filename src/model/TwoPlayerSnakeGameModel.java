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
    private final List<Tile> playerOneBody;
    private final List<Tile> playerTwoBody;

    private Tile playerOneHead;
    private Tile playerTwoHead;
    private Tile playerOneFood;
    private Tile playerTwoFood;
    private int playerOneVelocityX;
    private int playerOneVelocityY;
    private int playerTwoVelocityX;
    private int playerTwoVelocityY;
    private boolean playerOneAlive;
    private boolean playerTwoAlive;
    private boolean gameOver;
    private String winnerText;

    public TwoPlayerSnakeGameModel(int boardWidth, int boardHeight, int tileSize) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = tileSize;
        this.random = new Random();
        this.playerOneBody = new ArrayList<>();
        this.playerTwoBody = new ArrayList<>();
        resetGame();
    }

    public void resetGame() {
        int columns = boardWidth / tileSize;
        int rows = boardHeight / tileSize;

        playerOneHead = new Tile(4, rows / 2);
        playerTwoHead = new Tile(columns - 5, rows / 2);
        playerOneBody.clear();
        playerTwoBody.clear();

        playerOneVelocityX = 0;
        playerOneVelocityY = 0;
        playerTwoVelocityX = 0;
        playerTwoVelocityY = 0;

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
            return getCurrentDelay();
        }

        boolean playerOneMoves = playerOneAlive && isMoving(playerOneVelocityX, playerOneVelocityY);
        boolean playerTwoMoves = playerTwoAlive && isMoving(playerTwoVelocityX, playerTwoVelocityY);

        Tile nextPlayerOneHead = createNextHead(playerOneHead, playerOneVelocityX, playerOneVelocityY, playerOneMoves);
        Tile nextPlayerTwoHead = createNextHead(playerTwoHead, playerTwoVelocityX, playerTwoVelocityY, playerTwoMoves);

        boolean playerOneAteFood = playerOneMoves && isCollision(nextPlayerOneHead, playerOneFood);
        boolean playerTwoAteFood = playerTwoMoves && isCollision(nextPlayerTwoHead, playerTwoFood);

        List<Tile> nextPlayerOneBody = createNextBody(playerOneHead, playerOneBody, playerOneMoves, playerOneAteFood);
        List<Tile> nextPlayerTwoBody = createNextBody(playerTwoHead, playerTwoBody, playerTwoMoves, playerTwoAteFood);

        boolean playerOneLost = playerOneAlive && hasLost(nextPlayerOneHead, nextPlayerOneBody, nextPlayerTwoBody);
        boolean playerTwoLost = playerTwoAlive && hasLost(nextPlayerTwoHead, nextPlayerTwoBody, nextPlayerOneBody);

        if (isCollision(nextPlayerOneHead, nextPlayerTwoHead)) {
            if (playerOneMoves && playerTwoMoves) {
                playerOneLost = true;
                playerTwoLost = true;
            } else if (playerOneMoves) {
                playerOneLost = true;
            } else if (playerTwoMoves) {
                playerTwoLost = true;
            }
        }

        if (playerOneMoves
                && playerTwoMoves
                && isCollision(nextPlayerOneHead, playerTwoHead)
                && isCollision(nextPlayerTwoHead, playerOneHead)) {
            playerOneLost = true;
            playerTwoLost = true;
        }

        applyNextState(playerOneHead, playerOneBody, nextPlayerOneHead, nextPlayerOneBody);
        applyNextState(playerTwoHead, playerTwoBody, nextPlayerTwoHead, nextPlayerTwoBody);

        playerOneAlive = playerOneAlive && !playerOneLost;
        playerTwoAlive = playerTwoAlive && !playerTwoLost;

        if (playerOneAteFood) {
            placeFood(playerOneFood, playerTwoFood);
        }

        if (playerTwoAteFood) {
            placeFood(playerTwoFood, playerOneFood);
        }

        if (!playerOneAlive || !playerTwoAlive) {
            gameOver = true;
            winnerText = determineWinnerText();
        }

        return getCurrentDelay();
    }

    public void changePlayerOneDirection(int velocityX, int velocityY) {
        if (!isReverseDirection(playerOneVelocityX, playerOneVelocityY, velocityX, velocityY)) {
            playerOneVelocityX = velocityX;
            playerOneVelocityY = velocityY;
        }
    }

    public void changePlayerTwoDirection(int velocityX, int velocityY) {
        if (!isReverseDirection(playerTwoVelocityX, playerTwoVelocityY, velocityX, velocityY)) {
            playerTwoVelocityX = velocityX;
            playerTwoVelocityY = velocityY;
        }
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
        return playerOneHead;
    }

    public Tile getPlayerTwoHead() {
        return playerTwoHead;
    }

    public List<Tile> getPlayerOneBody() {
        return Collections.unmodifiableList(playerOneBody);
    }

    public List<Tile> getPlayerTwoBody() {
        return Collections.unmodifiableList(playerTwoBody);
    }

    public int getPlayerOneScore() {
        return playerOneBody.size();
    }

    public int getPlayerTwoScore() {
        return playerTwoBody.size();
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

    private int getCurrentDelay() {
        int longestSnake = Math.max(playerOneBody.size(), playerTwoBody.size());
        return Math.max(MIN_DELAY, BASE_DELAY - longestSnake * 2);
    }

    private Tile createNextHead(Tile currentHead, int velocityX, int velocityY, boolean moves) {
        if (!moves) {
            return new Tile(currentHead);
        }

        return new Tile(currentHead.getX() + velocityX, currentHead.getY() + velocityY);
    }

    private List<Tile> createNextBody(Tile head, List<Tile> body, boolean moves, boolean grows) {
        if (!moves) {
            return copyTiles(body);
        }

        List<Tile> previousPositions = new ArrayList<>();
        previousPositions.add(new Tile(head));
        previousPositions.addAll(copyTiles(body));

        int segmentCount = body.size() + (grows ? 1 : 0);
        List<Tile> nextBody = new ArrayList<>(segmentCount);
        for (int index = 0; index < segmentCount; index++) {
            nextBody.add(previousPositions.get(index));
        }
        return nextBody;
    }

    private void applyNextState(Tile head, List<Tile> body, Tile nextHead, List<Tile> nextBody) {
        head.setPosition(nextHead);
        body.clear();
        body.addAll(nextBody);
    }

    private boolean hasLost(Tile head, List<Tile> ownBody, List<Tile> otherBody) {
        return isOutOfBounds(head) || hitsBody(head, ownBody) || hitsBody(head, otherBody);
    }

    private boolean hitsBody(Tile head, List<Tile> body) {
        for (Tile segment : body) {
            if (isCollision(head, segment)) {
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

    private void placeFood(Tile targetFood, Tile otherFood) {
        int columns = boardWidth / tileSize;
        int rows = boardHeight / tileSize;

        do {
            targetFood.setPosition(random.nextInt(columns), random.nextInt(rows));
        } while (isOccupiedByAnySnake(targetFood) || overlapsOtherFood(targetFood, otherFood));
    }

    private boolean isOccupiedByAnySnake(Tile tile) {
        return occupiesSnake(playerOneHead, playerOneBody, tile) || occupiesSnake(playerTwoHead, playerTwoBody, tile);
    }

    private boolean occupiesSnake(Tile head, List<Tile> body, Tile tile) {
        if (isCollision(head, tile)) {
            return true;
        }

        for (Tile segment : body) {
            if (isCollision(segment, tile)) {
                return true;
            }
        }

        return false;
    }

    private boolean overlapsOtherFood(Tile targetFood, Tile otherFood) {
        return otherFood != null && isCollision(targetFood, otherFood);
    }

    private boolean isMoving(int velocityX, int velocityY) {
        return velocityX != 0 || velocityY != 0;
    }

    private List<Tile> copyTiles(List<Tile> tiles) {
        List<Tile> copies = new ArrayList<>(tiles.size());
        for (Tile tile : tiles) {
            copies.add(new Tile(tile));
        }
        return copies;
    }

    private boolean isReverseDirection(int currentVelocityX, int currentVelocityY, int nextVelocityX, int nextVelocityY) {
        if (nextVelocityX != 0 && currentVelocityX == -nextVelocityX) {
            return true;
        }
        if (nextVelocityY != 0 && currentVelocityY == -nextVelocityY) {
            return true;
        }
        return false;
    }

    private boolean isCollision(Tile first, Tile second) {
        return first.getX() == second.getX() && first.getY() == second.getY();
    }

    private String determineWinnerText() {
        if (playerOneAlive && !playerTwoAlive) {
            return "Player 1 wins";
        }

        if (playerTwoAlive && !playerOneAlive) {
            return "Player 2 wins";
        }

        if (getPlayerOneScore() == getPlayerTwoScore()) {
            return "Draw";
        }

        if (getPlayerOneScore() > getPlayerTwoScore()) {
            return "Player 1 wins";
        }

        return "Player 2 wins";
    }
}
