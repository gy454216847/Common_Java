package core;

import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author YGan
 */
public class CommonApp {
    static AndroidDriver driver;
    static Logger logger = Logger.getLogger(CommonApp.class.getName());
    static int timeout = Integer.parseInt(GlobalSettings.timeout);
    static String curPath= System.getProperty("user.dir");









    public AndroidDriver startApp() throws FileNotFoundException, MalformedURLException {


        Yaml yaml = new Yaml();
        File yamlFile = new File("src/main/java/config/360_rent_APK_caps.yaml");
        Map<String, String> data = (Map<String, String>) yaml.load(new FileInputStream(yamlFile));
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", data.get("platformName"));
        desiredCapabilities.setCapability("platformVersion", data.get("platformVersion"));
        desiredCapabilities.setCapability("deviceName", data.get("deviceName"));
        desiredCapabilities.setCapability("appname", data.get("appname"));
        desiredCapabilities.setCapability("app", data.get("app"));
        desiredCapabilities.setCapability("noReset", data.get("noReset"));
        desiredCapabilities.setCapability("unicodeKeyboard", data.get("unicodeKeyboard"));
        desiredCapabilities.setCapability("resetKeyboard", data.get("resetKeyboard"));
        desiredCapabilities.setCapability("appPackage", data.get("appPackage"));
        desiredCapabilities.setCapability("appActivity", data.get("appActivity"));
        driver =
                new AndroidDriver(
                        new URL("http://" + data.get("ip") + ":" + data.get("port") + "/wd/hub"),
                        desiredCapabilities);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }


    public WebElement getElement(String xpath) throws knifeException{
        if (!xpath.contains("=>")) {
            throw new knifeException("Positioning syntax errors, lack of '=>'.");
        }

        String by = xpath.split("=>")[0];
        String value = xpath.split("=>")[1];

        if (by.equals("id")) {
            return driver.findElement(By.id(value));
        } else if (by.equals("name")) {
            return driver.findElement(By.name(value));
        } else if (by.equals("class")) {
            return driver.findElement(By.className(value));
        } else if (by.equals("linkText")) {
            return driver.findElement(By.linkText(value));
        } else if (by.equals("xpath")) {
            return driver.findElement(By.xpath(value));
        } else if (by.equals("css")) {
            return driver.findElement(By.cssSelector(value));
        } else {
            throw new knifeException("Please enter the correct targeting elements,'id','name','class','xpath','css'.");
        }
    }


