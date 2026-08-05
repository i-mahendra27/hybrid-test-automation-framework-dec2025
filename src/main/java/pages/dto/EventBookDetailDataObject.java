package pages.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventBookDetailDataObject {
    private int numOfTickets;
    private String fullName;
    private String email;
    private String phoneNumber;
}
