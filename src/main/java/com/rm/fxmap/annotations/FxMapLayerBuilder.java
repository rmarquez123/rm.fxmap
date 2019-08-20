package com.rm.fxmap.annotations;

import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.impl.points.ArrayPointsSource;
import com.rm.panzoomcanvas.impl.points.PointShape;
import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.layers.points.PointsSource;
import com.rm.springjavafx.FxmlInitializer;
import java.util.Map;
import javafx.scene.paint.Color;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
public class FxMapLayerBuilder {

  private final FxmlInitializer fxmlInitializer;

  private final ApplicationContext applicationContext;
  
  /**
   * 
   * @param fxmlInitializer
   * @param applicationContext 
   */
  public FxMapLayerBuilder(FxmlInitializer fxmlInitializer, ApplicationContext applicationContext) {
    this.fxmlInitializer = fxmlInitializer;
    this.applicationContext = applicationContext;
  }

  /**
   *
   * @param canvas
   */
  public void createLayers(String mapId, FxCanvas canvas, Map<String, Object> pointLayerBeans) {
    for (Map.Entry<String, Object> entry : pointLayerBeans.entrySet()) {
      String beanId = entry.getKey();
      Object bean = entry.getValue();
      if (!(bean instanceof AbstractFxPointLayer)) {
        throw new IllegalStateException(
          String.format("bean '%s' is not an instance of '%s'", beanId, AbstractFxPointLayer.class));
      }
      FxPointLayer conf = bean.getClass().getDeclaredAnnotation(FxPointLayer.class);
      String refMapId = conf.mapId();
      if (mapId.equals(refMapId)) {
        String name = conf.name();
        PointShapeSymbology symbology = new PointShapeSymbology();
        Color baseColor = Color.web(conf.basecolorHex());
        symbology.fillColorProperty().setValue(baseColor);
        symbology.pointShapeProperty().setValue(PointShape.CIRCLE);
        Color selectColor = Color.web(conf.selectedColorHex());
        symbology.getSelected().fillColorProperty().setValue(selectColor);
        
        PointsSource<? extends Object> source = new ArrayPointsSource<>(new Wgs84Spheroid());
        PointsLayer<? extends Object> layer = new PointsLayer<>(name, symbology, source);
        layer.selectableProperty().set(true);
        layer.hoverableProperty().set(true);
        ((AbstractFxPointLayer) bean).setPointsLayer(layer);
        canvas.getContent().getLayers().add(layer);
      }
    }
  }

}
