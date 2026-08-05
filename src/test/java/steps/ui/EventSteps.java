package steps.ui;

import factory.BookingDataFactory;
import hooks.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.EventPage;
import pages.MyBookingPage;
import pages.dto.EventBookDetailDataObject;
import pages.dto.SelectedEventDataObject;

import java.util.Random;

/**
 * Event-specific steps. Page-specific actions that belong to EventPage.
 */
public class EventSteps {

    private final TestContext testContext;
    private final EventPage eventPage;
    private final MyBookingPage myBookingPage;

    public EventSteps(TestContext testContext) {
        this.testContext = testContext;
        this.eventPage = new EventPage();
        this.myBookingPage = new MyBookingPage();
    }

    @When("I enter {string} in search field")
    public void iEnterInSearchField(String keyword) {
        eventPage.searchEvent(keyword);
    }

    @When("I press enter")
    public void iPressEnter() {
        eventPage.pressEnter();
    }

    @And("I select {string} from category dropdown")
    public void iSelectFromCategoryDropdown(String category) {
        eventPage.selectEventCategory(category);
    }

    @And("I select {string} from city dropdown")
    public void iSelectFromCityDropdown(String city) {
        eventPage.selectEventCity(city);
    }

    @Given("I have an existing booking")
    public void iHaveAnExistingBooking() {
        Random genTicketNum = new Random();

        eventPage.goToEventPage();
        SelectedEventDataObject selectedEvent = eventPage.clickAnyAvailableEventAndGetData(2);
        setSelectedEventContext(selectedEvent);

        int maxTickets = Math.min(10, selectedEvent.getAvailableSeats());
        int tickets = genTicketNum.nextInt(maxTickets - 1) + 2;
        createAndFillBookingInformation(tickets);

        myBookingPage.clickConfirmBookingButton();
        myBookingPage.verifyEquals(myBookingPage.verifyBookingSuccess(), "Your tickets are reserved.");
    }

    @Given("I have an existing booking with {int} ticket\\(s)")
    public void iHaveAnExistingBookingWithTickets(int tickets) {
        eventPage.goToEventPage();
        SelectedEventDataObject selectedEvent = eventPage.clickAnyAvailableEventAndGetData(tickets);
        setSelectedEventContext(selectedEvent);

        createAndFillBookingInformation(tickets);

        myBookingPage.clickConfirmBookingButton();
        myBookingPage.verifyEquals(myBookingPage.verifyBookingSuccess(), "Your tickets are reserved.");
    }

    private void setSelectedEventContext(SelectedEventDataObject selectedEvent) {
        testContext.events().setSelectedEventName(selectedEvent.getEventName());
        testContext.events().setSelectedEventPrice(selectedEvent.getEventPrice());
        testContext.events().setSelectedEventAvailableSeats(selectedEvent.getAvailableSeats());
    }

    private void createAndFillBookingInformation(int tickets) {
        EventBookDetailDataObject bookingData = BookingDataFactory.createBooking(tickets);
        testContext.booking().setBookingData(bookingData);
        myBookingPage.fillBookingInformation(bookingData);
    }
}
