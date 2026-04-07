package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HorizontalWallWrapMap extends DefaultSinglePlayerMap {

    private final List<Tile> wallTiles;

    public HorizontalWallWrapMap(int boardWidth, int boardHeight, int tileSize) {
        super(boardWidth, boardHeight, tileSize);
        this.wallTiles = createWallTiles();
    }

    @Override
    public int normalizeX(int x) {
        if (x < 0) {
            return getColumns() - 1;
        }
        if (x >= getColumns()) {
            return 0;
        }
        return x;
    }

    @Override
    public int normalizeY(int y) {
        if (y < 0) {
            return getRows() - 1;
        }
        if (y >= getRows()) {
            return 0;
        }
        return y;
    }

    @Override
    public boolean isOutOfBounds(Tile tile) {
        return false;
    }

    @Override
    public List<Tile> getWallTiles() {
        return Collections.unmodifiableList(wallTiles);
    }

    private List<Tile> createWallTiles() {
        List<Tile> walls = new ArrayList<>();
        int middleRow = getRows() / 2;

        for (int x = 0; x < getColumns(); x++) {
            walls.add(new Tile(x, middleRow));
        }
        return walls;
    }
}
