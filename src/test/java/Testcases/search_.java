package Testcases;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.Myunit;

import static org.testng.Assert.assertTrue;

public class search_ extends Myunit {
    @DataProvider(name = "Search")
    public Object[][] Keys(){
        return new Object[][]{
                {"Java"}
        };
    }

    @Test(dataProvider = "Search")
    public void TestSearch(String text) throws InterruptedException {
        search(text);
        Thread.sleep(3000);
        String title = driver.getTitle();
        assertTrue(title.contains(text));
    }

}
