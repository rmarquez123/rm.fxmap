
package com.rm.fxmap.basemap.tiles;

/**
 *
 * @author rmarquez
 */
public class TileIndices {
  
  public final int level;
  public final int row;
  public final int col;
  
  public TileIndices(int level, int row, int col) {
    this.level = level;
    this.row = row;
    this.col = col;
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 67 * hash + this.level;
    hash = 67 * hash + this.row;
    hash = 67 * hash + this.col;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TileIndices other = (TileIndices) obj;
    if (this.level != other.level) {
      return false;
    }
    if (this.row != other.row) {
      return false;
    }
    if (this.col != other.col) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "{" + "level=" + level + ", row=" + row + ", col=" + col + '}';
  }
  
}
