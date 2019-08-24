package com.rm.fxmap;

import com.rm.fxmap.basemap.BaseMapTileLayer;
import com.rm.fxmap.projections.SpatialProjection;
import com.rm.fxmap.projections.Wgs84Mercator;
import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.fxmap.tools.BaseMapToggle;
import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.components.PositionBar;
import com.rm.panzoomcanvas.projections.Projector;
import com.rm.panzoomcanvas.tools.PointSelectOnClick;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Ricardo Marquez
 */
public class MainTestPointClick extends Application {

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

    VBox root = new VBox();

    HBox buttons = new HBox();
    root.getChildren().add(buttons);
    Button addBtn = new Button("Add");
    buttons.getChildren().add(addBtn);
    PointSelectOnClick pointselect = new PointSelectOnClick(mapCanvas, new Wgs84Spheroid());
    addBtn.setOnAction((evt) -> {
      pointselect.activate(!pointselect.isActivated());
    });
    pointselect.addEventListener((p)->{
      System.out.println("p  = " + p);
      pointselect.activate(false);
    });
    
    Parent parent = mapCanvas.getParent();
    root.getChildren().add(parent);
    VBox.setVgrow(parent, Priority.ALWAYS);

    Scene scene = new Scene(root);
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
