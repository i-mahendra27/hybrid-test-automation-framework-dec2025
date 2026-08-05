package steps.ui;

import helpers.DataFakerHelper;
import hooks.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.EventPage;
import pages.dto.NewEventDataObject;

public class StepsAddNewEvent {
    private TestContext testContext;
    private EventPage eventPage;

    public StepsAddNewEvent(TestContext testContext) {
        this.testContext = testContext;
        this.eventPage = new EventPage();
    }

    public StepsAddNewEvent() {
        this(new TestContext());
    }

    @Then("I should be redirected to event creation page")
    public void i_should_be_redirected_to_event_creation_page() {
        eventPage.createEventPage();
    }

    @When("I fill all event information")
    public void i_fill_all_event_information() {
        NewEventDataObject eventData =
                NewEventDataObject.builder()
                        .eventTitle(DataFakerHelper.getFaker().book().title() + " Event")
                        .eventDescription(DataFakerHelper.getFaker().lorem().paragraph())
                        .eventCategory("Conference")
                        .eventCity(DataFakerHelper.getFaker().address().city())
                        .eventVenue(DataFakerHelper.getFaker().company().name() + " Hall")
                        .eventStartDate("25 December 2026")
                        .eventPrice(String.valueOf(DataFakerHelper.getFaker().number().numberBetween(100, 5000)))
                        .totalSeats(DataFakerHelper.getFaker().number().numberBetween(50, 1000))
                        .eventImageURLPath("C:\\images\\event.png")
                        .build();

        eventPage.createNewEventForm(eventData);
    }
}
