package pages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectedEventDataObject {
    private String eventName;
    private int eventPrice;
    private int availableSeats;
}
