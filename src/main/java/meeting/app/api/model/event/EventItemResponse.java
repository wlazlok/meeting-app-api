package meeting.app.api.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import meeting.app.api.model.utils.ErrorMessage;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class EventItemResponse {

    private List<EventItem> eventItem;

    private ErrorMessage errorMessage;
}
