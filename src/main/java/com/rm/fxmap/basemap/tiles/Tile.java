
package com.rm.fxmap.basemap.tiles;

import com.rm.fxmap.basemap.BaseMapTileLayer;
import com.rm.panzoomcanvas.core.ScreenPoint;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author rmarquez
 */
public abstract class Tile {
  
  private final Property<Image> image = new SimpleObjectProperty<>(null);
  
  private final BaseMapTileLayer.BASE_MAP baseMap;
  
  public Tile(BaseMapTileLayer.BASE_MAP baseMap) {
    this.baseMap = baseMap;
  }
  
  /**
   * 
   * @return 
   */
  public BaseMapTileLayer.BASE_MAP getBaseMap() {
    return baseMap;
  }
  

  public final void loadImage() {
    Image _image = this.onLoadImage(); 
    this.image.setValue(_image);
  }
  /**
   *
   * @return
   */
  public ReadOnlyProperty<Image> getImage() {
    return image;
  }
  
  /**
   *
   * @param graphicsContext2D
   */
  public void addToGraphics(GraphicsContext graphicsContext2D, ScreenPoint screenPos) {
    double y = screenPos.getY();
    double x = screenPos.getX();
    graphicsContext2D.drawImage(image.getValue(), x, y, 256, 256);
  }

  protected abstract Image onLoadImage();
  
}
