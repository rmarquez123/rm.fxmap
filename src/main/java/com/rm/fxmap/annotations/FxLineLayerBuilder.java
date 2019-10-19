package com.rm.fxmap.annotations;

import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.layers.polyline.DefaultPolyLineSource;
import com.rm.panzoomcanvas.layers.polyline.PolyLineLayer;
import com.rm.panzoomcanvas.layers.polyline.PolyLineMarker;
import com.rm.panzoomcanvas.layers.polyline.PolyLineSource;
import com.rm.panzoomcanvas.layers.polyline.PolyLineSymbology;
import common.bindings.RmBindings;
import java.util.Objects;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.scene.paint.Color;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
public class FxLineLayerBuilder implements FxLayerBuilder {

  private final ApplicationContext appContext;

  public FxLineLayerBuilder(ApplicationContext applicationContext) {
    this.appContext = applicationContext;
  }

  @Override
  public boolean isBeanValid(Object bean) {
    FxLineLayer conf = bean.getClass().getDeclaredAnnotation(FxLineLayer.class);
    boolean result = conf != null;
    return result;
  }

  /**
   *
   * @param bean
   * @return
   */
  @Override
  public String getRefMapId(Object bean) {
    FxLineLayer conf = bean.getClass().getDeclaredAnnotation(FxLineLayer.class);
    String refMapId = conf.mapId();
    return refMapId;
  }

  /**
   *
   * @param bean
   * @return
   */
  @Override
  public Layer createLayer(Object bean) {
    FxLineLayer conf = bean.getClass().getDeclaredAnnotation(FxLineLayer.class);
    PolyLineSource source = new DefaultPolyLineSource(new Wgs84Spheroid());
    PolyLineSymbology symbology = new PolyLineSymbology();
    symbology.colorProperty().setValue(Color.web(conf.basecolorHex()));
    symbology.widthProperty().setValue((double) conf.basewidth());
    PolyLineLayer result = new PolyLineLayer(conf.name(), source, symbology);
    result.selectableProperty().set(true);
    result.hoverableProperty().set(true);
    if (!conf.visibilityId().isEmpty()) {
      Property<Boolean> property = (Property<Boolean>) this.appContext //
        .getBean(conf.visibilityId());
      RmBindings.bind1To2(result.visibleProperty(), property);
    }

    bindSelection(conf, result);
    return result;
  }

  /**
   *
   * @param conf
   * @param result
   * @throws BeansException
   */
  private void bindSelection(FxLineLayer conf, PolyLineLayer<?> layer) {
    String selectedBeanId = conf.selectedBeanId();
    if (!selectedBeanId.isEmpty()) {
      Object bean = this.appContext.getBean(selectedBeanId);
      Objects.requireNonNull(bean, String.format("Bean '%s' does not exist.", selectedBeanId));
      if (bean instanceof Property) {
        Property<?> property = (Property<?>) bean;
        property.addListener((obs, old, change) -> {
          layer.selectByUserObject(change);
        });
        layer.selectedProperty().addListener((obs, old, change) -> {
          PolyLineMarker<?> newValue = change.stream().findFirst().orElse(null);
          Object userObj = newValue == null ? null : newValue.getUserObject();
          ((Property) bean).setValue(userObj);
        });        
        layer.source().markers().addListener((ListChangeListener.Change c) -> {
          layer.selectByUserObject(property.getValue());
        });
      } else {
        throw new IllegalStateException(
          String.format("bean '%s' is not a property instance", selectedBeanId));
      }
    }
  }

}
