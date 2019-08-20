package com.rm.fxmap.annotations;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class FxMapAnnotationHandler implements InitializingBean, AnnotationHandler {

  @Autowired
  private FxmlInitializer fxmlInitializer;
  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addAnnotationHandler(this);
  }

  @Override
  public void readyFxmls() {
  }

  /**
   *
   */
  @Override
  public void setNodes() {
    Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(FxMap.class);
    FxMapBuilder mapBuilder = new FxMapBuilder(this.fxmlInitializer, this.applicationContext);
    FxMapLayerBuilder layerBuilder = new FxMapLayerBuilder(fxmlInitializer, applicationContext);
    for (Map.Entry<String, Object> entry : beans.entrySet()) {
      String beanId = entry.getKey();
      Object bean = entry.getValue();
      FxCanvas mapCanvas = mapBuilder.createMap(bean);
      String id = bean.getClass().getDeclaredAnnotation(FxMap.class).id();
      Map<String, Object> pointLayers = this.applicationContext.getBeansWithAnnotation(FxPointLayer.class);
      layerBuilder.createLayers(id, mapCanvas, pointLayers);
      this.registerBean(id, mapCanvas);
    }
  }

  /**
   *
   * @param id
   * @param mapCanvas
   */
  private void registerBean(String id, FxCanvas mapCanvas) {
    BeanDefinitionRegistry registry 
      = (BeanDefinitionRegistry) this.applicationContext.getAutowireCapableBeanFactory();
    BeanDefinition dynamicBean = BeanDefinitionBuilder
      .rootBeanDefinition(FxMapProxy.class)
      .getBeanDefinition();
    dynamicBean.getPropertyValues().add("mapcanvas", mapCanvas); 
    registry.registerBeanDefinition(id, dynamicBean);
    
  }
  

}
