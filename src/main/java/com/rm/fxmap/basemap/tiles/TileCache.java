package com.rm.fxmap.basemap.tiles;

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
  boolean containsKey(BaseMapTileLayer.BASE_MAP baseMap, TileIndices key);

  /**
   *
   * @param key
   * @return
   */
  Tile get(BaseMapTileLayer.BASE_MAP baseMap, TileIndices key);

  /**
   *
   * @param key
   * @param tile
   */
  void put(BaseMapTileLayer.BASE_MAP baseMap, TileIndices key, Tile tile);
  
}
