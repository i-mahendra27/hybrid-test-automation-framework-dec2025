package steps.ui;

import helpers.DataFakerHelper;
import helpers.ExcelHelper;
import helpers.PasswordHelper;
import hooks.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import pages.RegisterPage;
import pages.dto.RegisterFormDataObject;

public class StepsRegisterUser {
    private static final String REGISTERED_USER_EXCEL_PATH = "data/DataUser.xlsx";
    private static final String REGISTERED_USER_SHEET = "RegisteredUser";

    private final TestContext testContext;
    private final RegisterPage registerPage;
    private final ExcelHelper excelHelper;

    private static final String USER_EMAIL = DataFakerHelper.getFaker().internet().emailAddress();
    private static final String USER_PASSWORD = PasswordHelper.generateValidPassword();

    public StepsRegisterUser(TestContext testContext) {
        this.testContext = testContext;
        this.registerPage = new RegisterPage();
        this.excelHelper = new ExcelHelper();
    }

    @And("I fill in the registration form with valid details")
    public void fillRegistrationFormWithValidDetails() {
        RegisterFormDataObject credentialsData = RegisterFormDataObject.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .build();

        registerPage.setRegisterAccount(credentialsData);

        excelHelper.appendRegisteredUserCredentials(
                REGISTERED_USER_EXCEL_PATH,
                REGISTERED_USER_SHEET,
                USER_EMAIL,
                USER_PASSWORD
        );
    }

    @Then("I verify that registered user email is visible")
    public void iVerifyThatRegisteredUserEmailIsVisible() {
        String actualText = registerPage.getSuccessRegistered();
        registerPage.verifyEquals(actualText, USER_EMAIL,
                "Success login label is not showing the registered user email.");
    }
}
