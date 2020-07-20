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
import com.rm.panzoomcanvas.impl.points.ColorValueSymbology;
import com.rm.panzoomcanvas.impl.points.Evaluator;
import com.rm.panzoomcanvas.impl.points.PointValueSymbology;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.projections.Projector;
import common.colormap.LinearRangeColorModel;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

/**
 *
 * @author Ricardo Marquez
 */
public class MainPointSymbologyWithData extends Application {

  private DoubleProperty parameterProperty;
  private Property<LinearRangeColorModel> colorModelProperty = new SimpleObjectProperty<>();

  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage stage) throws Exception {
    this.parameterProperty = new SimpleDoubleProperty(5d);
    Stop minStop = new Stop(0, Color.BLACK);
    Stop maxStop = new Stop(10, Color.WHITE);
    this.colorModelProperty.setValue(new LinearRangeColorModel(minStop, maxStop));

    FxCanvas mapCanvas = this.createMap();
    this.addLayers(mapCanvas);
    ListProperty<Layer> layers = mapCanvas.getContent().getLayers();
    Layer baseMapLayer = layers.getValue().stream()
      .filter((l) -> l instanceof BaseMapTileLayer).findAny().get();
    BaseMapToggle toggle = new BaseMapToggle((BaseMapTileLayer) baseMapLayer);
    toggle.addToMap(mapCanvas);

    ColorLegendMapTool colorLegend = new ColorLegendMapTool(this.colorModelProperty.getValue());
    this.colorModelProperty.bind(colorLegend.colorModelProperty());

    colorLegend.addToMap(mapCanvas);

    Parent p = mapCanvas.getParent();
    Slider slider = new Slider(0, 10.0, 0);
    this.parameterProperty.bind(slider.valueProperty());
    HBox controls = new HBox(slider);
    VBox vbox = new VBox(controls, p);
    VBox.setVgrow(p, Priority.ALWAYS);
    Scene scene = new Scene(vbox);
    stage.setScene(scene);
    stage.setWidth(500);
    stage.setHeight(500);
    stage.show();
  }

  private void addLayers(FxCanvas mapCanvas) {
    this.addBaseMapLayer(mapCanvas);
    this.addPointsLayer(mapCanvas);
  }

  /**
   *
   * @param mapCanvas
   */
  private void addPointsLayer(FxCanvas mapCanvas) {
    FxPoint fxPoint = new FxPoint(-120.43, 37.36, new Wgs84Spheroid());

    PointMarker<String> marker = new PointMarker<>("Merced", fxPoint);
    ArrayPointsSource source = new ArrayPointsSource<>(marker);
    PointValueSymbology<Double> symbology = new PointValueSymbology<>(0d);
    symbology.parameterProperty().bind(this.parameterProperty.asObject());
    Evaluator<Double> evaluator = (t) -> {
      return t.getValue();
    };

    ColorValueSymbology<Double> colorValueSymbology = new ColorValueSymbology<>(this.colorModelProperty.getValue(), evaluator);
    colorValueSymbology.colorModelProperty().bind(this.colorModelProperty);
    symbology.markerSymbology().setValue(colorValueSymbology);
    PointsLayer layer = new PointsLayer("aasf", symbology, source);
    layer.selectableProperty().setValue(true);
    layer.hoverableProperty().setValue(true);
    mapCanvas.getContent().getLayers().add(layer);
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
