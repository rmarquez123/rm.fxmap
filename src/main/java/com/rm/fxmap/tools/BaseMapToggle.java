package com.rm.fxmap.tools;

import com.rm.fxmap.MapTool;
import com.rm.fxmap.basemap.BaseMap;
import com.rm.fxmap.basemap.BaseMapTileLayer;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Ricardo Marquez
 */
public class BaseMapToggle extends MapTool {

  private final BaseMapTileLayer baseMapTileLayer;

  /**
   *
   * @param baseMapTileLayer
   */
  public BaseMapToggle(BaseMapTileLayer baseMapTileLayer) {
    this.baseMapTileLayer = baseMapTileLayer;
  }

  /**
   *
   * @return
   */
  @Override
  protected Node onGetNode() {
    ImageView imageView = new ImageView();
    imageView.getStyleClass().add("basemaptoggle-image");
    Label label = new Label();
    label.setMinWidth(50);
    this.baseMapTileLayer.baseMapProperty().addListener((obs, old, change) -> {
      this.updateNode(imageView, label);
    });
    this.updateNode(imageView, label);
    StackPane result = new StackPane(imageView, label);
    result.getStyleClass().add("basemaptoggle");
    result.setOnMouseClicked((event) -> {
      BaseMap current = this.baseMapTileLayer.baseMapProperty().getValue();
      switch (current) {
        case ESRI_STREET_MAP:
          this.baseMapTileLayer.baseMapProperty().setValue(BaseMap.ESRI_WORLD);
          break;
        case ESRI_WORLD:
          this.baseMapTileLayer.baseMapProperty().setValue(BaseMap.ESRI_STREET_MAP);
          break;
        default:
          throw new AssertionError();
      }
    });

    result.setMaxWidth(50);
    result.setMaxHeight(50);
    StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
    StackPane.setAlignment(result, Pos.TOP_LEFT);
    StackPane.setMargin(result, new Insets(10, 10, 10, 10));
    return result;
  }

  /**
   *
   * @param imageView
   * @param label
   */
  private void updateNode(ImageView imageView, Label label) {
    String imgResource;
    InputStream imageStream;
    String text;
    if (this.baseMapTileLayer.baseMapProperty().getValue() == BaseMap.ESRI_STREET_MAP) {
      imgResource = "fxmap/images/satellite.jpg";
      imageStream = this.getClass().getClassLoader().getResourceAsStream(imgResource);
      text = "Imagery";
      double w = 60;
      double h = 60;
      Image imageObj;
      try {
        imageObj = new Image(imageStream, w, h, true, true);
      } catch(Exception ex) {
        throw new RuntimeException("Error on creating image.  Check args: {"
          + "imageUrl = " + imgResource
          + "}", ex); 
      }
      imageView.setImage(imageObj);
      label.setText(text);
    } else {
      imgResource = "fxmap/images/topo.jpg";
      imageStream = this.getClass().getClassLoader().getResourceAsStream(imgResource);
      text = "Topographic";
      double w = 60;
      double h = 60;
      imageView.setImage(new Image(imageStream, w, h, true, true));
      label.setText(text);
      label.setTextFill(Color.BLACK);
    }

    label.setTextFill(Color.WHITE);
    Color refColor = Color.BLACK;
    Color color = new Color(refColor.getRed(), refColor.getGreen(), refColor.getBlue(), 0.5);
    BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
    Background background = new Background(backgroundFill);
    label.setBackground(background);

  }

}
