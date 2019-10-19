package com.rm.fxmap.annotations;

import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.panzoomcanvas.layers.points.PointsLayer;
import javafx.collections.ObservableList;

/**
 *
 * @author Ricardo Marquez
 */
public class AbstractFxPointLayer implements FxMapLayer {

  private PointsLayer layer;

  /**
   *
   * @param layer
   */
  @Override
  public final void setLayer(Layer layer) {
    if ( !(layer instanceof PointsLayer)) {
      throw new IllegalArgumentException();
    }
    this.layer = (PointsLayer) layer;
  }

  /**
   *
   * @return
   */
  public final PointsLayer getLayer() {
    return layer;
  }

  /**
   *
   */
  public final ObservableList<PointMarker> pointMarkers() {
    if (this.layer == null) {
      throw new NullPointerException("layer has not been initialized");
    }
    return this.layer.getSource().pointMarkersProperty();
  }

}
