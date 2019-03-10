package com.rm.fxmap.tools;

import com.rm.fxmap.MapTool;
import com.rm.fxmap.basemap.BaseMapTileLayer;
import com.rm.fxmap.basemap.BaseMapTileLayer.BASE_MAP;
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
    Label label = new Label();
    label.setMinWidth(75);
    this.baseMapTileLayer.baseMapProperty().addListener((obs, old, change) -> {
      this.updateNode(imageView, label);
    });
    this.updateNode(imageView, label);
    StackPane result = new StackPane(imageView, label);
    result.setOnMouseClicked((event) -> {
      BaseMapTileLayer.BASE_MAP current = this.baseMapTileLayer.baseMapProperty().getValue();
      switch (current) {
        case ESRI_STREET_MAP:
          this.baseMapTileLayer.baseMapProperty().setValue(BASE_MAP.ESRI_WORLD);
          break;
        case ESRI_WORLD:
          this.baseMapTileLayer.baseMapProperty().setValue(BASE_MAP.ESRI_STREET_MAP);
          break;
        default:
          throw new AssertionError();
      }
    });
    
    result.setMaxWidth(75);
    result.setMaxHeight(75);
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
    String image;
    String text;
    if (this.baseMapTileLayer.baseMapProperty().getValue() == BaseMapTileLayer.BASE_MAP.ESRI_STREET_MAP) {
      image = "fxmap\\images\\satellite.jpg";
      text = "Imagery";
      imageView.setImage(new Image(image));
      label.setText(text);
    } else {
      image = "fxmap\\images\\topo.jpg";
      text = "Topographic";
      imageView.setImage(new Image(image));
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
