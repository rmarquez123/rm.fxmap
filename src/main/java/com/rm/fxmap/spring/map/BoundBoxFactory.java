package com.rm.fxmap.spring.map;

import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.core.FxEnvelope;
import com.rm.panzoomcanvas.core.FxPoint;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author rmarquez
 */
public class BoundBoxFactory implements FactoryBean<FxEnvelope>, InitializingBean {

  private Double xmin;
  private Double xmax;
  private Double ymin;
  private Double ymax;

  public BoundBoxFactory() {
  }

  public void setXmin(Double xmin) {
    this.xmin = xmin;
  }

  public void setXmax(Double xmax) {
    this.xmax = xmax;
  }

  public void setYmin(Double ymin) {
    this.ymin = ymin;
  }

  public void setYmax(Double ymax) {
    this.ymax = ymax;
  }

  public Double getXmin() {
    return xmin;
  }

  public Double getXmax() {
    return xmax;
  }

  public Double getYmin() {
    return ymin;
  }

  public Double getYmax() {
    return ymax;
  }
  

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public FxEnvelope getObject() throws Exception {
    Wgs84Spheroid sr = new Wgs84Spheroid();
    FxPoint min = new FxPoint(this.xmin, this.ymin, sr);
    FxPoint max = new FxPoint(this.xmax, this.ymax, sr);
    FxEnvelope result = new FxEnvelope(min, max);
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Class<?> getObjectType() {
    return FxEnvelope.class;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.xmin == null) {
      throw new NullPointerException("xmin cannot be null");
    }
    if (this.xmax == null) {
      throw new NullPointerException("xmax cannot be null");
    }
    if (this.ymin == null) {
      throw new NullPointerException("ymin cannot be null");
    }
    if (this.ymax == null) {
      throw new NullPointerException("ymax cannot be null");
    }
  }

}
