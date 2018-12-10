package com.rm.fxmap.spring.map;

import com.rm.panzoomcanvas.FxCanvas;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author rmarquez
 */
public class MapFactory implements FactoryBean<FxCanvas> {
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public FxCanvas getObject() throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Class<?> getObjectType() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
}
