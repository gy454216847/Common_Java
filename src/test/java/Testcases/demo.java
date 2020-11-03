package Testcases;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.Myunit;

public class demo extends Myunit {
  static Logger logger = Logger.getLogger(demo.class);

  @Test
  public void demo1() {
    driver.type(inputBox,"Java");





  }
}
