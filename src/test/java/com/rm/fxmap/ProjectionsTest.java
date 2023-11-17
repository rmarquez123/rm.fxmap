package com.rm.fxmap;

import com.rm.fxmap.projections.SpatialProjection;
import com.rm.fxmap.projections.Wgs84Mercator;
import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.core.FxPoint;
import org.junit.Test;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;

/**
 *
 * @author Ricardo Marquez
 */
public class ProjectionsTest {

  @Test
  public void test1() throws Exception {
    long previous = System.currentTimeMillis();
    SpatialProjection projection = new SpatialProjection();
    Wgs84Spheroid inSr = new Wgs84Spheroid();
    Wgs84Mercator outSr = new Wgs84Mercator();
    System.out.println("elapsed time in seconds: " + (System.currentTimeMillis() - previous) / 1000.0);
    for (int i = 0; i < 10; i++) {
      previous = System.currentTimeMillis();
      FxPoint testPt = new FxPoint(-120.43, 37.36, inSr);
      FxPoint pointMerc = projection.project(testPt, outSr);
      FxPoint inverse = projection.project(pointMerc, inSr);
      System.out.println(inverse);
      System.out.println("elapsed time in seconds: " + (System.currentTimeMillis() - previous) / 1000.0);
    }
  }

  @Test
  public void test2() throws Exception {
    CRSFactory crsFactory = new CRSFactory();
    CoordinateReferenceSystem WGS84 = crsFactory.createFromName("epsg:4326");
    CoordinateReferenceSystem UTM = crsFactory.createFromName("epsg:25833");
    
  }

}
