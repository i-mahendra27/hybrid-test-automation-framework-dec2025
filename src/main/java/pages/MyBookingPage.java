package pages;

import base.BasePage;
import factory.DriverManager;
import lombok.NonNull;
import managers.ConfigManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.dto.EventBookDetailDataObject;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import static helpers.PropertiesHelper.loadAllFiles;

public class MyBookingPage extends BasePage<MyBookingPage> {

    Properties properties = loadAllFiles();

    public MyBookingPage(){
    }

    String bookingPage = properties.getProperty("NAVIGATE_TO_MY_BOOKING_PAGE");
    String emptyStateLabel = properties.getProperty("EMPTY_STATE_LABEL");
    String currentTicketQty = properties.getProperty("CURRENT_TICKET_QTY");
    String addTicketButton =  properties.getProperty("ADD_TICKET");
    String minusTicketButton =  properties.getProperty("MIN_TICKET");
    String customerName = properties.getProperty("CUSTOMER_NAME");
    String customerEmail = properties.getProperty("CUSTOMER_EMAIL");
    String customerPhone = properties.getProperty("CUSTOMER_PHONE");
    String confirmBookingButton = properties.getProperty("CONFIRM_BOOKING_BUTTON");
    String successBookingLabel = properties.getProperty("SUCCESS_BOOKING_LABEL");
    String viewMyBookingButton = properties.getProperty("VIEW_MY_BOOKINGS");
    String listOfBookedEvents = properties.getProperty("LIST_OF_BOOKED_EVENT");
    String viewDetailsButton = properties.getProperty("VIEW_DETAILS_BUTTON");
    String currentTotalPaidLabel = properties.getProperty("ACTUAL_TOTAL_PRICE");
    String clearAllBookingsTextButton = properties.getProperty("DELETE_BOOKING_BUTTON");
    String cancelBookingButton = properties.getProperty("CANCEL_BOOKING_BUTTON");
    String cancelConfirmButton = properties.getProperty("CANCEL_CONFIRM_BUTTON");
    String refundSection = properties.getProperty("REFUND_SECTION");
    String checkEligibleOrNoEligibleTextButton = properties.getProperty("CHECK_ELIGIBLE_OR_NO_ELIGIBLE_BUTTON");


    public void goToMyBookingPage(){
        clickElement(By.xpath(bookingPage));
        waitForMyBookingsLoaded();
    }

    public String bookingEmptyStateLabel(){
        return getElementText(By.xpath(emptyStateLabel));
    }

    public String getCurrentURL(){
        return getCurrentUrl();
    }

    // Book Information
    public int getCurrentTicketQuantity(){
        return Integer.parseInt(getElementText(By.xpath(currentTicketQty)));
    }

    public void clickPlusButton(){
        clickElement(By.xpath(addTicketButton));
    }

    public void clickMinusButton(){
        clickElement(By.xpath(minusTicketButton));
    }

    public void enterFullName(String fullName){
        setText(By.xpath(customerName), fullName);
    }

    public void enterEmail(String email){
        setText(By.xpath(customerEmail), email);
    }

    public void enterPhoneNumber(String phoneNumber){
        setText(By.xpath(customerPhone), phoneNumber);
    }

    public void enterNumOfTickets(int desiredQty) {
        if (desiredQty < 1 || desiredQty > 10) {
            throw new IllegalArgumentException("Ticket quantity must be between 1 and 10");
        }

        int currentQty = getCurrentTicketQuantity();

        while (currentQty < desiredQty) {
            clickPlusButton();
            currentQty++;
        }

        while (currentQty > desiredQty) {
            clickMinusButton();
            currentQty--;
        }
    }

    // Book method
    public void waitForBookingFormDisplayed(){
        waitForElementVisible(By.xpath(currentTicketQty));
        waitForElementVisible(By.xpath(customerName));
        waitForElementVisible(By.xpath(customerEmail));
        waitForElementVisible(By.xpath(customerPhone));
    }

    public void fillBookingInformation(EventBookDetailDataObject data){
        enterNumOfTickets(data.getNumOfTickets());
        enterFullName(data.getFullName());
        enterEmail(data.getEmail());
        enterPhoneNumber(String.valueOf(data.getPhoneNumber()));
    }

    public void clickConfirmBookingButton(){
        clickElement(By.xpath(confirmBookingButton));
    }

    public String verifyBookingSuccess(){
        return getElementText(By.xpath(successBookingLabel));
    }

    public List<WebElement> getBookingList(){
        return getWebElements(By.cssSelector(listOfBookedEvents));
    }

