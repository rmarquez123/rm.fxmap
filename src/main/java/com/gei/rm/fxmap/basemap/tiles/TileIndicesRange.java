package com.gei.rm.fxmap.basemap.tiles;

import com.rm.panzoomcanvas.core.FxEnvelope;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.projections.Projector;
import java.util.function.Consumer;

/**
 *
 * @author rmarquez
 */
public class TileIndicesRange {

  private final int nxMin;
  private final int nxMax;
  private final int nyMin;
  private final int nyMax;
  private final double yNmax;
  private final double xNmax;
  public final double scalex;
  public final double scaley;
  private final int level;
  private final SpatialRef destSr;

  /**
   *
   * @param geoEnv
   * @param heightScreen
   */
  private TileIndicesRange(FxEnvelope geoEnv, double heightScreen) {
    this.destSr = geoEnv.getSr();
    double heightGeo = geoEnv.getHeight();
    double heightFull = geoEnv.getSr().getHeight();
    double levelDouble = Math.log(heightFull / heightGeo * heightScreen / 256.0) / Math.log(2.0);
    this.level = (int) Math.round(levelDouble);
    this.xNmax = Math.pow(2, level) - 1;
    this.yNmax = Math.pow(2, level) - 1;
    this.scalex = (xNmax + 1) / destSr.getWidth();
    this.scaley = (yNmax + 1) / destSr.getHeight();
    this.nxMin = (int) Math.floor(scalex * (geoEnv.getMin().getX() - destSr.getMin().getX()));
    this.nxMax = (int) Math.ceil(scalex * (geoEnv.getMax().getX() - destSr.getMin().getX()));
    this.nyMin = (int) Math.floor((scaley * (destSr.getMax().getY() - geoEnv.getMax().getY())));
    this.nyMax = (int) Math.ceil((scaley * (destSr.getMax().getY() - geoEnv.getMin().getY())));
  }

  /**
   *
   * @param consumer
   */
  public void forEach(Consumer<TileIndices> consumer) {
    int numTiles = (nyMax - nyMin)*(nxMax - nxMin) + 1;
    if (numTiles > 40) {
      System.out.println("skipping large number of tiles : "  + numTiles); 
      return;
    }
    for (int row = nyMin; row <= nyMax; row++) {
      for (int col = nxMin; col <= nxMax; col++) {
        if ((0 <= row && row <= yNmax) && (0 <= col && col <= xNmax)) {
          consumer.accept(new TileIndices(this.level, row, col));
        }
      }
    }

  }

  /**
   *
   * @param geoEnv
   * @param height
   * @param level
   * @return
   */
  public static TileIndicesRange createTileIndicesRange(
          FxEnvelope geoEnv, double heightScreen) {
    TileIndicesRange result = new TileIndicesRange(geoEnv, heightScreen);
    return result;
  }

  /**
   *
   * @param tileIndex
   * @return
   */
  public ScreenPoint computScreenPos(Projector projector, ScreenEnvelope screenEnv, TileIndices tileIndex) {
    double xMin = destSr.getMin().getX() + (tileIndex.col) / scalex;
    double yMin = destSr.getMax().getY() - tileIndex.row / scaley;
    ScreenPoint screenPos = projector.projectGeoToScreen(new FxPoint(xMin, yMin, destSr), screenEnv);
    return screenPos;
  }

  @Override
  public String toString() {
    return "TileIndicesRange{" + "nxMin=" + nxMin + ", nxMax=" + nxMax + ", nyMin=" + nyMin + ", nyMax=" + nyMax + ", yNmax=" + yNmax + ", xNmax=" + xNmax + ", scalex=" + scalex + ", scaley=" + scaley + ", level=" + level + '}';
  }
  
}
