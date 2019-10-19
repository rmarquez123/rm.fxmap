package com.rm.fxmap._00.ignore;

import com.rm.fxmap.annotations.AbstractFxPointLayer;
import com.rm.fxmap.annotations.FxPointLayer;
import com.rm.fxmap.projections.Wgs84Spheroid;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.layers.points.PointMarker;
import com.rm.springjavafx.FxmlInitializer;
import java.util.function.Function;
import javafx.util.StringConverter;
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
  basecolorHex = "#302a25",
  basewidth = 4,
  selectedColorHex = "#eb8c34",
  selectedWidth = 4,
  visibilityId = "pointLayerVisibility",
  label = @FxPointLayer.Label(textconvertId = "pointsLayerTextConverter")
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
      PointMarker marker = new PointMarker("marker", new FxPoint(-118.0, 37.0, sr));
      marker.setLabelProperty(new StringConverter() {
        @Override
        public String toString(Object object) {
          return "A Point";
        }

        @Override
        public Object fromString(String string) {
          return null;
        }
      });
      super.pointMarkers().addAll(marker);
    });
  }

  /**
   *
   */
  @Component("pointsLayerTextConverter")
  public static class PointsLayerTextConverter implements Function<PointMarker, String> {

    @Override
    public String apply(PointMarker t) {
      return "A Point";
    }
  }
}
