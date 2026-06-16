package pole.rf.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class HomePage extends BasePage {

    private final SelenideElement harvestMenu = $x("//li//span[text()='Урожай']").as("Пункт верхнего меню 'Урожай'");

    @Step("Принять cookie и подтвердить регион, если отображаются баннеры")
    public HomePage acceptCookiesIfVisible() {
        closePopupsIfVisible();
        return this;
    }

    @Step("Перейти через верхнее меню во вкладку 'Урожай'")
    public HarvestPage openHarvestPage() {
        harvestMenu.shouldBe(visible).click();
        return new HarvestPage();
    }
}
