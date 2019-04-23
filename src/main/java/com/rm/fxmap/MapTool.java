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
    Node node = this.getNode();
    mapCanvas.addTool(node);
  }
  
  public Node getNode() {
    Node node = this.onGetNode();
    node.getStyleClass().add("maptool"); 
    return node;
  }
  
  /**
   *
   * @return
   */
  protected abstract Node onGetNode();
}
