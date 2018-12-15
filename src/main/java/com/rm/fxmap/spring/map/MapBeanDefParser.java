package com.rm.fxmap.spring.map;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class MapBeanDefParser extends AbstractBeanDefinitionParser {
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(MapFactory.class);
    String parentFxml = elmnt.getAttribute("parentFxml");
    String parentNode = elmnt.getAttribute("parentNode");
    String layersRef = elmnt.getAttribute("layersRef");
    String bbox = elmnt.getAttribute("bbox");
    result.addPropertyValue("parentFxml", parentFxml);
    result.addPropertyValue("parentNode", parentNode);
    result.addPropertyReference("layersRef", layersRef);
    result.addPropertyReference("bbox", bbox);
    result.setLazyInit(false); 
    
    return result.getBeanDefinition();
  }

}
