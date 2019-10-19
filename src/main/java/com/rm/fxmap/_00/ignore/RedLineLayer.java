package com.rm.fxmap._00.ignore;

import com.rm.fxmap.annotations.AbstractFxLineLayer;
import com.rm.fxmap.annotations.FxLineLayer;
import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.layers.polyline.PolyLineMarker;
import com.rm.springjavafx.FxmlInitializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@FxLineLayer(
  mapId = "mymap",
  name = "Blue Points",
  basecolorHex = "purple",
  basewidth = 4,
  selectedColorHex = "#eb8c34",
  selectedWidth = 4,
  visibilityId = "pointLayerVisibility"
)
public class RedLineLayer extends AbstractFxLineLayer implements InitializingBean {

  @Autowired
  private FxmlInitializer fxmlInitializer;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i) -> {
      super.lineMarkers().clear();
      Wgs84Spheroid sr = new Wgs84Spheroid();
      double[] x = new double[]{
        -118.0,
        -117.0,
        -118.0,};
      double[] y = new double[]{
        37.0,
        38.0,
        39.0,};
      PolyLineMarker marker = new PolyLineMarker("marker_0", sr, x, y, 3);
      super.lineMarkers().addAll(marker);
    });
  }

}
