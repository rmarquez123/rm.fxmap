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
public @interface FxLineLayer {
  String mapId();

  /**
   *
   * @return
   */
  String name();

  /**
   *
   * @return
   */
  String selectedBeanId() default "";

  /**
   *
   * @return
   */
  String basecolorHex();

  /**
   *
   * @return
   */
  String strokecolorHex() default "";

  /**
   *
   * @return
   */
  int basewidth();

  /**
   *
   * @return
   */
  String selectedColorHex();

  /**
   *
   * @return
   */
  int selectedWidth();

  /**
   *
   * @return
   */
  String visibilityId() default "";
}
