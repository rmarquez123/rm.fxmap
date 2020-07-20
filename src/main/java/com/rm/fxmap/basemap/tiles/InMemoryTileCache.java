package com.rm.fxmap.basemap.tiles;

import com.rm.fxmap.basemap.BaseMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rmarquez
 */
public class InMemoryTileCache implements TileCache {

  private final Map<BaseMap, Map<TileIndices, Tile>> tiles;

  /**
   *
   */
  public InMemoryTileCache() {
    this.tiles = new HashMap<>();
  }

  /**
   *
   * @param key
   * @param tile
   */
  @Override
  public void put(BaseMap baseMap, TileIndices key, Tile tile) {
    if (tile.getBaseMap() == baseMap) {
      if (!this.tiles.containsKey(baseMap)) {
        this.tiles.put(baseMap, new HashMap<>());
      }
      this.tiles.get(baseMap).put(key, tile);
    }
  }

  /**
   *
   * @param key
   * @return
   */
  @Override
  public boolean containsKey(BaseMap baseMap, TileIndices key) {
    Map<TileIndices, Tile> baseMapTiles = tiles.get(baseMap);
    boolean result;
    if (baseMapTiles == null) {
      result = false;
    } else {
      result = baseMapTiles.containsKey(key);
    }
    return result;
  }

  /**
   *
   * @param key
   * @return
   */
  @Override
  public Tile get(BaseMap baseMap, TileIndices key) {
    Map<TileIndices, Tile> baseMapTiles = tiles.get(baseMap);
    Tile result = (baseMapTiles == null) ? null : baseMapTiles.get(key);
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "{" + "tiles=" + tiles.size() + '}';
  }

}
