package meeting.app.api.converters;

import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EventItemToEventItemListElement implements Converter<EventItem, EventItemListElement> {

    @Override
    public EventItemListElement convert(EventItem eventItem) {
        return EventItemListElement.builder()
                .id(eventItem.getId())
                .city(eventItem.getCity())
                .date(eventItem.getDate())
                .maxParticipants(eventItem.getMaxParticipants())
                .ratting(eventItem.getRating())
                .build();
    }
}
