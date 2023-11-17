package com.rm.fxmap.projections;

import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.GeometryProjection;
import com.rm.panzoomcanvas.core.Point;
import com.rm.panzoomcanvas.core.SpatialRef;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.InvalidValueException;
import org.locationtech.proj4j.ProjCoordinate;
import org.locationtech.proj4j.UnknownAuthorityCodeException;
import org.locationtech.proj4j.UnsupportedParameterException;

/**
 *
 * @author rmarquez
 */
public class SpatialProjection implements GeometryProjection {

  private CoordinateReferenceSystem sourceCRS;
  private CoordinateReferenceSystem targetCRS;
  private Map<Integer, CoordinateReferenceSystem> crs = new HashMap<>();

  /**
   *
   */
  public SpatialProjection() {
    try {
      this.sourceCRS = new CRSFactory().createFromName("EPSG:4326");
      this.targetCRS = new CRSFactory().createFromName("EPSG:3857");
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
    int targetSrid = targetSr.getSrid();
    int sourceSrid = geomPoint.getSpatialRef().getSrid();
    CoordinateTransform transform = getTransform(targetSrid, sourceSrid);
    ProjCoordinate resultProjCoordinate = new ProjCoordinate();
    transform.transform(new ProjCoordinate(geomPoint.getX(), geomPoint.getY()), resultProjCoordinate);
    FxPoint result = new FxPoint(resultProjCoordinate.x, resultProjCoordinate.y, targetSr);
    return result;
  }

  /**
   *
   * @param targetSrid
   * @param sourceSrid
   * @return
   * @throws UnknownAuthorityCodeException
   * @throws UnsupportedParameterException
   * @throws InvalidValueException
   */
  private CoordinateTransform getTransform(int targetSrid, int sourceSrid) {
    
    CoordinateReferenceSystem target = this.getCrs(targetSrid);
    CoordinateReferenceSystem source = this.getCrs(sourceSrid);
    CoordinateTransformFactory f = new CoordinateTransformFactory();
    CoordinateTransform transform = f.createTransform(source, target);
    return transform;
  }
  
  /**
   * 
   * @param factory
   * @param srid
   * @return 
   */
  private synchronized CoordinateReferenceSystem getCrs(int srid)  {
    if (!crs.containsKey(srid)) {
      CRSFactory factory = new CRSFactory();
      CoordinateReferenceSystem result = factory.createFromName("EPSG:" + srid);
      this.crs.put(srid, result); 
    }
    return crs.get(srid);
  }

  /**
   *
   * @param p
   * @param sourceSrid
   * @param targetSrid
   * @return
   */
  public Coordinate project(Coordinate p, int sourceSrid, int targetSrid) {
    Objects.requireNonNull(p);
    Objects.requireNonNull(sourceSrid);
    CoordinateTransform transform = getTransform(targetSrid, sourceSrid);
    ProjCoordinate resultProjCoordinate = new ProjCoordinate();
    transform.transform(new ProjCoordinate(p.getX(), p.getY()), resultProjCoordinate);
    Coordinate result = new Coordinate(resultProjCoordinate.x, resultProjCoordinate.y);
    
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
