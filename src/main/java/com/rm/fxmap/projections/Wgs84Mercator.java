package com.rm.fxmap.projections;

import com.rm.panzoomcanvas.core.Point;
import com.rm.panzoomcanvas.core.SpatialRef;



/**
 *
 * @author rmarquez
 */
public class Wgs84Mercator extends SpatialRef{
  
  /**
   * 
   */
  public Wgs84Mercator() {
    super(3857, new Point(-20037508.3427892, -20037508.3427892),
            new Point(20037508.3427892, 20037508.3427892));
  }
  
}
