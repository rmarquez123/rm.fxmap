package com.rm.fxmap._00.ignore;

import com.rm.fxmap.annotations.FxMap;
import com.rm.springjavafx.annotations.FxController;
import com.rm.springjavafx.annotations.childnodes.ChildNode;
import com.rm.springjavafx.annotations.childnodes.NodeBind;
import javafx.scene.control.CheckBox;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@FxController(fxml = "fxml/Main.fxml")
@FxMap(id = "mymap", nodeId = "mapstackpane")
public class MyMap implements InitializingBean {
  
  @ChildNode(id = "visibileCheckbox")
  @NodeBind.CheckBox(selectedId = "pointLayerVisibility")
  private CheckBox visibileCheckbox;
  
  /**
   * 
   * @throws Exception 
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    
  }

}
