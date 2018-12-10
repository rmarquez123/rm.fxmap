package com.rm.fxmap.spring;

import com.rm.fxmap.spring.layers.LayerBeanDefParser;
import com.rm.fxmap.spring.layers.LayersBeanDefParser;
import com.rm.fxmap.spring.map.BoundBoxBeanDefParser;
import com.rm.fxmap.spring.map.MapBeanDefParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 *
 * @author rmarquez
 */
public class SpringNameSpaceHandler extends NamespaceHandlerSupport {

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void init() {
    this.registerBeanDefinitionParser("bbox-property", new BoundBoxBeanDefParser());
    this.registerBeanDefinitionParser("layer", new LayerBeanDefParser());
    this.registerBeanDefinitionParser("layers", new LayersBeanDefParser());
    this.registerBeanDefinitionParser("map", new MapBeanDefParser());
  }

}
