package com.rm.fxmap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Marquez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FxMapTool {

  /**
   * The map id. An exception will be thrown if the map id does not exist.
   *
   * @return
   */
  String mapId();
}
