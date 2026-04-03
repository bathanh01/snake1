package model;

import java.util.List;

public interface SinglePlayerMap {

    int getColumns();

    int getRows();

    int normalizeX(int x);

    int normalizeY(int y);

    boolean isOutOfBounds(Tile tile);

    boolean hitsWall(Tile tile);

    List<Tile> getWallTiles();
}
