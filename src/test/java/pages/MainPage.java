package pages;

import core.CommonWeb;

public class MainPage {
    public static String inputBox = "id=>k";
    public static String submitButton = "id=>su";
    public static CommonWeb driver;
    public static String url;

    public static void search(String text){
        driver.type(inputBox,text);
        driver.click(submitButton);

    }
}
