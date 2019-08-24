package com.rm.fxmap.annotations;

import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.tools.PointSelectOnClick;
import com.rm.springjavafx.FxmlInitializer;
import java.lang.reflect.Field;
import java.util.Map;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
class FxMapToolsBuilder {

  private final FxmlInitializer fxmlInitializer;
  private final ApplicationContext applicationContext;

  /**
   * 
   * @param fxmlInitializer
   * @param applicationContext 
   */
  FxMapToolsBuilder(FxmlInitializer fxmlInitializer, ApplicationContext applicationContext) {
    this.fxmlInitializer = fxmlInitializer;
    this.applicationContext = applicationContext;
  }

  /**
   * 
   * @param mapId
   * @param mapCanvas 
   */
  void build(String mapId, FxCanvas mapCanvas) {
    Map<String, Object> mapToolBeans = this.applicationContext.getBeansWithAnnotation(FxMapTool.class); 
    for (Map.Entry<String, Object> entry : mapToolBeans.entrySet()) {
      Object bean = entry.getValue();
      Field[] fields = bean.getClass().getDeclaredFields();
      for (Field field : fields) {
        FxPointClickSelect fxPointClickConf = field.getDeclaredAnnotation(FxPointClickSelect.class); 
        if (fxPointClickConf != null) {
          PointSelectOnClick click = new PointSelectOnClick(mapCanvas, new Wgs84Spheroid());
          try {
            field.setAccessible(true);
            field.set(bean, click);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    }
  }
  
}
