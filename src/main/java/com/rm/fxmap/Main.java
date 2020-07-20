package com.rm.fxmap;

import com.rm.fxmap.basemap.BaseMap;
import com.rm.fxmap.basemap.BaseMapTileLayer;
import com.rm.fxmap.basemap.tiles.FileTileCache;
import com.rm.fxmap.basemap.tiles.TileCache;
import com.rm.fxmap.projections.SpatialProjection;
import com.rm.fxmap.projections.Wgs84Mercator;
import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.components.PositionBar;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.impl.points.ArrayPointsSource;
import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.projections.Projector;
import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author rmarquez
 */
public class Main extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    FxCanvas mapCanvas = this.createMap();
    Scene scene = new Scene(mapCanvas.getParent());
    this.addLayers(mapCanvas);
    stage.setScene(scene);
    stage.setWidth(500);
    stage.setHeight(500);
    stage.show();
  }

  private void addLayers(FxCanvas mapCanvas) {
    this.addBaseMapLayer(mapCanvas);
    this.addPointsLayer(mapCanvas);
  }

  private void addPointsLayer(FxCanvas mapCanvas) {
    FxPoint fxPoint = new FxPoint(-120.43, 37.36, new Wgs84Spheroid());
    PointMarker<String> marker = new PointMarker<>("Merced", fxPoint);
    ArrayPointsSource source = new ArrayPointsSource<>(marker);
    PointsLayer layer = new PointsLayer("aasf", new PointShapeSymbology(), source);
    layer.selectableProperty().setValue(true);
    layer.hoverableProperty().setValue(true);
    layer.hoveredMarkersProperty().addListener((evt)->{
      
    });
    
    layer.selectedMarkersProperty().addListener((obs, old, change)->{
      System.out.println(change);
    });
    mapCanvas.getContent().getLayers().add(layer);
  }
  
  /**
   * 
   * @param mapCanvas 
   */
  private void addBaseMapLayer(FxCanvas mapCanvas) {
    TileCache cache = new FileTileCache(new File("G:\\basemaps"));
    BaseMap basemap = BaseMap.ESRI_WORLD;
    BaseMapTileLayer layer = new BaseMapTileLayer(cache, basemap);
    mapCanvas.getContent().getLayers().add(layer);
  }

  /**
   *
   * @return
   */
  private FxCanvas createMap() {
    Content content = new Content();
    FxCanvas mapCanvas = new FxCanvas(content, new Projector(new Wgs84Mercator(), new SpatialProjection()));
    PositionBar positionBar = new PositionBar(mapCanvas);
    StackPane.setAlignment(positionBar, Pos.BOTTOM_RIGHT);
    StackPane root = new StackPane(mapCanvas, positionBar);
    root.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
      Platform.runLater(() -> {
        positionBar.toFront();
      });
    });
    StackPane.setAlignment(mapCanvas, Pos.CENTER);
    mapCanvas.widthProperty().bind(root.widthProperty());
    mapCanvas.heightProperty().bind(root.heightProperty());
    return mapCanvas;
  }

  /**
   * The main() method is ignored in correctly deployed JavaFX application.
   * main() serves only as fallback in case the application can not be launched
   * through deployment artifacts, e.g., in IDEs with limited FX support.
   * NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
