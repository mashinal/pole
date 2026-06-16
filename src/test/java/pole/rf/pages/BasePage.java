package pole.rf.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class BasePage {

    public static final String BASE_URL = "https://xn--e1alid.xn--p1ai";

    private final SelenideElement acceptCookiesButton = $x("//button[text()='Ок']").as("Кнопка принятия cookie");
    private final SelenideElement regionYesButton = $x("//button[text()='Да']").as("Кнопка подтверждения региона");

    public void closePopupsIfVisible() {
        if (regionYesButton.is(visible, Duration.ofSeconds(3))) {
            regionYesButton.click();
        }
        if (acceptCookiesButton.is(visible, Duration.ofSeconds(3))) {
            acceptCookiesButton.click();
        }
    }

}
