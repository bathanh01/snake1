package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Desert board: solid outer rim (touching it loses), plus a few inner rock clusters.
 */
public class DesertSinglePlayerMap implements SinglePlayerMap {

    private final int columns;
    private final int rows;
    private final List<Tile> wallTiles;

    public DesertSinglePlayerMap(int boardWidth, int boardHeight, int tileSize) {
        this.columns = boardWidth / tileSize;
        this.rows = boardHeight / tileSize;
        this.wallTiles = createWallTiles();
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int normalizeX(int x) {
        return x;
    }

    @Override
    public int normalizeY(int y) {
        return y;
    }

    @Override
    public boolean isOutOfBounds(Tile tile) {
        return tile.getX() < 0 || tile.getY() < 0 || tile.getX() >= columns || tile.getY() >= rows;
    }

    @Override
    public boolean hitsWall(Tile tile) {
        for (Tile wallTile : wallTiles) {
            if (wallTile.getX() == tile.getX() && wallTile.getY() == tile.getY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Tile> getWallTiles() {
        return Collections.unmodifiableList(wallTiles);
    }

    @Override
    public int getInitialSnakeHeadX() {
        return columns / 2;
    }

    @Override
    public int getInitialSnakeHeadY() {
        return rows / 2;
    }

    private List<Tile> createWallTiles() {
        List<Tile> walls = new ArrayList<>();

        for (int x = 0; x < columns; x++) {
            walls.add(new Tile(x, 0));
            walls.add(new Tile(x, rows - 1));
        }
        for (int y = 1; y < rows - 1; y++) {
            walls.add(new Tile(0, y));
            walls.add(new Tile(columns - 1, y));
        }

        addInnerRockClusters(walls);

        return walls;
    }

    private void addInnerRockClusters(List<Tile> walls) {
        walls.add(new Tile(6, 6));
        walls.add(new Tile(7, 6));
        walls.add(new Tile(6, 7));

        walls.add(new Tile(columns - 7, rows - 7));
        walls.add(new Tile(columns - 8, rows - 7));
        walls.add(new Tile(columns - 7, rows - 8));

        walls.add(new Tile(8, rows - 8));
        walls.add(new Tile(9, rows - 8));

        walls.add(new Tile(columns - 9, 7));
        walls.add(new Tile(columns - 10, 7));
    }
}
