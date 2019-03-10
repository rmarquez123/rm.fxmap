
package com.rm.fxmap.basemap.tiles;

import com.rm.fxmap.basemap.BaseMapTileLayer;
import javafx.scene.image.Image;

/**
 *
 * @author rmarquez
 */
public class WebServiceTile extends Tile {
  private final String url;
  
  /**
   * 
   * @param baseMap
   * @param url 
   */
  public WebServiceTile(BaseMapTileLayer.BASE_MAP baseMap, String url) {
    super(baseMap);
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
