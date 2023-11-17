package com.rm.fxmap;

import com.rm.panzoomcanvas.FxCanvas;
import com.rm.springjavafx.FxmlInitializer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 *
 * @author rmarquez
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/main.xml"})
public class CreateMapIT {

  @Autowired
  ApplicationContext appContext;
  @Autowired
  FxmlInitializer fxmlInitializer;
  
  @Autowired
  FxCanvas map;

  @BeforeClass
  public static void setUp() throws InterruptedException {
    JavaFxTest.initToolkit();
  }

  @Test
  public void test1() {
    System.out.println("testing");
    this.fxmlInitializer.load(this.appContext);
    Assert.assertNotNull("map not null", this.map);
    Assert.assertEquals("number of layers", 3, this.map.getContent().getLayers().size());
    Assert.assertNotNull("Parent is set", this.map.parentProperty().getValue());
    Assert.assertEquals("Parent is set", "mapstackpane",this.map.parentProperty().getValue().getId());
    
    System.out.println("testing");
  }
}
