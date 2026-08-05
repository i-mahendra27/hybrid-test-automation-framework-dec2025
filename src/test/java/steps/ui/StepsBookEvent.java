package steps.ui;

import factory.BookingDataFactory;
import hooks.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.EventPage;
import pages.MyBookingPage;
import pages.dto.EventBookDetailDataObject;
import pages.dto.SelectedEventDataObject;
import utils.LogUtils;

import java.util.List;
import java.util.Random;

public class StepsBookEvent {
    private final TestContext testContext;
    private final MyBookingPage myBookingPage;
    private final EventPage eventPage;
    private EventBookDetailDataObject bookingData;

    public StepsBookEvent(TestContext testContext) {
        this.testContext = testContext;
        this.myBookingPage = new MyBookingPage();
        this.eventPage = new EventPage();
        this.bookingData = new EventBookDetailDataObject();
    }

    @And("I click on any available event card")
    public void i_click_on_any_available_event_card() {
       SelectedEventDataObject selectedEvent = eventPage.clickAnyAvailableEventAndGetData(2);
       testContext.events().setSelectedEventName(selectedEvent.getEventName());
       testContext.events().setSelectedEventPrice(selectedEvent.getEventPrice());
       testContext.events().setSelectedEventAvailableSeats(selectedEvent.getAvailableSeats());
       myBookingPage.waitForBookingFormDisplayed();
    }

    @And("I enter booking information")
    public void i_enter_booking_information() {
        Random genTicketNum = new Random();
        Integer availableSeats = testContext.events().getSelectedEventAvailableSeats();
        int maxTickets = Math.min(10, availableSeats == null ? 10 : availableSeats);

        if (maxTickets < 2) {
            throw new IllegalStateException("Selected event does not have enough seats for non-eligible booking.");
        }

        bookingData = BookingDataFactory.createBooking(genTicketNum.nextInt(maxTickets - 1) + 2);
        testContext.booking().setBookingData(bookingData);
        myBookingPage.fillBookingInformation(bookingData);
    }

    @When("I view my bookings")
    public void i_view_my_bookings(){
        myBookingPage.clickViewMyBookingsButton();
    }

    @Then("I should be redirected to the my bookings page")
    public void i_should_be_redirected_to_my_bookings_page() {
        String selectedEventName = testContext.events().getSelectedEventName();
        if (selectedEventName == null) {
            throw new IllegalStateException("Selected event name is not available.");
        }

        myBookingPage.verifyTextVisible("My Bookings");
        myBookingPage.verifyTextVisible(selectedEventName);

        String currentUrl = myBookingPage.getCurrentURL();
        myBookingPage.verifyTrue(currentUrl.contains("/bookings"),
                "Expected URL to contain /bookings, but current URL is: " + currentUrl);
    }

    @And("my booked events should be displayed")
    public void my_booked_events_should_be_displayed() {
        List<org.openqa.selenium.WebElement> bookingList = myBookingPage.getBookingList();
        myBookingPage.verifyTrue(!bookingList.isEmpty(), "No booked events are displayed");
        LogUtils.info("Total booked events: " +  bookingList.size());
    }
}
