package com.rm.fxmap;

import com.rm.panzoomcanvas.FxCanvas;
import javafx.scene.Node;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class MapTool {

  /**
   *
   */
  public MapTool() {
  }

  /**
   *
   * @param mapCanvas
   */
  public void addToMap(FxCanvas mapCanvas) {
    Node node = this.onGetNode();
    mapCanvas.addTool(node);
  }

  /**
   *
   * @return
   */
  protected abstract Node onGetNode();
}
