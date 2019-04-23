package com.rm.fxmap;

import common.colormap.LinearRangeColorModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Ricardo Marquez
 */
public final class ColorLegendMapTool extends MapTool {

  private final Property<LinearRangeColorModel> colorModelProperty = new SimpleObjectProperty<>();
  private final Property<LinearGradient> linearGradient = new SimpleObjectProperty<>();
  private final DoubleProperty widthProperty = new SimpleDoubleProperty(20);
  private final DoubleProperty heightProperty = new SimpleDoubleProperty(100);
  private final Label label;
  private Group root;
  private final Label maxLabel;
  private final Label minLabel;
  private Rectangle rectangle;

  public ColorLegendMapTool(LinearRangeColorModel colorModel) {
    this.setColorModel(colorModel);
    this.label = new Label("Color ramp");
    this.linearGradient.bind(Bindings.createObjectBinding(() -> {
      return this.updateColorRamp();
    }, this.widthProperty, this.heightProperty, this.colorModelProperty));

    this.linearGradient.addListener((obs, old, change) -> this.setRectangleFill());
    this.maxLabel = new Label("Max");
    this.minLabel = new Label("Min");
    this.minLabel.textProperty().bind(Bindings.createStringBinding(() -> {
      double minValue = this.getCurrentMinValue();
      return String.valueOf(minValue);
    }, this.colorModelProperty));
    this.maxLabel.textProperty().bind(Bindings.createStringBinding(() -> {
      double maxValue = this.getCurrentMaxValue();
      return String.valueOf(maxValue);
    }, this.colorModelProperty));
  }

  /**
   *
   * @return
   */
  public Property<LinearRangeColorModel> colorModelProperty() {
    return colorModelProperty;
  }

  /**
   *
   * @return
   */
  public LinearRangeColorModel getColorModel() {
    return colorModelProperty.getValue();
  }

  /**
   *
   * @param cm
   */
  public void setColorModel(LinearRangeColorModel cm) {
    this.colorModelProperty.setValue(cm);
  }

  /**
   *
   * @return
   */
  public Label getLabel() {
    return this.label;
  }

  /**
   *
   * @return
   */
  @Override
  protected Node onGetNode() {
    if (this.root == null) {
      VBox container = new VBox();
      container.getChildren().add(this.label);
      double width = this.widthProperty.getValue();
      double height = this.heightProperty.getValue();
      this.rectangle = new Rectangle(width, height);
      this.setRectangleFill();

      HBox colorMapArea = new HBox();

      colorMapArea.getChildren().add(rectangle);

      VBox colorMapLabels = new VBox();

      colorMapLabels.getChildren().add(this.maxLabel);
      Region spacer = new Region();
      spacer.setMinHeight(Region.USE_PREF_SIZE);
      spacer.setMaxHeight(Double.POSITIVE_INFINITY);
      colorMapLabels.getChildren().add(spacer);
      VBox.setVgrow(spacer, Priority.ALWAYS);
      colorMapLabels.getChildren().add(this.minLabel);
      colorMapArea.getChildren().add(colorMapLabels);
      container.getChildren().add(colorMapArea);
      VBox.setVgrow(colorMapArea, Priority.ALWAYS);
      this.root = new Group(container);
      ContextMenu menu = new ContextMenu();
      MenuItem menuItem = new MenuItem("Change");
      menuItem.setOnAction((evt) -> {
        this.onChangeMenuItem(menuItem);
      });
      menu.getItems().add(menuItem);
      colorMapArea.setOnContextMenuRequested((evt) -> {
        menu.show(colorMapArea, evt.getScreenX(), evt.getScreenY());
      });
      this.root.parentProperty().addListener((obs, old, change) -> {
        StackPane.setAlignment(root, Pos.BOTTOM_LEFT);
        StackPane.setMargin(root, new Insets(20));
      });

      this.root.getStyleClass().add("coloramp");

    }
    return root;
  }

  private void setRectangleFill() {
    this.rectangle.setFill(linearGradient.getValue());
  }

