package com.rm.fxmap;

import com.rm.fxmap.projections.SpatialProjection;
import com.rm.fxmap.projections.Wgs84Mercator;
import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.core.FxPoint;
import java.util.Collection;
import org.apache.sis.metadata.iso.extent.DefaultGeographicBoundingBox;
import org.apache.sis.referencing.CRS;
import org.junit.Test;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

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
    System.out.println("elapsed time in seconds: " + (System.currentTimeMillis() - previous)/1000.0);
    for (int i = 0; i < 10; i++) {
      previous = System.currentTimeMillis();
      FxPoint testPt = new FxPoint(-120.43, 37.36, inSr);
      FxPoint pointMerc = projection.project(testPt, outSr);
      System.out.println(pointMerc);
      System.out.println("elapsed time in seconds: " + (System.currentTimeMillis() - previous)/1000.0);
    }
  }
  
  
  
  @Test
  public void test2() throws Exception {
    CoordinateReferenceSystem targetCRS = CRS.forCode("EPSG:3857");
    Collection<? extends GeographicExtent> geoDomains = targetCRS.getDomainOfValidity().getGeographicElements();
    for (GeographicExtent geoDomain : geoDomains) {
      ((DefaultGeographicBoundingBox) geoDomain).getEastBoundLongitude();
      ((DefaultGeographicBoundingBox) geoDomain).getNorthBoundLatitude();
      System.out.println();
    }
  }

}
