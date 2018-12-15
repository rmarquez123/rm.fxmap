package com.rm.fxmap.spring.map;

import com.rm.fxmap.basemap.BaseMapTileLayer;
import com.rm.fxmap.projections.SpatialProjection;
import com.rm.fxmap.projections.Wgs84Mercator;
import com.rm.panzoomcanvas.Content;
import com.rm.panzoomcanvas.FxCanvas;
import com.rm.panzoomcanvas.Layer;
import com.rm.panzoomcanvas.core.FxEnvelope;
import com.rm.panzoomcanvas.projections.Projector;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import javafx.beans.property.ListProperty;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author rmarquez
 */
public class MapFactory implements FactoryBean<FxCanvas>, InitializingBean {
  
  @Autowired
  private FxmlInitializer fxmlInitializer;
  @Autowired
  private ApplicationContext context;
  
  private String parentFxml;
  private String parentNode;
  private ListProperty<Layer> layersRef;
  private FxEnvelope bbox;

  public void setParentFxml(String parentFxml) {
    this.parentFxml = parentFxml;
  }

  public void setParentNode(String parentNode) {
    this.parentNode = parentNode;
  }

  public void setLayersRef(ListProperty<Layer> layersRef) {
    this.layersRef = layersRef;
  }

  public void setBbox(FxEnvelope bbox) {
    this.bbox = bbox;
  }
  
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public FxCanvas getObject() throws Exception {
    SpatialProjection spatialProjection = new SpatialProjection();
    Wgs84Mercator wgs84Mercator = new Wgs84Mercator();
    Projector projector = new Projector(wgs84Mercator, spatialProjection);
    Content content = new Content();
    content.getLayers().getValue().add(new BaseMapTileLayer());
    content.getLayers().getValue().addAll(this.layersRef.getValue());
    FxCanvas result = new FxCanvas(content, projector);
    this.fxmlInitializer.initializeRoots(this.context);
    Pane refPane = (Pane) this.fxmlInitializer.getNode(this.parentFxml, this.parentNode);
    SpringFxUtils.setNodeOnRefPane(refPane, result);
    
    if (this.bbox != null) {
      result.zoomToEnvelope(this.bbox); 
    }
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Class<?> getObjectType() {
    return FxCanvas.class;
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("bbox : " + this.bbox);
  }
  
  
}
