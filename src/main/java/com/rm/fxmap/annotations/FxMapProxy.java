package com.rm.fxmap.annotations;

import com.rm.panzoomcanvas.FxCanvas;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Ricardo Marquez
 */
public class FxMapProxy implements FactoryBean<FxCanvas> {

  private FxCanvas mapcanvas;
    
  public FxMapProxy() {
    
  }
  
  @Required
  public void setMapcanvas(FxCanvas mapcanvas) {
    this.mapcanvas = mapcanvas;
  }
  
  /**
   * 
   * @return
   * @throws Exception 
   */
  @Override
  public FxCanvas getObject() throws Exception {
    return this.mapcanvas;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Class<?> getObjectType() {
    return FxCanvas.class;
  }
  
  
  
}
