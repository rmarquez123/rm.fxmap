package com.rm.fxmap.annotations;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.Layer;
import com.rm.springjavafx.FxmlInitializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
public class FxMapLayerBuilder {

  private final FxmlInitializer fxmlInitializer;
  private final ApplicationContext applicationContext;
  private final List<FxLayerBuilder> builders;

  /**
   *
   * @param fxmlInitializer
   * @param applicationContext
   */
  public FxMapLayerBuilder(FxmlInitializer fxmlInitializer, ApplicationContext applicationContext) {
    this.fxmlInitializer = fxmlInitializer;
    this.applicationContext = applicationContext;
    this.builders = new ArrayList<>(); 
    this.builders.add(new FxPointLayerBuilder(this.applicationContext));
    this.builders.add(new FxLineLayerBuilder(this.applicationContext));
  }

  /**
   *
   * @param canvas
   */
  void createLayers(String mapId, FxCanvas canvas, Map<String, Object> layerBeans) {
    for (Map.Entry<String, Object> entry : layerBeans.entrySet()) {
      String beanId = entry.getKey();
      Object bean = entry.getValue();
      if (!(bean instanceof FxMapLayer)) {
        throw new IllegalStateException(
          String.format("bean '%s' is not an instance of '%s'", beanId, FxMapLayer.class));
      }
      try {
        this.createLayer(bean, mapId, canvas);
      } catch (Exception ex) {
        throw new RuntimeException(
          String.format("Error on creating layer : '%s'", beanId),
          ex);
      }
    }
  }

  /**
   *
   * @param bean
   * @param mapId
   * @param canvas
   * @throws BeansException
   */
  private void createLayer(Object bean, String mapId, FxCanvas canvas) {
    FxLayerBuilder b = this.getLayerBuilder(bean);
    if (b.getRefMapId(bean).equals(mapId)) {
      Layer layer = b.createLayer(bean);
      canvas.getContent().getLayers().add(layer);
      ((FxMapLayer) bean).setLayer(layer);
    }
  }
  
  /**
   * 
   * @param bean 
   */
  private FxLayerBuilder getLayerBuilder(Object bean) {
    FxLayerBuilder result = this.builders.stream()
      .filter((b)->b.isBeanValid(bean))
      .findFirst()
      .orElse(null); 
    return result;
  }
}
