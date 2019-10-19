package com.rm.fxmap.annotations;

import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.layers.polyline.PolyLineLayer;
import com.rm.panzoomcanvas.layers.polyline.PolyLineMarker;
import javafx.collections.ObservableList;

/**
 *
 * @author Ricardo Marquez
 */
public class AbstractFxLineLayer implements FxMapLayer {

  private PolyLineLayer layer;

  /**
   *
   * @param layer
   */
  @Override
  public void setLayer(Layer layer) {
    if (!(layer instanceof PolyLineLayer)) {
      throw new IllegalArgumentException();
    }
    this.layer = (PolyLineLayer) layer;
  }

  /**
   *
   * @return
   */
  public PolyLineLayer getLayer() {
    return layer;
  }

  /**
   *
   */
  public ObservableList<PolyLineMarker> lineMarkers() {
    if (this.layer == null) {
      throw new NullPointerException("layer has not been initialized");
    }
    return this.layer.source().markers();
  }

}
