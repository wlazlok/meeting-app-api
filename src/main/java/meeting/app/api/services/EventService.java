package meeting.app.api.services;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.converters.EventItemToEventItemListElement;
import meeting.app.api.exceptions.MeetingApiException;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import meeting.app.api.repositories.CategoryItemRepository;
import meeting.app.api.repositories.EventItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventService {

    @Autowired
    private EventItemRepository eventItemRepository;

    @Autowired
    private CategoryItemRepository categoryItemRepository;

    @Autowired
    private EventItemToEventItemListElement eventItemToEventItemListElement;

    public List<EventItem> getAllEvents() {
        List<EventItem> events = new ArrayList<>();

        try {
            Iterable<EventItem> eventsFound = eventItemRepository.findAll();
            for (EventItem eventItem : eventsFound) {
                events.add(eventItem);
            }
        } catch (Exception ex) {
            log.info("event.service.getAllEvents.exception " + ex.getMessage());
            throw new MeetingApiException("msg.err.event.get.all.events");
        }

        return events;
    }

    public List<EventItemListElement> getEventsForCategory(Long categoryId) {
        List<EventItemListElement> events = new ArrayList<>();

        try {
            Optional<CategoryItem> categoryItem = categoryItemRepository.findById(categoryId);
            if (categoryItem.isPresent()) {
                events = categoryItem.get().getEvents()
                        .stream()
                        .map(e -> eventItemToEventItemListElement.convert(e))
                        .collect(Collectors.toList());

            }

            return events;
        } catch (Exception ex) {
            log.info("event.service.getEventsForCategory.exception " + ex.getMessage());
            throw new MeetingApiException("msg.err.event.get.events.for.category");
        }
    }
}
