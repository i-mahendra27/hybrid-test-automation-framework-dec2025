package steps.ui;

import helpers.ExcelHelper;
import hooks.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.RegisterPage;
import pages.dto.RegisterFormDataObject;

public class StepsRegisterUserWithExistingEmail {
    private final TestContext testContext;
    private final RegisterPage registerPage;

    private String REGISTERED_EMAIL = "";
    private String REGISTERED_PASSWORD = "";

    private void loadRegisteredUserData() {
        Object[][] data = ExcelHelper.getExcelData("data/DataUser.xlsx", "RegisteredUser");
        int lastIndex = data.length - 1;
        REGISTERED_EMAIL = data[lastIndex][0].toString();
        REGISTERED_PASSWORD = data[lastIndex][1].toString();
    }

    public StepsRegisterUserWithExistingEmail(TestContext testContext) {
        this.testContext = testContext;
        this.registerPage = new RegisterPage();
    }

    @And("I already have a registered user")
    public void alreadyHaveRegisteredUser() {
        loadRegisteredUserData();
    }

    @When("I register with an existing email")
    public void registerWithExistingEmail() {
        RegisterFormDataObject credentialsData = RegisterFormDataObject.builder()
                .email(REGISTERED_EMAIL)
                .password(REGISTERED_PASSWORD)
                .confirmPassword(REGISTERED_PASSWORD)
                .build();

        registerPage.setRegisterAccount(credentialsData);
    }

    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String expectedMessage) {
        String actualResult = registerPage.getErrorToastMessage();
        registerPage.verifyEquals(actualResult, expectedMessage);
    }
}
