package com.rm.fxmap;

import com.rm.fxmap.basemap.BaseMapTileLayer;
import com.rm.fxmap.projections.SpatialProjection;
import com.rm.fxmap.projections.Wgs84Mercator;
import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.fxmap.tools.BaseMapToggle;
import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.components.PositionBar;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.impl.points.ArrayPointsSource;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointSymbology;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.layers.vectors.VectorDisplayInfo;
import com.rm.panzoomcanvas.layers.vectors.VectorPointSymbology;
import com.rm.panzoomcanvas.projections.Projector;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.ListProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Ricardo Marquez
 */
public class MainTestWindFieldLayer extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    FxCanvas mapCanvas = this.createMap();
    this.addLayers(mapCanvas);
    ListProperty<Layer> layers = mapCanvas.getContent().getLayers();
    Layer baseMapLayer = layers.getValue().stream()
      .filter((l) -> l instanceof BaseMapTileLayer).findAny().get();
    BaseMapToggle toggle = new BaseMapToggle((BaseMapTileLayer) baseMapLayer);
    toggle.addToMap(mapCanvas);

    Parent p = mapCanvas.getParent();
    VBox vbox = new VBox(p);
    VBox.setVgrow(p, Priority.ALWAYS);
    Scene scene = new Scene(vbox);
    stage.setScene(scene);
    stage.setWidth(500);
    stage.setHeight(500);
    stage.show();
  }

  /**
   *
   * @param mapCanvas
   */
  private void addLayers(FxCanvas mapCanvas) {
    this.addBaseMapLayer(mapCanvas);
    this.addPointsLayer(mapCanvas);
  }

  /**
   *
   * @param mapCanvas
   */
  private void addBaseMapLayer(FxCanvas mapCanvas) {
    BaseMapTileLayer baseMapTileLayer = new BaseMapTileLayer();
    mapCanvas.getContent().getLayers().add(baseMapTileLayer);
  }

  /**
   *
   * @param mapCanvas
   */
  private void addPointsLayer(FxCanvas mapCanvas) {
    FxPoint fxPoint = new FxPoint(-120.43, 37.36, new Wgs84Spheroid());
    PointMarker<String> marker = new PointMarker<>("Merced", fxPoint);
    ArrayPointsSource source = new ArrayPointsSource<>(marker);
    PointSymbology symbology = new VectorPointSymbology.Builder()
      .setDispalySupplier(this::displaySupplier)
      .build(); 
    PointsLayer layer = new PointsLayer("aasf", symbology, source);
    layer.selectableProperty().setValue(true);
    layer.hoverableProperty().setValue(true);
    layer.selectedMarkersProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
      if (newValue != null) {
        System.out.println(newValue);
        System.out.println(displaySupplier(newValue));
        
      }
    });
    mapCanvas.getContent().getLayers().add(layer);
  }
    
  
  /**
   * 
   * @param a
   * @return 
   */
  private VectorDisplayInfo displaySupplier(Object a) {
    int u = -2; 
    int v = 4; 
    int scale = 3000; 
    Wgs84Mercator sr = new Wgs84Mercator(); 
    VectorDisplayInfo vectorDisplayInfo = new VectorDisplayInfo(u, v, scale, sr); 
    return vectorDisplayInfo; 
  }

  /**
   *
   * @return
   */
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
