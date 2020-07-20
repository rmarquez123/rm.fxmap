package com.rm.fxmap.annotations;

import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.impl.points.ArrayPointsSource;
import com.rm.panzoomcanvas.impl.points.PointShape;
import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.rm.panzoomcanvas.layers.points.Offset;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointSymbology;
import com.rm.panzoomcanvas.layers.points.PointsLabel;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.layers.points.PointsSource;
import common.bindings.RmBindings;
import java.util.Objects;
import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.scene.paint.Color;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
public class FxPointLayerBuilder implements FxLayerBuilder {

  private final ApplicationContext appContext;

  /**
   *
   * @param appContext
   */
  FxPointLayerBuilder(ApplicationContext appContext) {
    this.appContext = appContext;
  }

  /**
   *
   * @param bean
   * @return
   */
  @Override
  public boolean isBeanValid(Object bean) {
    FxPointLayer conf = bean.getClass().getDeclaredAnnotation(FxPointLayer.class);
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
    FxPointLayer conf = bean.getClass().getDeclaredAnnotation(FxPointLayer.class);
    String refMapId = conf.mapId();
    return refMapId;
  }

  /**
   *
   * @param bean
   * @param mapId
   */
  @Override
  public Layer createLayer(Object bean) {
    FxPointLayer conf = bean.getClass().getDeclaredAnnotation(FxPointLayer.class);
    String name = conf.name();
    PointSymbology symbology = this.getSymbology(conf);
    PointsSource<? extends Object> source = new ArrayPointsSource<>(new Wgs84Spheroid());
    PointsLayer<? extends Object> result = new PointsLayer<>(name, symbology, source);
    result.selectableProperty().set(!conf.selectedColorHex().isEmpty());
    result.hoverableProperty().set(!conf.selectedColorHex().isEmpty());
    this.setLabel(conf, result);
    this.bindVisibility(conf, result);
    this.bindSelection(conf, result);
    return result;
  }

  /**
   *
   * @param conf
   * @return
   */
  private PointSymbology getSymbology(FxPointLayer conf) {
    PointSymbology result;
    if (conf.symbologyId().isEmpty()) {
      PointShapeSymbology symbology = new PointShapeSymbology();
      Color baseColor = Color.web(conf.basecolorHex());
      Color strokeColor = conf.strokecolorHex().isEmpty() ? baseColor : Color.web(conf.strokecolorHex());
      symbology.fillColorProperty().setValue(baseColor);
      symbology.pointShapeProperty().setValue(PointShape.CIRCLE);
      symbology.strokeColorProperty().setValue(strokeColor);
      if (!conf.selectedColorHex().isEmpty()) {
        Color selectColor = Color.web(conf.selectedColorHex());
        symbology.getSelected().fillColorProperty().setValue(selectColor);
      }
      result = symbology;
    } else {
      result = this.appContext.getBean(conf.symbologyId(), PointSymbology.class);
      
    }
    return result;
  }

  /**
   *
   * @param conf
   * @param layer
   * @throws BeansException
   */
  private void bindVisibility(FxPointLayer conf, PointsLayer<? extends Object> layer) throws BeansException {
    String visibilityId = conf.visibilityId();
    if (!visibilityId.isEmpty()) {
      Property<Boolean> visibleProperty = (Property<Boolean>) this.appContext
        .getBean(visibilityId);
      RmBindings.bind1To2(layer.visibleProperty(), visibleProperty);
    }
  }

  /**
   *
   * @param conf
   * @param layer
   */
  private void bindSelection(FxPointLayer conf, PointsLayer<?> layer) {
    String selectedBeanId = conf.selectedBeanId();
    if (!selectedBeanId.isEmpty()) {
      Object bean = this.appContext.getBean(selectedBeanId);
      Objects.requireNonNull(bean, String.format("Bean '%s' does not exist.", selectedBeanId));
      if (bean instanceof Property) {
        Property<?> property = (Property<?>) bean;
        property.addListener((obs, old, change) -> {
          layer.selectByUserObject(change);
        });

        layer.selectedMarkersProperty().addListener((obs, old, change) -> {
          PointMarker<?> newValue = change.stream().findFirst().orElse(null);
          ((Property) bean).setValue(newValue == null ? null : newValue.getUserObject());
        });

        layer.getSource().pointMarkersProperty().addListener(new ListChangeListener<PointMarker<?>>() {
          @Override
          public void onChanged(ListChangeListener.Change<? extends PointMarker<?>> c) {
            layer.selectByUserObject(property.getValue());
          }
        });

      } else {
        throw new IllegalStateException(
          String.format("bean '%s' is not a property instance", selectedBeanId));
      }
    }
  }

  /**
   *
   * @param conf
   * @param layer
   */
  private void setLabel(FxPointLayer conf, PointsLayer<? extends Object> layer) {

    FxPointLayer.Label labelconf = conf.label();
    if (!labelconf.ignore()) {
      Color foregroundColor = Color.web(labelconf.foregroundColorHex());
      Color backgroundColor = Color.web(labelconf.backgroundColorHex());
      Offset offset = new Offset.OffsetBuilder()
        .east(labelconf.east())
        .west(labelconf.west())
        .north(labelconf.north())
        .south(labelconf.south())
        .build();
      Function<? extends Object, String> textConverter // 
        = (Function<? extends Object, String>) this.appContext.getBean(labelconf.textconvertId());
      PointsLabel label = new PointsLabel(offset, textConverter, foregroundColor, backgroundColor);
      layer.labelProperty().setValue(label);
    }
  }
}
