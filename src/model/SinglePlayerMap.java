package model;

import java.util.List;

public interface SinglePlayerMap {

    int getColumns();

    int getRows();

    List<Tile> getWallTiles();

    default int normalizeX(int x) {
        return x;
    }

    default int normalizeY(int y) {
        return y;
    }

    default boolean isOutOfBounds(Tile tile) {
        return tile.getX() < 0
                || tile.getY() < 0
                || tile.getX() >= getColumns()
                || tile.getY() >= getRows();
    }

    default boolean hitsWall(Tile tile) {
        for (Tile wallTile : getWallTiles()) {
            if (wallTile.getX() == tile.getX() && wallTile.getY() == tile.getY()) {
                return true;
            }
        }
        return false;
    }

    default Tile getInitialSnakeHead() {
        return new Tile(5, 5);
    }

    default boolean blocksSpawn(Tile tile) {
        return isOutOfBounds(tile) || hitsWall(tile);
    }
    int normalizeX(int x);

    int normalizeY(int y);

    boolean isOutOfBounds(Tile tile);

    boolean hitsWall(Tile tile);

    List<Tile> getWallTiles();
}
