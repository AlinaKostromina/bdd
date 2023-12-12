package test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import data.DataHelper;
import page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV2() {
        open("http://localhost:9999");

        var loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var transferInfo = DataHelper.getCardTransferInfo("1000");
        var transferPage = dashboardPage.transferToCard(transferInfo.getId1());
        transferPage.addMoneyToCard1(transferInfo);

        var actualBalance1 = dashboardPage.getCardBalance(transferInfo.getId1());
        var expectedBalance1 = 11000;
        Assertions.assertEquals(actualBalance1, expectedBalance1, "Ошибка баланса карты 1");

        var actualBalance2 = dashboardPage.getCardBalance(transferInfo.getId2());
        var expectedBalance2 = 9000;
        Assertions.assertEquals(actualBalance2, expectedBalance2, "Ошибка баланса карты 2");

        dashboardPage.transferToCard(transferInfo.getId1()).cleanUp();
        dashboardPage.transferToCard(transferInfo.getId2()).addMoneyToCard2(transferInfo);
        var actualBalance3 = dashboardPage.getCardBalance(transferInfo.getId1());
        var expectedBalance3 = 10000;
        Assertions.assertEquals(actualBalance3, expectedBalance3, "Ошибка баланса карты 1");

        var actualBalance4 = dashboardPage.getCardBalance(transferInfo.getId1());
        var expectedBalance4 = 10000;
        Assertions.assertEquals(actualBalance4, expectedBalance4, "Ошибка баланса карты 2");
    }
}
