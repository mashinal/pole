package pole.rf.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class HarvestPage extends BasePage {

    private static final Pattern CARD_NUMBER = Pattern.compile("№\\s*(\\d+)");

    private final SelenideElement title = $x("//h2[text()='Урожай']").as("Заголовок 'Урожай'");
    private final SelenideElement tenderBlock = $x("//p[contains(text(), 'Тендер')]").as("Блок 'Тендер'");
    private final SelenideElement createTenderButton = $x("//button[text()='Создать тендер']").as("Кнопка 'Создать тендер'");
    private final SelenideElement saleRequestBlock = $x("//p[contains(text(), 'Заявка на продажу')]").as("Блок 'Заявка на продажу'");
    private final SelenideElement createRequestButton = $x("//button[text()='Создать заявку']").as("Кнопка 'Создать заявку'");
    private final SelenideElement filtersForm = $x("//form[contains(@class,'filters')]").as("Форма фильтров");
    private final SelenideElement searchInput = $x("//input[@name='searchField']").as("Поле поиска");
    private final SelenideElement applyFiltersButton = $x("//button[text()='Применить']").as("Кнопка 'Применить'");
    private final SelenideElement resetFiltersButton = $x("//button[text()='Сбросить фильтры']").as("Кнопка сброса фильтров");
    private final SelenideElement allFiltersButton = $x("//button[text()='Все фильтры']").as("Кнопка 'Все фильтры'");
    private final SelenideElement typeFilter = $x("//button[@aria-label='Тип']").as("Фильтр 'Тип'");
    private final SelenideElement buyTypeOption = $x("//li[@data-key='TENDER']//span[text()='Покупка']").as("Значение фильтра 'Покупка'");
    private final SelenideElement selectedFilterValue = $x("//span[contains(@class, 'dropdownTextValue')]").as("Выбранное значение фильтра");

    private final ElementsCollection cards = $$x("//div[contains(@class,'offerCard') and @role='button']").as("Карточки предложений или тендеров");

    @Step("Проверить, что открылась страница раздела 'Урожай'")
    public HarvestPage checkPageOpened() {
        title.shouldBe(visible);
        return this;
    }

    @Step("Проверить основные элементы страницы")
    public void checkMainElements() {
        SelenideElement[] elements = {
                title,
                tenderBlock,
                createTenderButton,
                saleRequestBlock,
                createRequestButton,
                filtersForm,
                searchInput,
                applyFiltersButton
        };
        for (SelenideElement element : elements) {
            element.shouldBe(visible);
        }
        cards.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(30));
    }

    @Step("Выбрать фильтр Тип = Покупка")
    public HarvestPage setBuyTypeFilter() {
        openAllFiltersIfNeed();
        typeFilter.shouldBe(visible).click();
        buyTypeOption.shouldBe(visible).click();
        return this;
    }

    @Step("Применить фильтр")
    public HarvestPage applyFilters() {
        applyFiltersButton.shouldBe(visible).click();
        waitUntilCardsLoaded();
        return this;
    }

    @Step("Проверить, что отображаемые карточки имеют тип 'Покупка'")
    public HarvestPage checkCardsHaveBuyType() {
        cards.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(30));
        cards.asDynamicIterable().forEach(card -> card.shouldHave(text("Покупка")));
        return this;
    }

    @Step("Сбросить фильтры")
    public HarvestPage resetFilters() {
        resetFiltersButton.shouldBe(visible).click();
        waitUntilCardsLoaded();
        return this;
    }

    @Step("Проверить, что фильтры сброшены")
    public HarvestPage checkFiltersReset() {
        selectedFilterValue.shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что список карточек отображается")
    public void checkCardsVisible() {
        cards.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(30));
    }

    @Step("Получить номер первой карточки")
    public String getFirstCardNumber() {
        Matcher matcher = CARD_NUMBER.matcher(getFirstCard().getText());
        if (!matcher.find()) {
            throw new AssertionError("Не найден номер первой карточки");
        }
        return matcher.group(1);
    }

    @Step("Ввести номер в поиск: {number}")
    public HarvestPage searchByNumber(String number) {
        searchInput.shouldBe(visible).setValue(number).sendKeys(Keys.ENTER);
        waitUntilCardsLoaded();
        return this;
    }

    @Step("Проверить, что в результатах отображается карточка с номером {number}")
    public void checkCardWithNumberVisible(String number) {
        cards.findBy(text(number)).shouldBe(visible);
    }

    @Step("Проверить обязательные элементы первой карточки")
    public void checkFirstCardRequiredFields() {
        SelenideElement card = getFirstCard();

        String[] texts = {
                "Начальная цена",
                "Последняя ставка",
                "Регион",
                "Адрес поставки/отгрузки",
                "Дата поставки"
        };

        String[] patterns = {
                "(?s).*(Покупка|Продажа).*",
                "(?s).*№\\s*\\d+.*",
                "(?s).*(Активный|Завершен|Завершён).*",
                "(?s).*(Пшеница|Кукуруза|Ячмень|Подсолнечник|Соя|Рапс|Меласса|Жом|Шрот).*",
                "(?s).*(\\d+\\s*т|Объем|Объём).*"
        };

        for (String pattern : patterns) {
            card.shouldHave(matchText(pattern));
        }
        for (String text : texts) {
            card.shouldHave(text(text));
        }
    }

    private SelenideElement getFirstCard() {
        return cards.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(30)).first().shouldBe(visible);
    }

    private void waitUntilCardsLoaded() {
        cards.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(30));
    }

    private void openAllFiltersIfNeed() {
        if (!typeFilter.is(visible) && allFiltersButton.is(visible)) {
            allFiltersButton.click();
        }
    }
}
