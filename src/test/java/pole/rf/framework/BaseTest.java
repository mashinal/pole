package pole.rf.framework;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.open;
import static pole.rf.pages.BasePage.BASE_URL;

public class BaseTest {

    @BeforeEach
    public void setUp() {

        WebDriverManager.chromedriver().setup();

        Configuration.browser = "chrome";
        Configuration.browserSize = "1600x900";
        Configuration.timeout = 15000;
        Configuration.pageLoadTimeout = 60000;
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        Configuration.screenshots = true;
        Configuration.savePageSource = false;

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--lang=ru-RU",
                "--start-maximized",
                "--disable-notifications",
                "--disable-popup-blocking",
                "--no-sandbox"
        );

        if (Configuration.headless) {
            options.addArguments("--headless=new", "--window-size=1600,900");
        }

        Configuration.browserCapabilities = options;
        open(BASE_URL);
    }

    @AfterEach
    public void closeDriver() {
        Selenide.closeWebDriver();
    }
}
