package pole.rf.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pole.rf.framework.BaseTest;
import pole.rf.pages.HarvestPage;
import pole.rf.pages.HomePage;

public class HarvestSmokeTest extends BaseTest {

    @Test
    @DisplayName("Проверка открытия раздела 'Урожай'")
    public void checkHarvestPageOpen() {
        new HomePage()
                .acceptCookiesIfVisible()
                .openHarvestPage()
                .checkPageOpened();
        new HarvestPage()
                .checkMainElements();
    }

    @Test
    @DisplayName("Проверка фильтрации по типу 'Покупка'")
    public void checkTypeFilter() {
        new HomePage()
                .acceptCookiesIfVisible()
                .openHarvestPage();
        new HarvestPage()
                .setBuyTypeFilter()
                .applyFilters()
                .checkCardsHaveBuyType()
                .resetFilters()
                .checkFiltersReset()
                .checkCardsVisible();
    }

    @Test
    @DisplayName("Проверка поиска по номеру карточки")
    public void checkSearchByCardNumber() {

        new HomePage()
                .acceptCookiesIfVisible()
                .openHarvestPage();

        HarvestPage harvestPage = new HarvestPage();

        harvestPage.checkCardsVisible();

        String cardNumber = harvestPage.getFirstCardNumber();

        harvestPage
                .searchByNumber(cardNumber)
                .checkCardWithNumberVisible(cardNumber);
    }

    @Test
    @DisplayName("Проверка карточки предложения")
    public void checkOfferCard() {
        new HomePage()
                .acceptCookiesIfVisible()
                .openHarvestPage();
        new HarvestPage()
                .checkFirstCardRequiredFields();
    }
}
