package com.gei.rm.fxmap.projections;

import com.rm.panzoomcanvas.core.Point;
import com.rm.panzoomcanvas.core.SpatialRef;

/**
 *
 */
public class Wgs84Spheroid extends SpatialRef {
  /**
   * 
   */
  public Wgs84Spheroid() {
    super(4326, new Point(-179.999999, -89.999999), new Point(179.999999, 89.999999));
  }
  
}
