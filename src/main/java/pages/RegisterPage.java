package pages;

import base.BasePage;
import org.openqa.selenium.By;
import pages.dto.RegisterFormDataObject;

import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import static helpers.PropertiesHelper.loadAllFiles;

public class RegisterPage extends BasePage<RegisterPage> {

    Properties properties = loadAllFiles();

    public RegisterPage(){
    }

    String registerEmail = properties.getProperty("REGISTER_EMAIL");
    String registerPassword = properties.getProperty("REGISTER_PASSWORD");
    String registerConfirmPassword = properties.getProperty("REGISTER_CONF_PASSWORD");
    String createAccountButton = properties.getProperty("CREATE_ACCOUNT_BUTTON");
    String registerPage = properties.getProperty("NAVIGATE_TO_REGISTER_PAGE");
    String successLoginLabel = properties.getProperty("SUCCESS_LOGIN_LABEL");
    String errorToastMessage = properties.getProperty("ERROR_TOAST_MESSAGE");

    // Dynamic button map
    private static final Map<String, Consumer<RegisterPage>> BUTTON_ACTIONS = Map.of(
            "register", RegisterPage::goToRegisterPage,
            "create account", RegisterPage::createAccountButton
    );

    public static Map<String, Consumer<RegisterPage>> getButtonActions() {
        return BUTTON_ACTIONS;
    }

    public void goToRegisterPage(){
        clickElement(By.xpath(registerPage));
    }

    public void setRegisterAccount(RegisterFormDataObject data){
        if (data.getEmail() != null)
            setText(By.xpath(registerEmail), data.getEmail());
        if (data.getPassword() != null)
            setText(By.xpath(registerPassword), data.getPassword());
        if (data.getConfirmPassword() != null)
            setText(By.xpath(registerConfirmPassword), data.getConfirmPassword());
    }

    public void createAccountButton(){
        clickElement(By.xpath(createAccountButton));
    }

    public String getSuccessRegistered(){
        return getElementText(By.xpath(successLoginLabel));
    }

    public String getErrorToastMessage(){
        return getElementText(By.xpath(errorToastMessage));
    }
}