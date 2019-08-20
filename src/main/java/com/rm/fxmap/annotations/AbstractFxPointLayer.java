package com.rm.fxmap.annotations;

import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import javafx.collections.ObservableList;

/**
 *
 * @author Ricardo Marquez
 */
public class AbstractFxPointLayer {
  
  private PointsLayer layer;
  
  
  /**
   * 
   * @param layer 
   */
  public void setPointsLayer(PointsLayer layer) {
    this.layer = layer;
  }

  /**
   *
   */
  public ObservableList<PointMarker> pointMarkers() {
    if(this.layer == null) {
      throw new NullPointerException("layer has not been initialized"); 
    }
    return this.layer.getSource().pointMarkersProperty();
  }

}
