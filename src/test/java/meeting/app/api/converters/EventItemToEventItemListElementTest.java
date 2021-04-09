package meeting.app.api.converters;

import meeting.app.api.ConverterConfig;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import org.junit.jupiter.api.Test;

import static meeting.app.api.mocks.MockModel.generateEventItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventItemToEventItemListElementTest extends ConverterConfig {

    @Test
    void convert() {
        EventItem eventItem = generateEventItem();

        EventItemListElement eventItemListElement = eventItemToEventItemListElement.convert(eventItem);

        assertNotNull(eventItemListElement);
        assertEquals(eventItem.getId(), eventItemListElement.getId());
        assertEquals(eventItem.getCity(), eventItemListElement.getCity());
        assertEquals(eventItem.getDate(), eventItemListElement.getDate());
        assertEquals(eventItem.getMaxParticipants(), eventItemListElement.getMaxParticipants());
    }
}
