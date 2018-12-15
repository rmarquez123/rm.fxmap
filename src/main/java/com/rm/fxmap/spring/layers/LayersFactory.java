package com.rm.fxmap.spring.layers;

import com.rm.panzoomcanvas.Layer;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author rmarquez
 */
public class LayersFactory implements FactoryBean<ListProperty<Layer>> {

  private List<Layer> layers = new ArrayList<>();
  
  
  /**
   * 
   * @param layers 
   */
  public void setLayers(List<Layer> layers) {
    this.layers.addAll(layers); 
  }
  
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public ListProperty<Layer> getObject() throws Exception {
    ObservableList<Layer> obsList = FXCollections.observableArrayList(this.layers);
    SimpleListProperty<Layer> result = new SimpleListProperty<>(obsList);
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Class<?> getObjectType() {
    return ListProperty.class;
  }

}
