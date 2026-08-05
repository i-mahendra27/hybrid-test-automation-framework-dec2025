package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import pages.dto.NewEventDataObject;
import utils.LogUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pages.dto.SelectedEventDataObject;

import static helpers.PropertiesHelper.loadAllFiles;

public class EventPage extends BasePage<EventPage> {

    private static final Random RANDOM = new Random();
    Properties properties = loadAllFiles();

    public EventPage(){
    }

    String eventPage = properties.getProperty("NAVIGATE_TO_EVENT_PAGE");
    String searchEvent = properties.getProperty("SEARCH_EVENT_INPUT");
    String eventList = properties.getProperty("EVENT_LIST");
    String clearFilterButton = properties.getProperty("CLEAR_FILTER");
    String addNewEventButton = properties.getProperty("ADD_NEW_EVENT");

    String createEventPage = properties.getProperty("CREATE_EVENT_PAGE");
    String eventTitle = properties.getProperty("EVENT_TITLE");
    String eventDescription = properties.getProperty("EVENT_DESCRIPTION");
    String eventCategory = properties.getProperty("EVENT_CATEGORY");
    String eventCity = properties.getProperty("EVENT_CITY");
    String eventVenue = properties.getProperty("EVENT_VENUE");
    String eventDate = properties.getProperty("EVENT_DATE");
    String eventPrice = properties.getProperty("EVENT_PRICE");
    String eventSeat = properties.getProperty("EVENT_SEAT");
    String eventImageURL = properties.getProperty("EVENT_IMAGE_URL");
    String addEventButton = properties.getProperty("ADD_EVENT_BUTTON");
    String eventNotFoundMessage = properties.getProperty("NO_EVENT_FOUND_MESSAGE");

    String eventInformationDetail = properties.getProperty("EVENT_INFORMATION_DETAIL");
    String aboutEventDetail = properties.getProperty("ABOUT_EVENT_DETAIL");

    String selectEventCategory = properties.getProperty("SELECT_EVENT_CATEGORY");
    String selectEventCity = properties.getProperty("SELECT_EVENT_CITY");

    String bookNowButton = properties.getProperty("BOOK_NOW_BUTTON");

    // Event Page
    public void goToEventPage(){
        clickElement(By.xpath(eventPage));
    }

    public void listOfEvents(){
        List<WebElement> events = getWebElements(By.cssSelector(eventList));

        for (WebElement event : events){
            if (!events.isEmpty()){
                verifyTrue(event.isDisplayed());
            }else {
                verifyFalse(event.isDisplayed());
            }
        }
    }

    public List<WebElement> getEvents(){
        waitForElementVisible(By.cssSelector(eventList));
        return getWebElements(By.cssSelector(eventList));
    }

    public SelectedEventDataObject clickAnyAvailableEventAndGetData() {
        return clickAnyAvailableEventAndGetData(1);
    }

    public SelectedEventDataObject clickAnyAvailableEventAndGetData(int requiredTickets) {
        List<WebElement> candidates = new ArrayList<>();

        for (WebElement card : getEvents()) {
            if (isBookNowButtonAvailable(card) && getAvailableSeats(card) >= requiredTickets) {
                candidates.add(card);
            }
        }

        if (candidates.isEmpty()) {
            throw new IllegalStateException("No bookable event found with at least " + requiredTickets + " seat(s).");
        }

        WebElement selectedCard = candidates.get(RANDOM.nextInt(candidates.size()));
        String eventName = selectedCard.findElement(By.tagName("h3")).getText().trim();
        int eventPrice = getEventPrice(selectedCard);
        int availableSeats = getAvailableSeats(selectedCard);

        WebElement bookNow = selectedCard.findElement(By.xpath(bookNowButton));
        scrollToElement(bookNow);
        bookNow.click();
        LogUtils.info("Selected event for booking: " + eventName);
        return new SelectedEventDataObject(eventName, eventPrice, availableSeats);
    }

    public int getAnyEventPrice(String eventName) {
        for (WebElement card : getEvents()) {
            String actualEvent = card.findElement(By.tagName("h3")).getText().trim();

            if (actualEvent.equalsIgnoreCase(eventName)) {
                return getEventPrice(card);
            }
        }
        throw new NoSuchElementException("Event not found: " + eventName);
    }

