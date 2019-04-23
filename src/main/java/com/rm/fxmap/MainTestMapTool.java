package com.rm.fxmap;

import com.rm.fxmap.basemap.BaseMapTileLayer;
import com.rm.fxmap.projections.SpatialProjection;
import com.rm.fxmap.projections.Wgs84Mercator;
import com.rm.fxmap.tools.BaseMapToggle;
import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.components.PositionBar;
import com.rm.panzoomcanvas.projections.Projector;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Ricardo Marquez
 */
public class MainTestMapTool extends Application {

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    FxCanvas mapCanvas = this.createMap();
    

    
    Scene scene = new Scene(mapCanvas.getParent());
    this.addLayers(mapCanvas);
    scene.getStylesheets().add("styles/fxmap.css"); 
    stage.setScene(scene);
    stage.setWidth(500);
    stage.setHeight(500);
    stage.show();
  }

  private void addLayers(FxCanvas mapCanvas) {
    BaseMapTileLayer baseMapTileLayer = new BaseMapTileLayer();
    mapCanvas.getContent().getLayers().add(baseMapTileLayer);
    MapTool toggle = new BaseMapToggle(baseMapTileLayer);
    toggle.addToMap(mapCanvas);
  }

  private FxCanvas createMap() {
    Content content = new Content();
    Projector projector = new Projector(new Wgs84Mercator(), new SpatialProjection());
    FxCanvas mapCanvas = new FxCanvas(content, projector);
    
    PositionBar positionBar = new PositionBar(mapCanvas);
    StackPane.setAlignment(positionBar, Pos.BOTTOM_RIGHT);
    mapCanvas.addTool(positionBar);
    
    
    StackPane root = new StackPane(mapCanvas);
    StackPane.setAlignment(mapCanvas, Pos.CENTER);
    
    mapCanvas.widthProperty().bind(root.widthProperty());
    mapCanvas.heightProperty().bind(root.heightProperty());
    return mapCanvas;
  }
}
