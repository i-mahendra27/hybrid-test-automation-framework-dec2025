package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.LoginPage;
import pages.dto.CredentialsDataObject;

public class StepsLoginWithUnregisterCredentials {
    private final TestContext testContext;
    private final LoginPage loginPage;

    public StepsLoginWithUnregisterCredentials(TestContext testContext) {
        this.testContext = testContext;
        this.loginPage = new LoginPage();
    }

    @When("I enter unregister email address and password")
    public void enterIncorrectEmailAndPassword() {
        CredentialsDataObject credentialsData = CredentialsDataObject.builder()
                .userEmail("unregister@mail.com")
                .userPassword("Test@123")
                .build();

        loginPage.loginAccount(credentialsData);
    }

    @Then("I verify error {string} is visible")
    public void verifyErrorMessageIsVisible(String expectedError) {
        String actualError = loginPage.getErrorToastMessage();
        loginPage.verifyEquals(expectedError, actualError, "Error message does not match expected value.");
    }
}
