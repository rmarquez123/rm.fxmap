
package com.gei.rm.fxmap.basemap.tiles;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rmarquez
 */
public class InMemoryTileCache implements TileCache {
  
  private Map<TileIndices, Tile> tiles = new HashMap<>(); 
  
  
  /**
   * 
   * @param key
   * @param tile 
   */
  @Override
  public void put(TileIndices key, Tile tile) {
    this.tiles.put(key, tile); 
  }

  /**
   * 
   * @param key
   * @return 
   */
  @Override
  public boolean containsKey(TileIndices key) {
    boolean result = tiles.containsKey(key);
    return result;
  }
  
  /**
   * 
   * @param key
   * @return 
   */
  @Override
  public Tile get(TileIndices key) {
    return tiles.get(key);
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
