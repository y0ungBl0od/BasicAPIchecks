package web;

import com.codeborne.selenide.Selenide;
import org.junit.Test;
import org.openqa.selenium.devtools.v85.page.Page;

public class AppleTest extends BaseClass{
    private static final String BASE_URL = "https://appleinsider.ru/";
    private static final String searchString = "Чем iPhone 13 Отличается от iPhone 12";

    @Test
    public void checkHref(){
        PageMain mainpage = new PageMain();
        mainpage.openSite(BASE_URL);

        int x = 0;
    }

}