    public void waitElement(String xpath, int second) throws knifeException {

        if (!xpath.contains("=>")) {
            throw new knifeException("Positioning syntax errors, lack of '=>'.");
        }

        String by = xpath.split("=>")[0];
        String value = xpath.split("=>")[1];
        By findelement = null;

        if (by.equals("id")) {
            findelement = By.id(value);
        } else if (by.equals("name")) {
            findelement = By.name(value);
        } else if (by.equals("class")) {
            findelement = By.className(value);
        } else if (by.equals("linkText")) {
            findelement = By.linkText(value);
        } else if (by.equals("xpath")) {
            findelement = By.xpath(value);
        } else if (by.equals("css")) {
            findelement = By.cssSelector(value);
        } else {
            throw new knifeException("Please enter the correct targeting elements,'id','name','class','xpath','css'.");
        }
        try{
            new WebDriverWait(driver, second).until(ExpectedConditions
                    .presenceOfElementLocated(findelement));
        }catch (Exception e){
            e.printStackTrace();
            logger.error(String.format("Can not find the element within %d",second));
            get_screenshot(String.format("Can not find the element within %d",second));

        }
    }
    public void get_screenshot(String filename){
        File srcfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            logger.info("Get screenshot");
            FileUtils.copyFile(srcfile, new File(curPath +"/src/test/java/screenshots/" + filename + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Fail to get screenshot");
        }
    }
    public  void click(String xpath) throws knifeException {

        try {
            waitElement(xpath, timeout);
            getElement(xpath).click();
            logger.info("Click the element");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Fail to click the element");
            get_screenshot("Fail to click the element");

        }

    }
    public  void type(String xpath, String text) throws knifeException {



        try {
            waitElement(xpath, timeout);
            WebElement element = getElement(xpath);
            element.clear();
            element.click();
            element.sendKeys(text);
            logger.info(String.format("Type %s in the element",text));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Fail to type %s in the element",text));
            get_screenshot(String.format("Fail to type %s in the element",text));
        }


    }
    public  void closeApp() {
        try{
            driver.closeApp();
            logger.info("Close the App");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Fail to close the app");
            get_screenshot("Fail to close the app");
        }
    }
    public  void getText(String xpath) throws knifeException {
        try{
            WebElement element = getElement(xpath);
            element.getText();
            logger.info(String.format("The elements' text is : %s",element.getText()));

        }catch (Exception e){
            logger.error("Fail to get the elements' text");
        }

    }


    public boolean isElementDisplayed(String xpath) throws knifeException{
        logger.info("Check whether the element is displayed");
        try{

            assert getElement(xpath).isDisplayed();
            System.out.println("The element is displayed");
            return true;

        }catch (AssertionError error){
            System.out.println("The element is not displayed");
            System.out.println(error.getMessage());
            return false;
        }




    }
    public void waitPage(){
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    public void back(){
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
    }


    public void swipe(String direction) throws knifeException{
        Dimension size = driver.manage().window().getSize();
        int height = size.height;
        int width = size.width;
        if (direction.toLowerCase().equals( "up")) {
            new TouchAction(driver).longPress(PointOption.point(width / 2, 100))
                    .moveTo(PointOption.point(width / 2, height - 100)).release()
                    .perform();
            logger.info("Swipe up");
        }
        if (direction.toLowerCase().equals("down")){
            new TouchAction(driver)
                    .longPress(PointOption.point(width / 2, height - 100))
                    .moveTo(PointOption.point(width / 2, 100)).release().perform();
            logger.info("Swipe down");

        }
        if (direction.toLowerCase().equals("left")){
            new TouchAction(driver)
                    .longPress(PointOption.point(width - 100, height / 2))
                    .moveTo(PointOption.point(100, height / 2)).release().perform();
            logger.info("Swipe left");

        }
        if (direction.toLowerCase().equals("right")){
            new TouchAction(driver).longPress(PointOption.point(100, height / 2))
                    .moveTo(PointOption.point(width - 100, height / 2)).release()
                    .perform();
            logger.info("Swipe right");

        }
        else{
            logger.error("Can not swipe");
            get_screenshot("Can not swipe");
        }
    }

    public void openNotification(){
        try{
        driver.openNotifications();
        logger.info("Open Notifications");
    }catch (Exception e){
        logger.error("Fail to open notifications");
        get_screenshot("Fail to open notifications");}
    }

    public void lopngPress(String xpath) throws knifeException {
        try{
            TouchAction action = new TouchAction(driver);
            action.longPress((LongPressOptions) getElement(xpath)).release().perform();
            logger.info("Long press the element");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Fail to long press the element");
        }
    }

    public void getCurrentActivity(){
        try{
            driver.currentActivity();
            logger.info(String.format("Current activity is : %s",driver.currentActivity()));
        }catch (Exception e){
            logger.error("Can not get current activity");
            get_screenshot("Can not get current activity");
        }
    }

    public void getCurrentPacakage(){
        try{
            driver.getCurrentPackage();
            logger.info(String.format("Current package is :%s",driver.getCurrentPackage()));
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Fail to get current package");
            get_screenshot("Fail to get current package");
        }
    }

    public String getCurrentTime(){
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        return now.format(new Date());
    }

    public void tap(int x,int y){
        try{
            TouchAction action = new TouchAction(driver);
            action.tap(PointOption.point(x, y)).release().perform();
            logger.info(String.format("Tap the location : {%d, %d}",x,y));
    }catch (Exception e){
        e.printStackTrace();
        logger.error(String.format("Fail to tap the location : {%d, %d}",x,y));}
    }

    public void zoom(){
        try{
            int Y = driver.manage().window().getSize().getHeight();
            int X = driver.manage().window().getSize().getWidth();
            MultiTouchAction multiTouchAction = new MultiTouchAction(driver);
            TouchAction touchAction1 = new TouchAction(driver);
            TouchAction touchAction2 = new TouchAction(driver);
            touchAction1.press(PointOption.point(X*4/10,Y*4/10)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(X*2/10,Y*2/10)).release();
            touchAction2.press(PointOption.point(X*6/10,Y*6/10)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(X*2/10,Y*2/10)).release();
            multiTouchAction.add(touchAction1).add(touchAction2);
            multiTouchAction.perform();
            logger.info("Zoom the mobile");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Fail to zoom the mobile");
            get_screenshot("Fail to zoom the mobile");
        }
    }

    public void pinch(){
        try{
            int Y = driver.manage().window().getSize().getHeight();
            int X = driver.manage().window().getSize().getWidth();
            MultiTouchAction multiTouchAction = new MultiTouchAction(driver);
            TouchAction touchAction1 = new TouchAction(driver);
            TouchAction touchAction2 = new TouchAction(driver);
            touchAction1.press(PointOption.point(X*2/10,Y*2/10)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(X*4/10,Y*4/10)).release();
            touchAction2.press(PointOption.point(X*2/10,Y*2/10)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(X*6/10,Y*6/10)).release();
            multiTouchAction.add(touchAction1).add(touchAction2);
            multiTouchAction.perform();
            logger.info("Pinch the mobile");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Fail to pinch the mobile");
            get_screenshot("Fail to pinch the mobile");
        }
    }




}

