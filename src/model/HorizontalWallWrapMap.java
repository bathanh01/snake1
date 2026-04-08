package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HorizontalWallWrapMap implements SinglePlayerMap {

    private final int columns;
    private final int rows;
    private final List<Tile> wallTiles;

    public HorizontalWallWrapMap(int boardWidth, int boardHeight, int tileSize) {
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
        return (x + columns) % columns;
    }

    @Override
    public int normalizeY(int y) {
        return (y + rows) % rows;
    }

    @Override
    public boolean isOutOfBounds(Tile tile) {
        return false;
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

    private List<Tile> createWallTiles() {
        int middleRow = rows / 2;
        List<Tile> walls = new ArrayList<>();

        for (int x = 0; x < columns; x++) {
            walls.add(new Tile(x, middleRow));
        }
        return walls;
    }
}
