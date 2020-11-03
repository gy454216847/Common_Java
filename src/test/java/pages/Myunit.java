package pages;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import core.CommonWeb;
import core.knifeException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

public class Myunit extends MainPage {
    private static ExtentTest test;
    private static ExtentReports extentReports;
    static org.apache.log4j.Logger logger = Logger.getLogger(CommonWeb.class.getName());

    @BeforeMethod
    public void setUp(Method method) throws knifeException {
        url = "https://www.baidu.com";
        driver = new CommonWeb();
        driver.open(url);
    }
    @AfterMethod
    public void tearDown(ITestResult result){
        if (result.getStatus() == ITestResult.FAILURE) {
            driver.get_screenshot("Fail picture");
        }

        driver.quit();
    }

}
