package com.rm.fxmap.annotations;

import com.rm.panzoomcanvas.Layer;

/**
 *
 * @author Ricardo Marquez
 */
public interface FxLayerBuilder {

  /**
   *
   * @param bean
   * @return
   */
  boolean isBeanValid(Object bean);

  /**
   *
   * @param bean
   * @return
   */
  String getRefMapId(Object bean);

  /**
   *
   * @param bean
   * @return
   */
  Layer createLayer(Object bean);

}
