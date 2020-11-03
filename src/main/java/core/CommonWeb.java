package core;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.yaml.snakeyaml.Yaml;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


/**
 * @author YGan
 */
public class CommonWeb {
    public static WebDriver browser;

    static Logger logger = Logger.getLogger(CommonWeb.class.getName());

    static String curPath= System.getProperty("user.dir");

    int timeout = Integer.parseInt(GlobalSettings.timeout);




    public CommonWeb() throws knifeException {
        int browserType = GlobalSettings.browserType;
        if (browserType == 1) {
            browser = new FirefoxDriver();
        } else if (browserType == 2) {
            String newCurPath = curPath.replace("\\","\\\\");
            System.setProperty("webdriver.chrome,driver", "\\src\\main\\java\\driver\\chromedriver.exe");
            browser = new ChromeDriver();
        } else if (browserType == 3) {
            browser = new InternetExplorerDriver();
        } else if (browserType == 4) {
            browser = new EdgeDriver();
        } else if (browserType == 5) {
            browser = new OperaDriver();
        } else if (browserType == 6) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless");
            browser = new ChromeDriver(chromeOptions);
        } else {

            throw new knifeException("Positioning syntax errors, lack of '=>'.");
        }

    }

    public WebDriver getDriver() {
        return browser;
    }

    public WebElement getElement(String xpath) throws knifeException {

        if (!xpath.contains("=>")) {
            throw new knifeException("Positioning syntax errors, lack of '=>'.");
        }

        String by = xpath.split("=>")[0].toLowerCase();
        String value = xpath.split("=>")[1];

        if (by.equals("id")) {
            return browser.findElement(By.id(value));
        } else if (by.equals("name")) {
            return browser.findElement(By.name(value));
        } else if (by.equals("class") || by.equals("classname")) {
            return browser.findElement(By.className(value));
        } else if (by.equals("linkText")) {
            return browser.findElement(By.linkText(value));
        } else if (by.equals("xpath")) {
            return browser.findElement(By.xpath(value));
        } else if (by.equals("css") || by.equals("cssselector")) {
            return browser.findElement(By.cssSelector(value));
        } else {
            throw new knifeException("Please enter the correct targeting elements,'id','name','class','xpath','css'.");
        }


    }

    public void waitElement(String xpath,int seconds) throws knifeException{

        if (!xpath.contains("=>")){
            throw new knifeException("Positioning syntax errors, lack of '=>'.");
    }
        String by = xpath.split("=>")[0].toLowerCase();
        String value = xpath.split("=>")[1];


        if (by.equals("id")){
            WebDriverWait wait = new WebDriverWait(browser,seconds);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(value)));
        } else if (by.equals("name")){
            WebDriverWait wait = new WebDriverWait(browser,seconds);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name(value)));
        } else if (by.equals("class") || by.equals("classname")){
            WebDriverWait wait = new WebDriverWait(browser,seconds);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className(value)));
        } else if (by.equals("linkText") || by.equals("link")){
            WebDriverWait wait = new WebDriverWait(browser,seconds);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(value)));
        } else if (by.equals("xpath")){
            WebDriverWait wait = new WebDriverWait(browser,seconds);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(value)));
        } else if (by.equals("css") || by.equals("cssselector")){
            WebDriverWait wait = new WebDriverWait(browser,seconds);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(value)));

        }
        else{
            logger.error(String.format("Can not find the element within %d seconds", seconds));
//            get_screenshot(String.format("Can not find the element within %d",seconds));
        }
        }

    public void get_screenshot(String filename){
        File srcfile = ((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE);
        try {
            logger.info("Get screenshot");
            FileUtils.copyFile(srcfile, new File(curPath +"/src/test/java/screenshots/" + filename + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Fail to get screenshot");
        }

    }

    public void open(String url){
        try{
            logger.info("Open Browser");
            browser.get(url);
        } catch (Exception e){
            logger.error("Fail to open Browser");
            e.printStackTrace();
        }


}
    public void setWindow(int wide, int high){

        try{
            logger.info(String.format("Set Window's wide is:%d,high is: %d",wide,high));
            browser.manage().window().setSize(new Dimension(wide,high));
        }catch (Exception e){
            logger.error(String.format("Fail to set window's wide is:%d,high is:%d",wide,high));
            e.printStackTrace();
            get_screenshot(String.format("Fail to set window's wide is:%d,high is:%d",wide,high));
            }
    }

    public void maxWindow(){
        try{
            logger.info("Set max window");
            browser.manage().window().maximize();
        }catch (Exception e){
            logger.error("Fail to set max window");
            e.printStackTrace();
        }

    }

    public void close(){
        try{
            logger.info("Close the window");
            browser.close();
        }catch (Exception e){
            logger.error("Fail to close the window");
            e.printStackTrace();
        }
    }

    public void quit(){
        try{
            logger.info("Quit the browser");
            browser.quit();
        }catch (Exception e){
            logger.error("Fail to quit browser");
            e.printStackTrace();
        }
    }

    public void click(String xpath){
        try{
            logger.info("Click element");
            waitElement(xpath,timeout);
            getElement(xpath).click();

        }catch (Exception e){
            logger.error("Fail to element");
            e.printStackTrace();
            get_screenshot("Fail to element");
        }

    }
    
    public void doubleClick(String xpath) throws knifeException {
        try{
            logger.info("Double click the element");
            waitElement(xpath,timeout);
            Actions actions = new Actions(browser);
            actions.doubleClick(getElement(xpath)).perform();
            
        }catch (Exception e){
            logger.error("Fail to double click the element");
            e.printStackTrace();
            get_screenshot("Fail to double click the element");
        }
    }

    public void type(String xpath,String text){

            logger.info(String.format("Type the element with: %s",text));
            try{
            waitElement(xpath,timeout);
            getElement(xpath).click();
            getElement(xpath).clear();
            getElement(xpath).sendKeys(text);
}catch (knifeException e){
                e.printStackTrace();
            }

    }

    public void clear(String xpath) throws knifeException {
        try{
            logger.info("Clear the element");
            getElement(xpath).clear();

        }catch (Exception e){
            e.printStackTrace();
            logger.error("Fail to clear the element");
            get_screenshot("Fail to clear the element");
        }
    }
    
    public void rightClick(String xpath) throws knifeException {
        try{

            logger.info("Right click the element");
            waitElement(xpath,timeout);
            Actions actions = new Actions(browser);
            actions.contextClick(getElement(xpath)).perform();
        }catch (Exception e){
            logger.error("Fail to right click the element");
            e.printStackTrace();
            get_screenshot("Fail to right click the element");
        }
    }

    public void clickAndHold(String xpath) throws knifeException {
        try{
            logger.info("Click and Hold the element");
            waitElement(xpath,timeout);
            Actions actions = new Actions(browser);
            actions.clickAndHold(getElement(xpath)).perform();
        }catch (Exception e){
            logger.error("Fail to click and hold the element");
            e.printStackTrace();
            get_screenshot("Fail to click and hold the element");
        }
    }

    public void dragAndDrop(String el_xpath, String ta_xpath) throws knifeException {
        try{
            logger.info("Drag and Drop");
            waitElement(el_xpath,timeout);
            waitElement(ta_xpath,timeout);

            Actions actions = new Actions(browser);
            actions.dragAndDrop(getElement(el_xpath),getElement(ta_xpath)).perform();
        }catch (Exception e){
            logger.error("Fail to drag and drop");
            e.printStackTrace();
            get_screenshot("Fail to drag and drop");
        }
    }

    public void clickLink(String text){
        try{

            logger.info(String.format("Click the link : %s",text));
            browser.findElement(By.partialLinkText(text)).click();

        }catch (Exception e){
            logger.error(String.format("Fail to click the link : %s",text));
            e.printStackTrace();
            get_screenshot(String.format("Fail to click the link : %s",text));
        }
    }

    public void selectValue(String xpath,String value) throws knifeException {
        try{
            logger.info(String.format("Select by value: %s",value));
            waitElement(xpath,timeout);
            Select select = new Select(getElement(xpath));
            select.selectByValue(value);
            }catch (Exception e){
            logger.error(String.format("Fail to select by value : %s",value));
            e.printStackTrace();
            get_screenshot(String.format("Fail to select by value : %s",value));
        }

    }

    public void refresh(){
        try{
            logger.info("Fresh website");
            browser.navigate().refresh();
        }catch (Exception e){
            logger.error("Fail to fresh website");
            e.printStackTrace();
            get_screenshot("Fail to fresh website");
        }
    }

    public void enterFrame(String xpath) throws knifeException{
        try{
             logger.info("enter to frame");
             waitElement(xpath,timeout);
             browser.switchTo().frame(getElement(xpath));
            }catch (Exception e){
            e.printStackTrace();
            logger.error("Fail to enter to frame");
            get_screenshot("Fail to enter to frame");
        }
    }
    public void leaveFrame(){
        try{
            logger.info("leave frame");
            browser.switchTo().defaultContent();

        }catch (Exception e){
            e.printStackTrace();
            logger.error("Fail to leave frame");
            get_screenshot("Fail to leave frame");
        }
    }

    public  void getText(String xpath) throws knifeException {

        try{
            waitElement(xpath,timeout);
            WebElement element = getElement(xpath);
             String text = element.getText();
             logger.info(String.format("The elements' text is : %s", text));
        }catch (Exception e){
             e.printStackTrace();
             logger.error("Fail to get the elements' text");
        }

    }
    public void waitPage(){
        browser.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }


    public  String getTitle(){
            waitPage();
            return browser.getTitle();

    }

    public void getCurrentUrl(){
        try{
            waitPage();
            browser.getCurrentUrl();
            logger.info(String.format("The windows' url is : %s",browser.getCurrentUrl()));
        }catch (Exception e){
            logger.error("Fail to get the windows' url");
            get_screenshot("Fail to get the windows' url");
        }
    }

    public String getAttribute(String xpath, String attribute) throws knifeException {
        return getElement(xpath).getAttribute(attribute);
    }

    public void acceptAlter(){
        try{
            logger.info("Accept the alter");
            browser.switchTo().alert().accept();
        }catch (Exception e){
            logger.error("Fail to accept the alter");
            e.printStackTrace();
            get_screenshot("Fail to accept the alter");
        }
    }

    public void dismissAlter(){
        try{
            logger.info("Dismiss the alter");
            browser.switchTo().alert().dismiss();
    }catch (Exception e){
            logger.error("Fail to dismiss the alter");
            e.printStackTrace();
            get_screenshot("Fail to dismiss the alter");
        }
    }

    public void isElementExist(String xpath) throws knifeException {
        logger.info("Whether the element is existed");
        try{
            waitElement(xpath,timeout);
            System.out.println("The element is existed");
        }catch (TimeoutException e){
            e.printStackTrace();
            System.out.println("The element is not existed");
        }
    }

    public String getCurrentTime(){
        DateFormat dateformat= new SimpleDateFormat("yyyyMMdd_HHmmss");
        //创建一个data format对象
        Date date = new Date();
        //利用Date()获取当前时间
        String date1 = dateformat.format(date);
        return date1;
    }



    public void sendMail() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        File emailYamlFile = new File("src/main/java/config/email.yaml ");
        final Map<String, String> dataEmail = (Map<String, String>) yaml.load(new FileInputStream(emailYamlFile));
        // 创建一个Property文件对象
        Properties props = new Properties();

        // 设置邮件服务器的信息，这里设置smtp主机名称
        props.put("mail.smtp.host", "smtp.qq.com");

        // 设置socket factory 的端口
        props.put("mail.smtp.socketFactory.port", "465");

        // 设置socket factory
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

        // 设置需要身份验证
        props.put("mail.smtp.auth", "true");

        // 设置SMTP的端口，QQ的smtp端口是25
        props.put("mail.smtp.port", "25");

        // 身份验证实现
        Session session = Session.getDefaultInstance(props, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 第二个参数，就是我QQ开启smtp的授权码
                return new PasswordAuthentication(dataEmail.get("user"), dataEmail.get("password"));

            }

        });

        try {

            // 创建一个MimeMessage类的实例对象
            Message message = new MimeMessage(session);

            // 设置发件人邮箱地址
            message.setFrom(new InternetAddress(dataEmail.get("user")));

            // 设置收件人邮箱地址
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(dataEmail.get("receives")));

            // 设置邮件主题
            message.setSubject(dataEmail.get("subject"));

            // 创建一个MimeBodyPart的对象，以便添加内容
            BodyPart messageBodyPart1 = new MimeBodyPart();

            // 设置邮件正文内容
            messageBodyPart1.setText(dataEmail.get("body_text"));

            // 创建另外一个MimeBodyPart对象，以便添加其他内容
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            // 设置邮件中附件文件的路径
            String filename = "src/test/java/reports/" + dataEmail.get("filename") + ".html";

            // 创建一个datasource对象，并传递文件
            DataSource source = new FileDataSource(filename);

            // 设置handler
            messageBodyPart2.setDataHandler(new DataHandler(source));

            // 加载文件
            messageBodyPart2.setFileName(filename);

            // 创建一个MimeMultipart类的实例对象
            Multipart multipart = new MimeMultipart();

            // 添加正文1内容
            multipart.addBodyPart(messageBodyPart1);

            // 添加正文2内容
            multipart.addBodyPart(messageBodyPart2);

            // 设置内容
            message.setContent(multipart);

            // 最终发送邮件
            Transport.send(message);

            System.out.println("=====Send email=====");

        } catch (MessagingException e) {

            throw new RuntimeException(e);






        }




}
}