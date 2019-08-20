package com.rm.fxmap._00.ignore;

import com.rm.fxmap.annotations.AbstractFxPointLayer;
import com.rm.fxmap.annotations.FxPointLayer;
import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.layers.points.PointMarker;
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
@FxPointLayer(
  mapId = "mymap",
  name = "Blue Points",
  selectedBeanId = "selectedbluepoint",
  basecolorHex = "#302a25",
  basewidth = 4,
  selectedColorHex = "#eb8c34",
  selectedWidth = 4
)
public class BluePointsLayer extends AbstractFxPointLayer implements InitializingBean {

  @Autowired
  private FxmlInitializer fxmlInitializer;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i) -> {
      super.pointMarkers().clear();
      
      Wgs84Spheroid sr = new Wgs84Spheroid(); 
      PointMarker marker = new PointMarker("blah", new FxPoint(-118.0, 37.0, sr)); 
      
      super.pointMarkers().addAll(marker); 
    });
  }
}
