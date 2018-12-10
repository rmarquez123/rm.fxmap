package com.rm.fxmap.basemap;


import com.rm.panzoomcanvas.core.Level;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.VirtualEnvelope;
import com.rm.panzoomcanvas.layers.DrawArgs;
import com.rm.panzoomcanvas.projections.Projector;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;

/**
 *
 * @author rmarquez
 */
public class DrawTemporaryTilesTask implements Task {

  private final BaseMapTileLayer host;
  private final DrawArgs args;

  /**
   *
   * @param host
   * @param args
   */
  public DrawTemporaryTilesTask(BaseMapTileLayer host, DrawArgs args) {
    this.host = host;
    this.args = args;
  }

  @Override
  public void execute() {
    DrawArgs drawArgs = this.host.lastDrawArgs;
    if (drawArgs != null) {
      Canvas lastCanvas = (Canvas) drawArgs.getLayerCanvas();
      Level lastLevel = this.host.lastDrawArgs.getLevel();
      Level level = args.getLevel();
      double f = Math.pow(2.0, -((double) lastLevel.getValue() - (double) level.getValue()));
      SnapshotParameters params = new SnapshotParameters();
      params.setTransform(new Scale(f, f));
      WritableImage image = lastCanvas.snapshot(params, null);
      Canvas canvas = (Canvas) this.args.getLayerCanvas();
      
      canvas.getGraphicsContext2D().clearRect(0, 0,
              canvas.getWidth(), canvas.getHeight());
      Projector projector = this.args.getCanvas().getProjector();
      VirtualEnvelope virtual = projector.projectScreenToVirtual(this.host.lastDrawArgs.getScreenEnv()); 
      ScreenEnvelope sc = projector.projectVirtualToScreen(virtual, this.args.getScreenEnv());
      
      double x = sc.getMin().getX();
      double y = sc.getMin().getY();
      double height = sc.getHeight();
      double width = sc.getWidth();
      canvas.getGraphicsContext2D().drawImage(image, x, y, width, height);
    }

  }

  @Override
  public void cancel() {

  }

}
