package com.rm.fxmap.annotations;

import com.rm.fxmap.basemap.BaseMapTileLayer;
import com.rm.fxmap.projections.SpatialProjection;
import com.rm.fxmap.projections.Wgs84Mercator;
import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.components.PositionBar;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.projections.Projector;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.annotations.FxController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.mutable.MutableObject;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
public class FxMapBuilder {

  private final FxmlInitializer fxmlInitializer;
  private final ApplicationContext applicationContext;

  public FxMapBuilder(FxmlInitializer fxmlInitializer, ApplicationContext applicationContext) {
    this.fxmlInitializer = fxmlInitializer;
    this.applicationContext = applicationContext;
  }

  /**
   *
   * @param bean
   */
  public FxCanvas createMap(Object bean) {
    FxMap mapconf = bean.getClass().getDeclaredAnnotation(FxMap.class);
    FxController controllerconf = bean.getClass().getDeclaredAnnotation(FxController.class);
    String fxml = controllerconf.fxml();
    String nodeId = mapconf.nodeId();
    Node pane;
    try {
      pane = this.fxmlInitializer.getNode(fxml, nodeId);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }

    FxCanvas mapCanvas = this.createMap((StackPane) pane);
    this.addBaseMapLayer(mapCanvas);
    return mapCanvas;
  }

  /**
   *
   * @return
   */
  private FxCanvas createMap(StackPane root) {
    Content content = new Content();
    Projector projector = new Projector(new Wgs84Mercator(), new SpatialProjection());
    FxCanvas mapCanvas = new FxCanvas(content, projector);
    PositionBar positionBar = new PositionBar(mapCanvas);
    StackPane.setAlignment(positionBar, Pos.BOTTOM_RIGHT);
    mapCanvas.addTool(positionBar);
    root.getChildren().addAll(mapCanvas);
    
    StackPane.setAlignment(mapCanvas, Pos.CENTER);
    mapCanvas.widthProperty().bind(root.widthProperty());
    mapCanvas.heightProperty().bind(root.heightProperty());
    MutableObject<Boolean> initialized = new MutableObject<>(false);
    this.fxmlInitializer.addListener((i) -> {
      mapCanvas.screenEnvelopeProperty().addListener((obs, old, change) -> {
        if (mapCanvas.screenEnvelopeProperty().getValue().getWidth() > 0) {
          if (!initialized.getValue()) {
            initialized.setValue(true);
            Platform.runLater(() -> {
              FxPoint center = new FxPoint(-96.0, 40.0, new Wgs84Spheroid());
              mapCanvas.zoomToVirtualPoint(4, center);
              mapCanvas.getContent().redraw();
            });
          }
        }
      });
    });
    return mapCanvas;
  }

  /**
   *
   * @param mapCanvas
   */
  private void addBaseMapLayer(FxCanvas mapCanvas) {
    mapCanvas.getContent().getLayers().add(new BaseMapTileLayer());
  }
}
