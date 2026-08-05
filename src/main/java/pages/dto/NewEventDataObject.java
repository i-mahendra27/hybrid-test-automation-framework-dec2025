package pages.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDataObject {
    private String eventTitle;
    private String eventDescription;
    private String eventCategory;
    private String eventCity;
    private String eventVenue;
    private String eventStartDate;
    private String eventPrice;
    private int totalSeats;
    private String eventImageURLPath;
}
