package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.RegisterPage;
import pages.dto.RegisterFormDataObject;

/**
 * Register-specific steps. Page-specific actions that belong to RegisterPage.
 */
public class RegisterSteps {

    private final TestContext testContext;
    private final RegisterPage registerPage;

    public RegisterSteps(TestContext testContext) {
        this.testContext = testContext;
        this.registerPage = new RegisterPage();
    }

    @And("I fill in the registration form with valid data")
    public void iFillInTheRegistrationFormWithValidData() {
        RegisterFormDataObject registerData = testContext.auth().getRegisterData();
        registerPage.setRegisterAccount(registerData);
    }

    @Then("I should see registration success message")
    public void iShouldSeeRegistrationSuccessMessage() {
        registerPage.verifyEquals(
                registerPage.getSuccessRegistered(),
                "Sign in successful",
                "Registration success message mismatch"
        );
    }

    @Then("I should see registration error message")
    public void iShouldSeeRegistrationErrorMessage() {
        registerPage.verifyTrue(
                !registerPage.getErrorToastMessage().isEmpty(),
                "Expected error message but none found"
        );
    }

    @Given("I am on the registration page")
    public void iAmOnTheRegistrationPage() {
        steps.ButtonDispatcher.clickButton("register");
    }
}
