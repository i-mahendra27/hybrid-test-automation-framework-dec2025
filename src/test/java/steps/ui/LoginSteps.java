package steps;

import hooks.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import pages.dto.CredentialsDataObject;

/**
 * Login-specific steps. Page-specific actions that belong to LoginPage.
 */
public class LoginSteps {

    private final TestContext testContext;
    private final LoginPage loginPage;

    public LoginSteps(TestContext testContext) {
        this.testContext = testContext;
        this.loginPage = new LoginPage();
    }

    @And("I login with registered credentials")
    public void iLoginWithRegisteredCredentials() {
        CredentialsDataObject credentials = testContext.auth().getCredentials();
        loginPage.loginAccount(credentials);
    }

    @Then("I should see login success message")
    public void iShouldSeeLoginSuccessMessage() {
        loginPage.verifyEquals(
                loginPage.getSuccessLogin(),
                "Sign in successful",
                "Login success message mismatch"
        );
    }

    @Then("I should see login error message")
    public void iShouldSeeLoginErrorMessage() {
        loginPage.verifyTrue(
                !loginPage.getErrorToastMessage().isEmpty(),
                "Expected error message but none found"
        );
    }

    @Given("I am logged in")
    public void iAmLoggedIn() {
        CredentialsDataObject credentials = testContext.auth().getCredentials();
        loginPage.loginAccount(credentials);
        ButtonDispatcher.clickButton("sign in");
        loginPage.sleep(0.5);
    }
}
