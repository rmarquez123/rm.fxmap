package com.gei.rm.fxmap.basemap.tiles;

/**
 *
 * @author rmarquez
 */
public interface TileCache {

  /**
   *
   * @param key
   * @return
   */
  boolean containsKey(TileIndices key);

  /**
   *
   * @param key
   * @return
   */
  Tile get(TileIndices key);

  /**
   *
   * @param key
   * @param tile
   */
  void put(TileIndices key, Tile tile);
  
}
