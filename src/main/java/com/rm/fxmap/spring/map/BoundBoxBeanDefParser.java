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
public class BoundBoxBeanDefParser extends AbstractBeanDefinitionParser {

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(BoundBoxFactory.class);
    String xmin = elmnt.getAttribute("xmin");
    String xmax = elmnt.getAttribute("xmax");
    String ymin = elmnt.getAttribute("ymin");
    String ymax = elmnt.getAttribute("ymax");
    result.addPropertyValue("xmin", Double.valueOf(xmin));
    result.addPropertyValue("xmax", Double.valueOf(xmax));
    result.addPropertyValue("ymin", Double.valueOf(ymin));
    result.addPropertyValue("ymax", Double.valueOf(ymax));
    return result.getBeanDefinition();
  }

}
