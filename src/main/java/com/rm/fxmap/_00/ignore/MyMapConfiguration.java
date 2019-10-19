package com.rm.fxmap._00.ignore;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class MyMapConfiguration {
  
  /**
   * 
   * @return 
   */
  @Bean("pointLayerVisibility")
  public Property<Boolean> pointLayerVisibility() {
    Property<Boolean> result = new SimpleObjectProperty<>(true);
    return result;
  }
}
