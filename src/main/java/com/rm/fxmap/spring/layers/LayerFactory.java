package com.rm.fxmap.spring.layers;

import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.impl.points.ArrayPointsSource;
import com.rm.panzoomcanvas.impl.points.PointShapeSymbology;
import com.rm.panzoomcanvas.impl.polygon.BasePolygonSource;
import com.rm.panzoomcanvas.impl.polygon.DefaultPolygonSymbology;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.layers.points.PointSymbology;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import com.rm.panzoomcanvas.layers.points.PointsSource;
import com.rm.panzoomcanvas.layers.polygon.PolygonLayer;
import com.rm.panzoomcanvas.layers.polygon.PolygonMarker;
import com.rm.panzoomcanvas.layers.polygon.PolygonPoints;
import com.rm.panzoomcanvas.layers.polygon.PolygonSource;
import com.rm.panzoomcanvas.layers.polygon.PolygonSymbology;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author rmarquez
 */
public class LayerFactory implements FactoryBean<Layer>, InitializingBean {

  private String type;
  private String name;

  /**
   *
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   *
   * @param type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Layer getObject() throws Exception {
    Layer result;
    switch (this.type) {
      case "point":
        PointSymbology symbology = new PointShapeSymbology();
        PointsSource source = new ArrayPointsSource(new Wgs84Spheroid());
        result = new PointsLayer<>(this.name, symbology, source);
        break;
      case "polygon":
        PolygonSymbology polygonSymbology = new DefaultPolygonSymbology();
        PolygonSource polygonSource = new BasePolygonSource(new Wgs84Spheroid()) {
          @Override
          public PolygonMarker getScreenPoints(DrawArgs args) {
            double[] x = new double[]{};
            double[] y = new double[]{};
            PolygonPoints points = new PolygonPoints(x, y, 0);
            PolygonMarker marker = new PolygonMarker(args, points);
            return marker;
          }
        };
        result = new PolygonLayer(this.name, polygonSymbology, polygonSource);
        break;
      default:
        throw new IllegalStateException();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Class<?> getObjectType() {
    return Layer.class;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.type == null) {
      throw new NullPointerException("Type cannot be null");
    }
  }

}