    private boolean isBookNowButtonAvailable(WebElement card) {
        List<WebElement> buttons = card.findElements(By.xpath(bookNowButton));
        return !buttons.isEmpty() && buttons.get(0).isDisplayed() && buttons.get(0).isEnabled();
    }

    private int getEventPrice(WebElement card) {
        String priceText = card.findElement(By.cssSelector("p.text-lg.font-bold.text-indigo-700")).getText();
        String numericText = priceText.replaceAll("[^\\d]", "");

        if (numericText.isEmpty()) {
            throw new IllegalStateException("Event price is not numeric: " + priceText);
        }

        return Integer.parseInt(numericText);
    }

    private int getAvailableSeats(WebElement card) {
        String cardText = card.getText();

        if (cardText.toLowerCase(Locale.ROOT).contains("sold out")) {
            return 0;
        }

        Matcher seatMatcher = Pattern.compile("(\\d+)\\s+seats?\\s+(left|available)", Pattern.CASE_INSENSITIVE)
                .matcher(cardText);

        if (seatMatcher.find()) {
            return Integer.parseInt(seatMatcher.group(1));
        }

        return Integer.MAX_VALUE;
    }

    // Event Information
    public Map<String, String> getEventInformation(List<String> expectedFields) {
        Map<String, String> eventInfo = new HashMap<>();

        for (String field : expectedFields) {
            By valueLocator = By.xpath(
                    "//p[normalize-space()='" + field + "']/following-sibling::p"
            );

            String value = getElementText(valueLocator);
            eventInfo.put(field, value);
        }

        return eventInfo;
    }

    // Event detail
    public void isEventInformationDisplayed(){
        List <WebElement> eventInformation = getWebElements(By.cssSelector(eventInformationDetail));

        if (!eventInformation.isEmpty()) {
            LogUtils.info("Event information section is displayed");
        }else {
            LogUtils.info("Event information section is not displayed");
        }
    }

    public void isEventDetailDisplayed() {
        if (isElementDisplayed(By.xpath(aboutEventDetail))) {
            LogUtils.info("About Event Detail is displayed");
        } else {
            LogUtils.error("About Event Detail is not displayed");
        }
    }

    // Add Event
    public void clickAddNewEventButton(){
        clickElement(By.xpath(addNewEventButton));
    }

    public void createEventPage(){
        waitForElementVisible(By.xpath(createEventPage));
    }

    public void clickAddEventButton(){
        clickElement(By.xpath(addEventButton));
    }

    public void createNewEventForm(NewEventDataObject data){
        setText(By.xpath(eventTitle), data.getEventTitle());
        setText(By.xpath(eventDescription), data.getEventDescription());

        Select dropDown = new Select(getWebElement(By.xpath(eventCategory)));
        dropDown.selectByVisibleText(data.getEventCategory());

        setText(By.xpath(eventCity), data.getEventCity());
        setText(By.xpath(eventVenue), data.getEventVenue());
        setText(By.xpath(eventDate), data.getEventStartDate());
        setText(By.xpath(eventPrice), data.getEventPrice());
        setText(By.xpath(eventSeat), String.valueOf(data.getTotalSeats()));
        setText(By.xpath(eventImageURL), data.getEventImageURLPath());
    }

    // Search Event
    public void searchEvent(String eventName){
        setText(By.xpath(searchEvent), eventName);
    }

    public void pressEnter(){
        pressENTER();
    }

    public void clearFilterButton(){
        clickElement(By.xpath(clearFilterButton));
    }

    public String searchNotFound(){
        waitForElementVisible(By.xpath(eventNotFoundMessage));
        return getWebElement(By.xpath(eventNotFoundMessage)).getText();
    }

    // Filter Event
    public void selectEventCategory(String category){
        selectDropDown(By.cssSelector(selectEventCategory), category);
    }

    public void selectEventCity(String city){
        selectDropDown(By.cssSelector(selectEventCity), city);
    }

    // Dynamic button map
    private static final Map<String, Consumer<EventPage>> BUTTON_ACTIONS = Map.of(
            "add new event", EventPage::clickAddNewEventButton,
            "add event", EventPage::clickAddEventButton
    );

    public static Map<String, Consumer<EventPage>> getButtonActions() {
        return BUTTON_ACTIONS;
    }

}
