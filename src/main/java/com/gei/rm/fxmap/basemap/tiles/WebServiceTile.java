
package com.gei.rm.fxmap.basemap.tiles;

import javafx.scene.image.Image;

/**
 *
 * @author rmarquez
 */
public class WebServiceTile extends Tile {
  private final String url;
  
  
  public WebServiceTile(String url) {
    this.url = url;
  }


  /**
   * 
   */
  @Override
  public Image onLoadImage() {
    Image _image = new Image(url);
    return _image;
  }
  
}
