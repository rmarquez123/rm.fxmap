package com.gei.rm.fxmap.basemap;

import com.gei.rm.fxmap.basemap.tiles.InMemoryTileCache;
import com.gei.rm.fxmap.basemap.tiles.Tile;
import com.gei.rm.fxmap.basemap.tiles.TileCache;
import com.gei.rm.fxmap.basemap.tiles.TileIndices;
import com.gei.rm.fxmap.basemap.tiles.TileIndicesRange;
import com.gei.rm.fxmap.basemap.tiles.WebServiceTile;
import com.gei.rm.fxmap.projections.Wgs84Mercator;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.ParamsIntersects;
import com.rm.panzoomcanvas.core.FxEnvelope;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.rm.panzoomcanvas.core.VirtualEnvelope;
import com.rm.panzoomcanvas.layers.BaseLayer;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.projections.Projector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author rmarquez
 */
public class BaseMapTileLayer extends BaseLayer {

  final InMemoryTileCache inMemoryCache = new InMemoryTileCache();
  final TileCache cache;
  final String baseUrl;
  private final DelayExecutor drawNewTilesExecutor = new DelayExecutor(200);
  private final DelayExecutor temporaryDrawExecutor = new DelayExecutor(0);
  DrawArgs lastDrawArgs = null;

  public BaseMapTileLayer() {
    this(new InMemoryTileCache());
  }

  public BaseMapTileLayer(TileCache cache) {
    this(cache, "https://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer/tile/%d/%d/%d");
  }

  public BaseMapTileLayer(TileCache cache, String baseUrl) {
    super("Base", (ParamsIntersects args) -> true);
    this.baseUrl = baseUrl;
    this.cache = cache;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected ScreenEnvelope onGetScreenEnvelope(FxCanvas canvas) {
    VirtualEnvelope virtualEnv = canvas.virtualEnvelopeProperty().getValue();
    ScreenEnvelope screenVal = canvas.screenEnvelopeProperty().getValue();
    ScreenEnvelope result = canvas.getProjector().projectVirtualToScreen(virtualEnv, screenVal);
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected Node createLayerCanvas(double width, double height) {
    Canvas result = new Canvas(width, height);
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected void onDraw(DrawArgs args) {
    final BaseMapTileLayer self = this;
    this.drawNewTilesExecutor.setTask(new DrawNewTilesTask(self, args));
    this.temporaryDrawExecutor.setTask(new DrawTemporaryTilesTask(self, args));
    this.lastDrawArgs = args;
  }

  /**
   *
   * @param args
   * @return
   */
  TileIndicesRange getTileIndicesRange(DrawArgs args) {
    Projector projector = args.getCanvas().getProjector();
    SpatialRef destSr = new Wgs84Mercator();
    VirtualEnvelope vEnv = args.getCanvas().virtualEnvelopeProperty().getValue();
    FxEnvelope geoEnv = projector.projectVirtualToGeo(vEnv, destSr);
    double heightScreen = args.getAreaHeight();
    TileIndicesRange tileIndicesRange = TileIndicesRange.createTileIndicesRange(geoEnv, heightScreen);
    return tileIndicesRange;
  }

  /**
   *
   * @param tileIndex
   * @param screenPos
   * @param graphicsContext2D
   */
  void redrawCachedTileFromInMemory(TileIndices tileIndex, ScreenPoint screenPos, GraphicsContext graphicsContext2D) {
    Tile tile = this.inMemoryCache.get(tileIndex);
    Platform.runLater(() -> {
      tile.addToGraphics(graphicsContext2D, screenPos);
    });
  }

  /**
   *
   * @param tileIndex
   * @param screenPos
   * @param graphicsContext2D
   */
  void redrawCachedTile(TileIndices tileIndex, ScreenPoint screenPos, GraphicsContext graphicsContext2D) {
    Tile tile = this.cache.get(tileIndex);
    Platform.runLater(() -> {
      tile.addToGraphics(graphicsContext2D, screenPos);
    });
  }

  /**
   *
   * @param tileIndex
   * @param screenPos
   * @param graphicsContext2D
   */
  void initializeTile(TileIndices tileIndex, ScreenPoint screenPos, GraphicsContext graphicsContext2D) {
    String url = this.getUrl(tileIndex);
    try {
      WebServiceTile tile = new WebServiceTile(url);
      tile.getImage().addListener((obs, oldVal, newVal) -> {
        tile.addToGraphics(graphicsContext2D, screenPos);
      });
      tile.loadImage();
      this.cache.put(tileIndex, tile);
      this.inMemoryCache.put(tileIndex, tile);
    } catch (Exception ex) {
      Logger.getLogger(BaseMapTileLayer.class.getName())
              .log(Level.SEVERE, "Error getting tile.", ex);
    }
  }

  String getUrl(TileIndices tileIndex) {
    String result = String.format(baseUrl,
            tileIndex.level, tileIndex.row, tileIndex.col);
    return result;
  }

  /**
   *
   * @param args
   * @param tileIndicesRange
   * @param tileIndex
   * @return
   */
  ScreenPoint getScreenPosition(DrawArgs args, TileIndicesRange tileIndicesRange, TileIndices tileIndex) {
    ScreenPoint screenPos;
    Projector projector = args.getCanvas().getProjector();
    if (tileIndicesRange.scalex > 0.0) {
      ScreenEnvelope screenEnv = args.getCanvas().screenEnvelopeProperty().getValue();
      screenPos = tileIndicesRange.computScreenPos(projector, screenEnv, tileIndex);
    } else {
      screenPos = new ScreenPoint(args.getAreaX(), args.getAreaY());
    }
    return screenPos;
  }

}
