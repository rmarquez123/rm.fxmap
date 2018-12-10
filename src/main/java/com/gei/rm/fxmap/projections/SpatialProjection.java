package com.gei.rm.fxmap.projections;

import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.GeometryProjection;
import com.rm.panzoomcanvas.core.SpatialRef;
import org.apache.sis.geometry.DirectPosition2D;
import org.apache.sis.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;


/**
 *
 * @author rmarquez
 */
public class SpatialProjection implements GeometryProjection{

  private CoordinateOperation operation;
    
  /**
   * 
   */
  public SpatialProjection() {
    try {
      CoordinateReferenceSystem sourceCRS = CRS.forCode("EPSG:4326"); 
      CoordinateReferenceSystem targetCRS = CRS.fromWKT("PROJCS[\"WGS 84 / Pseudo-Mercator\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137,298.257223563]],PRIMEM[\"Greenwich\",0],UNIT[\"Degree\",0.017453292519943295]],PROJECTION[\"Mercator\"],PARAMETER[\"central_meridian\",0],PARAMETER[\"scale_factor\",1],PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],UNIT[\"Meter\",1]]");
      this.operation = CRS.findOperation(sourceCRS, targetCRS, null);
    } catch(Exception ex) {
      throw new RuntimeException();
    }
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public FxPoint project(FxPoint geomPoint, SpatialRef destination) {
    FxPoint result;
    try {
      DirectPosition ptSrc = new DirectPosition2D(geomPoint.getY(), geomPoint.getX());
      this.operation.getRemarks();
      DirectPosition ptDst = this.operation.getMathTransform().transform(ptSrc, null);
      result = new FxPoint(ptDst.getCoordinate()[0], ptDst.getCoordinate()[1], destination); 
    } catch(Exception ex) {
      throw new RuntimeException(ex); 
    }
    return result;  
  }
    
}
