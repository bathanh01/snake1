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
    public List<Tile> getWallTiles() {
        return Collections.emptyList();
    }
}
