package com.rm.fxmap.basemap.tiles;

import com.rm.fxmap.basemap.BaseMap;
import com.rm.fxmap.basemap.BaseMapTileLayer;

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
  boolean containsKey(BaseMap baseMap, TileIndices key);

  /**
   *
   * @param key
   * @return
   */
  Tile get(BaseMap baseMap, TileIndices key);

  /**
   *
   * @param key
   * @param tile
   */
  void put(BaseMap baseMap, TileIndices key, Tile tile);
  
}
