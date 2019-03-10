package com.rm.fxmap.projections;

import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.GeometryProjection;
import com.rm.panzoomcanvas.core.Point;
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
public class SpatialProjection implements GeometryProjection {

  private static CoordinateOperation operation;
  private CoordinateReferenceSystem sourceCRS;
  private CoordinateReferenceSystem targetCRS;

  /**
   *
   */
  public SpatialProjection() {
    try {
      this.sourceCRS = CRS.forCode("EPSG:4326");
      this.targetCRS = CRS.forCode("EPSG:3857");
      if (operation == null) {
        operation = CRS.findOperation(sourceCRS, targetCRS, null);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
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
      if (geomPoint.getSpatialRef().getSrid() == destination.getSrid()) {
        result = geomPoint;
      } else if (destination.getSrid() == 3857 && geomPoint.getSpatialRef().getSrid() == 4326) {
        DirectPosition ptSrc = new DirectPosition2D(geomPoint.getY(), geomPoint.getX());
        DirectPosition ptDst = operation.getMathTransform().transform(ptSrc, null);
        result = new FxPoint(ptDst.getCoordinate()[0], ptDst.getCoordinate()[1], destination);
      } else if (destination.getSrid() == 4326 && geomPoint.getSpatialRef().getSrid() == 3857) {
        DirectPosition ptSrc = new DirectPosition2D(geomPoint.getX(), geomPoint.getY());
        DirectPosition ptDst = operation.getMathTransform().inverse().transform(ptSrc, null);
        result = new FxPoint(ptDst.getCoordinate()[1], ptDst.getCoordinate()[0], destination);
      } else {
        throw new RuntimeException("Invalid arguments"); 
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   *
   * @return
   */
  @Override
  public Point getMax() {
    double x = 20037508.3428;
    double y = 20048966.104;
    return new Point(x, y);
  }

  /**
   *
   * @return
   */
  @Override
  public Point getMin() {
    double x = -20037508.3428;
    double y = -20048966.104;
    return new Point(x, y);
  }

}
