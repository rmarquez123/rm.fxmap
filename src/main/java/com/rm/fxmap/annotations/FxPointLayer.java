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
public @interface FxPointLayer {

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

  Label label() default @Label(ignore = true, textconvertId = "");

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.ANNOTATION_TYPE)
  public static @interface Label {

    boolean ignore() default false;

    String foregroundColorHex() default "#000";

    String backgroundColorHex() default "#fff";

    double east() default 5;

    double north() default 5;

    double west() default Double.NaN;

    double south() default Double.NaN;

    String textconvertId();

  }
}
