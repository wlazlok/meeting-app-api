package meeting.app.api.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class EventItemListElement {

    private Long id;

    private String city;

    private int maxParticipants;

    private Date date;
}
