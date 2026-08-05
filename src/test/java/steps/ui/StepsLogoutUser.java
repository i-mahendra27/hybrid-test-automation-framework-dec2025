package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.Then;
import managers.ConfigManager;
import pages.LoginPage;

public class StepsLogoutUser {
    private final TestContext testContext;
    private final LoginPage loginPage;

    public StepsLogoutUser(TestContext testContext) {
        this.testContext = testContext;
        this.loginPage = new LoginPage();
    }

    @Then("I should be redirected to the login page")
    public void verifyUserRedirectedToLoginPage() {
        String actualUrl = ConfigManager.getBaseUrl();
        String expectedUrl = loginPage.getCurrentUrl();

        loginPage.verifyEquals(actualUrl, expectedUrl,
                "User is NOT redirected to Login Page");
    }
}
