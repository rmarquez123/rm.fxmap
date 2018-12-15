package com.rm.fxmap.spring.layers;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author rmarquez
 */
public class LayersBeanDefParser extends AbstractBeanDefinitionParser {

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(LayersFactory.class);
    NodeList layerNodes = elmnt.getElementsByTagName("fxmap:layer");
    ManagedList<BeanDefinition> layers = new ManagedList<>();
    for (int i = 0; i < layerNodes.getLength(); i++) {
      Element layerNode = (Element) layerNodes.item(i);
      BeanDefinition beanDef = new LayerBeanDefParser().parseInternal(layerNode, pc);
      layers.add(beanDef); 
    }
    result.addPropertyValue("layers", layers); 
    return result.getBeanDefinition();
  }
  
}
