
package com.rm.fxmap.basemap;


import com.rm.fxmap.basemap.tiles.TileIndices;
import com.rm.fxmap.basemap.tiles.TileIndicesRange;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.layers.DrawArgs;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author rmarquez
 */
class DrawNewTilesTask implements Task {
  
  private final BaseMapTileLayer self;
  private final DrawArgs args;

  public DrawNewTilesTask(BaseMapTileLayer self, DrawArgs args) {
    this.self = self;
    this.args = args;
  }

  @Override
  public void execute() {
    TileIndicesRange tileIndicesRange = self.getTileIndicesRange(args);
    Canvas canvas = (Canvas) args.getLayerCanvas();
    GraphicsContext graphics = canvas.getGraphicsContext2D();
    tileIndicesRange.forEach((TileIndices tileIndex) -> {
      ScreenPoint screenPos = self.getScreenPosition(args, tileIndicesRange, tileIndex);
      BaseMapTileLayer.BASE_MAP baseMap = self.baseMapProperty().getValue();
      if (self.inMemoryCache.containsKey(baseMap, tileIndex)) {
        self.redrawCachedTileFromInMemory(tileIndex, screenPos, graphics);
      } else if (self.cache.containsKey(baseMap, tileIndex)) {
        self.redrawCachedTile(tileIndex, screenPos, graphics);
      } else {
        self.initializeTile(tileIndex, screenPos, graphics);
      }
    });
  }

  @Override
  public void cancel() {
  }
  
}
