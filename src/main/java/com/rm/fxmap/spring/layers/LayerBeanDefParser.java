package com.rm.fxmap.spring.layers;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class LayerBeanDefParser extends AbstractBeanDefinitionParser {
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(LayerFactory.class);
    result.addPropertyValue("name", elmnt.getAttribute("name")); 
    result.addPropertyValue("type", elmnt.getAttribute("type")); 
    return result.getBeanDefinition();
  }
  
}
