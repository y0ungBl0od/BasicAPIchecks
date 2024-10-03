package web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Главная страница
 */
public class PageMain {
    public final SelenideElement searchButton = $x("//form");

    public void clickOnSearch (){
        searchButton.click();
    }

    public void openSite(String url){
        Selenide.open(url);
    }


}