    public void waitForMyBookingsLoaded() {
        By bookingCardLocator = By.cssSelector(listOfBookedEvents);

        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(ConfigManager.getExplicitWaitTimeout()))
                .until(driver -> !driver.findElements(bookingCardLocator).isEmpty()
                        || isElementDisplayed(By.xpath(emptyStateLabel)));
    }

    public Map<String, String> getEventInformation(List<String> expectedFields){
        return getInformation(expectedFields);
    }

    public Map<String, String> getCustomerInformation(List<String> expectedFields){
        return getInformation(expectedFields);
    }

    @NonNull
    private Map<String, String> getInformation(List<String> expectedFields) {
        Map<String, String> information = new HashMap<>();

        for (String field : expectedFields) {
            By valueLocator = By.xpath(
                    "//span[normalize-space()='" + field + "']/following-sibling::span"
            );

            information.put(field, getElementText(valueLocator));
        }
        return information;
    }

    public void clickViewMyBookingsButton(){
        clickElement(By.xpath(viewMyBookingButton));
    }

    public void clickViewDetailsButton(){
        clickElement(By.xpath(viewDetailsButton));
    }

    public int getCurrentTotalPaidAmount(){
        String totalPaidText = getElementText(By.cssSelector(currentTotalPaidLabel));
        String numericText = totalPaidText.replaceAll("[^\\d]", "");

        if (numericText.isEmpty()){
            throw new IllegalStateException("Total paid amount is not numeric: " + totalPaidText);
        }
        return Integer.parseInt(numericText);
    }

    public void clickClearAllBookingsTextButton(){
        clickElement(By.xpath(clearAllBookingsTextButton));
    }

    public void confirmBookingDeletion(){
        Alert alert = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(ConfigManager.getExplicitWaitTimeout()))
                .until(ExpectedConditions.alertIsPresent());
        alert.accept();
    }

    public void waitUntilBookingsCleared(){
        By bookingCardLocator = By.cssSelector(listOfBookedEvents);

        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(ConfigManager.getExplicitWaitTimeout()))
                .until(driver -> getWebElements(bookingCardLocator).isEmpty()
                        || isElementDisplayed(By.xpath(emptyStateLabel)));
    }

    public void waitForEmptyStateDisplayed(){
        waitForElementVisible(By.xpath(emptyStateLabel));
    }

    public void clearAllBookingsIfPresent(){
        if (!isClearAllBookingsTextButtonDisplayed()) {
            return;
        }

        clickClearAllBookingsTextButton();
        confirmBookingDeletion();
        waitUntilBookingsCleared();
    }

    public boolean isClearAllBookingsTextButtonDisplayed(){
        return isElementDisplayed(By.xpath(clearAllBookingsTextButton));
    }

    public String getFirstBookedEventName(){
        waitForMyBookingsLoaded();
        List<WebElement> bookings = getBookingList();

        if (bookings.isEmpty()) {
            throw new IllegalStateException("No booked events are displayed.");
        }

        return bookings.get(0).findElement(By.tagName("h3")).getText().trim();
    }

    public String getFirstBookedEventCardText(){
        waitForMyBookingsLoaded();
        List<WebElement> bookings = getBookingList();

        if (bookings.isEmpty()) {
            throw new IllegalStateException("No booked events are displayed.");
        }

        return bookings.get(0).getText().trim();
    }

    public boolean isBookedEventCardDisplayed(String bookedEventCardText){
        return getBookingList().stream()
                .map(WebElement::getText)
                .map(String::trim)
                .anyMatch(bookedEventCardText::equals);
    }

    public void waitUntilBookedEventCardDisappears(String bookedEventCardText){
        By bookingCardLocator = By.cssSelector(listOfBookedEvents);

        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(ConfigManager.getExplicitWaitTimeout()))
                .until(driver -> driver.findElements(bookingCardLocator).stream()
                        .map(WebElement::getText)
                        .map(String::trim)
                        .noneMatch(bookedEventCardText::equals));
    }

    public void clickCancelButton(){
        clickElement(By.xpath(cancelBookingButton));
    }

    public void clickConfirmBookingCancellationButton(){
        waitForElementClickable(By.xpath(cancelConfirmButton));
        clickElement(By.xpath(cancelConfirmButton));
    }

    public void waitForRefundSectionDisplayed(){
        scrollToPosition(0, 99999);
        waitForElementVisible(By.xpath(refundSection));
    }

    public void clickCheckEligible(){
        By checkEligibleButton = By.xpath(checkEligibleOrNoEligibleTextButton);
        scrollToPosition(0, 99999);
        waitForElementVisible(checkEligibleButton);

        clickElement(By.xpath(checkEligibleOrNoEligibleTextButton));
    }

    // Dynamic button map - NOTE: "cancel booking" and "yes, cancel it" need testContext access
    // Use BookingSteps directly for those instead
    private static final Map<String, Consumer<MyBookingPage>> BUTTON_ACTIONS = Map.of(
            "confirm booking", MyBookingPage::clickConfirmBookingButton,
            "view details", MyBookingPage::clickViewDetailsButton,
            "clear all bookings", MyBookingPage::clickClearAllBookingsTextButton,
            "check eligibility for refund?", MyBookingPage::clickCheckEligible
    );

    public static Map<String, Consumer<MyBookingPage>> getButtonActions() {
        return BUTTON_ACTIONS;
    }

}