  /**
   *
   * @param menuItem
   */
  private void onChangeMenuItem(MenuItem menuItem) {
    Alert alert = new Alert(Alert.AlertType.NONE);
    alert.getButtonTypes().add(ButtonType.APPLY);
    alert.getButtonTypes().add(ButtonType.CANCEL);
    alert.setResizable(true);
    alert.getDialogPane().setMinWidth(Region.USE_COMPUTED_SIZE);
    alert.getDialogPane().setPrefWidth(Region.USE_COMPUTED_SIZE);
    alert.setWidth(100.0);
    VBox vbox = new VBox();
    alert.setX(menuItem.getParentPopup().getX());
    alert.setY(menuItem.getParentPopup().getY());

    ColorPicker maxColorPicker = new ColorPicker(this.getCurrentMaxColor());
    TextField maxValueTextField = new TextField();
    maxValueTextField.setPrefWidth(80);
    maxValueTextField.setText(String.valueOf(this.getCurrentMaxValue()));
    Label maxLabel = new Label("Maximum Value: ");
    maxLabel.setPrefWidth(100.0);
    HBox maxContainer = new HBox(maxLabel, maxValueTextField, maxColorPicker);
    maxContainer.setSpacing(5);
    vbox.getChildren().add(maxContainer);
    ColorPicker minColorPicker = new ColorPicker(this.getCurrentMinColor());
    TextField minValueTextField = new TextField();
    minValueTextField.setPrefWidth(80);
    minValueTextField.setText(String.valueOf(this.getCurrentMinValue()));
    Label minLabel = new Label("Minimum Value:");
    minLabel.setPrefWidth(100.0);
    HBox minContainer = new HBox(minLabel, minValueTextField, minColorPicker);
    minContainer.setSpacing(5.0);
    vbox.getChildren().add(minContainer);
    alert.setGraphic(vbox);
    Optional<ButtonType> optionalBtnType = alert.showAndWait();
    if (optionalBtnType.isPresent() && optionalBtnType.get() == ButtonType.APPLY) {
      Stop maxStop = new Stop(Double.parseDouble(maxValueTextField.getText()), maxColorPicker.getValue());
      Stop minStop = new Stop(Double.parseDouble(minValueTextField.getText()), minColorPicker.getValue());
      LinearRangeColorModel cm = new LinearRangeColorModel(minStop, maxStop);
      this.colorModelProperty.setValue(cm);
    }
  }

  /**
   *
   */
  private LinearGradient updateColorRamp() {
    LinearGradient result;

    double width = this.widthProperty.getValue();
    double height = this.heightProperty.getValue();
    if (colorModelProperty.getValue() != null) {
      Stop[] stops = this.colorModelProperty.getValue().getStops();
      stops = new Stop[]{
        new Stop(0., stops[0].getColor()),
        new Stop(1., stops[1].getColor())
      };
      result = new LinearGradient(0, height, width, 0, false, CycleMethod.NO_CYCLE, stops);
    } else {
      result = new LinearGradient(0, height, width, 0, false, CycleMethod.NO_CYCLE, new Stop[]{
        new Stop(height, Color.WHITE), new Stop(height, Color.WHITE)
      });
    }
    return result;
  }

  /**
   *
   * @return
   */
  private double getCurrentMaxValue() {
    double maxValue;
    if (this.getColorModel() != null) {
      List<Stop> stops = Arrays.asList(getColorModel().getStops());
      Stop s = Collections.max(stops, (i1, i2) -> Double.compare(i1.getOffset(), i2.getOffset()));
      maxValue = s.getOffset();
    } else {
      maxValue = Double.NaN;
    }
    return maxValue;
  }

  /**
   *
   * @return
   */
  private double getCurrentMinValue() {
    double minValue;
    if (this.getColorModel() != null) {
      List<Stop> stops = Arrays.asList(getColorModel().getStops());
      Stop s = Collections.min(stops, (i1, i2) -> Double.compare(i1.getOffset(), i2.getOffset()));
      minValue = s.getOffset();
    } else {
      minValue = Double.NaN;
    }

    return minValue;
  }

  /**
   *
   * @return
   */
  private Color getCurrentMaxColor() {
    Color minValue;
    if (this.getColorModel() != null) {
      List<Stop> stops = Arrays.asList(getColorModel().getStops());
      Stop s = Collections.max(stops, (i1, i2) -> Double.compare(i1.getOffset(), i2.getOffset()));
      minValue = s.getColor();
    } else {
      minValue = Color.WHITE;
    }
    return minValue;
  }

  /**
   *
   * @return
   */
  private Color getCurrentMinColor() {
    Color minValue;
    if (this.getColorModel() != null) {
      List<Stop> stops = Arrays.asList(getColorModel().getStops());
      Stop s = Collections.min(stops, (i1, i2) -> Double.compare(i1.getOffset(), i2.getOffset()));
      minValue = s.getColor();
    } else {
      minValue = Color.WHITE;
    }
    return minValue;
  }

}
