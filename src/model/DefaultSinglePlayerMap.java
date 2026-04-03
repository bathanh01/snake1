package model;

import java.util.Collections;
import java.util.List;

public class DefaultSinglePlayerMap implements SinglePlayerMap {

    private final int columns;
    private final int rows;

    public DefaultSinglePlayerMap(int boardWidth, int boardHeight, int tileSize) {
        this.columns = boardWidth / tileSize;
        this.rows = boardHeight / tileSize;
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
        return false;
    }

    @Override
    public List<Tile> getWallTiles() {
        return Collections.emptyList();
    }
}
