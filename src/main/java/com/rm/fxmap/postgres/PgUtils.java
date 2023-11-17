package com.rm.fxmap.postgres;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKBReader;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Marquez
 */
public class PgUtils {
  
  /**
   *
   */
  private PgUtils() {
  }

  /**
   * 
   * @param sql 
   */
  public static void logLongQuery(String sql) {
    Logger.getLogger(PgUtils.class.getName()).log(Level.INFO, "long query: \n{0}", sql);
  }
  
  /**
   * Executes: <br>
   * <pre>
   * String result = String.format("ST_MakeEnvelope(%f, %f, %f, %f, %n)", new Object[]{
   * env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY(), srid
   * })
   * </pre>
   *
   * @param env
   * @param srid
   * @return
   */
  public static String getMakeEnvelopeText(Envelope env, int srid) {
    Objects.requireNonNull(env, "Envelope cannot be null"); 
    String result;
    result = String.format("st_makeenvelope(%f, %f, %f, %f, %d)", new Object[]{
      env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY(), srid
    });
    return result;
  }

  /**
   *
   * @param srid
   * @return
   */
  public static WKBReader getWKBReader(int srid) {
    GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
    WKBReader result = new WKBReader(geomFactory);
    return result;
  }

}
