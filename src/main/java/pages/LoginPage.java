package pages;

import base.BasePage;
import org.openqa.selenium.By;
import pages.dto.CredentialsDataObject;

import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import static helpers.PropertiesHelper.loadAllFiles;

public class LoginPage extends BasePage<LoginPage> {

    public LoginPage(){
    }

    Properties properties = loadAllFiles();

    String email = properties.getProperty("EMAIL");
    String password = properties.getProperty("PASSWORD");
    String sigInButton = properties.getProperty("SIGN_IN_BUTTON");
    String successLoginLabel = properties.getProperty("SUCCESS_LOGIN_LABEL");
    String logOutButton = properties.getProperty("LOG_OUT_BUTTON");
    String errorToastMessage = properties.getProperty("ERROR_TOAST_MESSAGE");

    // Dynamic button map - key: button name (lowercase), value: action
    private static final Map<String, Consumer<LoginPage>> BUTTON_ACTIONS = Map.of(
            "sign in", LoginPage::clickSignInButton,
            "login", LoginPage::clickSignInButton,
            "log out", LoginPage::clickLogOutButton,
            "logout", LoginPage::clickLogOutButton
    );

    public static Map<String, Consumer<LoginPage>> getButtonActions() {
        return BUTTON_ACTIONS;
    }

    public void loginAccount(CredentialsDataObject data){
        if (data.getUserEmail() != null)
            setText(By.xpath(email), data.getUserEmail());
        if (data.getUserPassword() != null)
            setText(By.xpath(password), data.getUserPassword());
    }

    public void clickSignInButton(){
        clickElement(By.xpath(sigInButton));
    }

    public String getSuccessLogin(){
        return getElementText(By.xpath(successLoginLabel));
    }

    public void clickLogOutButton(){
        clickElement(By.xpath(logOutButton));
    }

    public String getErrorToastMessage(){
        By toastContainer = By.cssSelector("div[aria-live='polite']");
        waitForElementPresent(toastContainer, 10);
        waitForElementVisible(By.xpath(errorToastMessage), 10);
        return getElementText(By.xpath(errorToastMessage));
    }
}