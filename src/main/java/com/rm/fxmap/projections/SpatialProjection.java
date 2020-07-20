package com.rm.fxmap.projections;

import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.GeometryProjection;
import com.rm.panzoomcanvas.core.Point;
import com.rm.panzoomcanvas.core.SpatialRef;
import com.vividsolutions.jts.geom.Coordinate;
import java.util.Objects;
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
  public FxPoint project(FxPoint geomPoint, SpatialRef targetSr) {
    Objects.requireNonNull(geomPoint);
    Objects.requireNonNull(targetSr);
    FxPoint result;
    int targetSrid = targetSr.getSrid();
    int sourceSrid = geomPoint.getSpatialRef().getSrid();

    try {
      if (targetSrid == sourceSrid) {
        result = geomPoint;
      } else if (targetSrid == 4326) {
        CoordinateReferenceSystem target = CRS.forCode("EPSG:" + targetSrid);
        CoordinateReferenceSystem source = CRS.forCode("EPSG:" + sourceSrid);
        CoordinateOperation op = CRS.findOperation(target, source, null);
        DirectPosition ptSrc = new DirectPosition2D(geomPoint.getX(), geomPoint.getY());
        DirectPosition ptDst = op.getMathTransform().inverse().transform(ptSrc, null);
        result = new FxPoint(ptDst.getCoordinate()[1], ptDst.getCoordinate()[0], targetSr);
      } else if (sourceSrid == 4326) {
        CoordinateReferenceSystem target = CRS.forCode("EPSG:" + targetSrid);
        CoordinateReferenceSystem source = CRS.forCode("EPSG:" + sourceSrid);
        CoordinateOperation op = CRS.findOperation(target, source, null);
        DirectPosition ptSrc = new DirectPosition2D(geomPoint.getY(), geomPoint.getX());
        DirectPosition ptDst = op.getMathTransform().inverse().transform(ptSrc, null);
        result = new FxPoint(ptDst.getCoordinate()[0], ptDst.getCoordinate()[1], targetSr);
      } else {
        CoordinateReferenceSystem target = CRS.forCode("EPSG:" + targetSrid);
        CoordinateReferenceSystem source = CRS.forCode("EPSG:" + sourceSrid);
        CoordinateOperation op = CRS.findOperation(source, target, null);
        DirectPosition ptSrc = new DirectPosition2D(geomPoint.getX(), geomPoint.getY());
        DirectPosition ptDst = op.getMathTransform().transform(ptSrc, null);
        result = new FxPoint(ptDst.getCoordinate()[0], ptDst.getCoordinate()[1], targetSr);
      }
    } catch (Exception ex) {
      throw new RuntimeException("Error on projecting point. Check args : {"
        + "geomPoint = " + geomPoint
        + ", targetSr  =" + targetSr.getSrid()
        + "}", ex);
    }
    return result;
  }

  /**
   *
   * @param p
   * @param sourceSrid
   * @param targetSrid
   * @return
   */
  public Coordinate project(Coordinate p, int sourceSrid, int targetSrid) {
    Coordinate result;
    try {
      if (targetSrid == sourceSrid) {
        result = p;
      } else if (targetSrid == 4326) {
        CoordinateReferenceSystem target = CRS.forCode("EPSG:" + targetSrid);
        CoordinateReferenceSystem source = CRS.forCode("EPSG:" + sourceSrid);
        CoordinateOperation op = CRS.findOperation(target, source, null);
        DirectPosition ptSrc = new DirectPosition2D(p.x, p.y);
        DirectPosition ptDst = op.getMathTransform().inverse().transform(ptSrc, null);
        result = new Coordinate(ptDst.getCoordinate()[1], ptDst.getCoordinate()[0]);
      } else if (sourceSrid == 4326) {
        CoordinateReferenceSystem target = CRS.forCode("EPSG:" + targetSrid);
        CoordinateReferenceSystem source = CRS.forCode("EPSG:" + sourceSrid);
        CoordinateOperation op = CRS.findOperation(target, source, null);
        DirectPosition ptSrc = new DirectPosition2D(p.y, p.x);
        DirectPosition ptDst = op.getMathTransform().inverse().transform(ptSrc, null);
        result = new Coordinate(ptDst.getCoordinate()[0], ptDst.getCoordinate()[1]);
      } else {
        CoordinateReferenceSystem target = CRS.forCode("EPSG:" + targetSrid);
        CoordinateReferenceSystem source = CRS.forCode("EPSG:" + sourceSrid);
        CoordinateOperation op = CRS.findOperation(source, target, null);
        DirectPosition ptSrc = new DirectPosition2D(p.x, p.y);
        DirectPosition ptDst = op.getMathTransform().transform(ptSrc, null);
        result = new Coordinate(ptDst.getCoordinate()[0], ptDst.getCoordinate()[1]);
      }
    } catch (Exception ex) {
      throw new RuntimeException("Error on projecting point. Check args : {"
        + "geomPoint = " + p
        + ", sourceSrid  =" + sourceSrid
        + ", targetSrid  =" + targetSrid
        + "}", ex);
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
