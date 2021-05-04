package meeting.app.api.services;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.converters.EventItemToEventItemListElement;
import meeting.app.api.exceptions.MeetingApiException;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import meeting.app.api.model.event.EventItemResponse;
import meeting.app.api.model.rating.RatingItem;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.repositories.CategoryItemRepository;
import meeting.app.api.repositories.EventItemRepository;
import meeting.app.api.repositories.RatingItemRepository;
import meeting.app.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private RatingItemRepository ratingItemRepository;

    @Autowired
    private UserRepository userRepository;

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
            log.info("event.service.getEventsForCategory.exception {}", ex.getStackTrace());
            throw new MeetingApiException("msg.err.event.get.events.for.category");
        }
    }

    @Transactional
    public EventItemResponse addRatingToEvent(Integer rating, Long eventId, UserEntity userEntity) {

        try {
            EventItem eventItem = eventItemRepository.getById(eventId);
            if (eventItem == null) {
                throw new MeetingApiException("msg.err.event.not.found");
            }

            RatingItem ratingItem = RatingItem.builder()
                    .rating(rating)
                    .eventItem(eventItem)
                    .userEntity(userEntity)
                    .build();

            eventItem.getRatings().add(ratingItem);
            eventItem.setRating(calculateAvgRating(eventItem.getRatings()));

            ratingItemRepository.save(ratingItem);

            return EventItemResponse.builder()
                    .eventItem(Arrays.asList(eventItemRepository.save(eventItem)))
                    .errorMessage(null)
                    .build();
        } catch (Exception ex) {
            log.info("event.service.add.rating.to.event.exception {}", ex.getMessage());
            throw new MeetingApiException("msg.err.event.add.rating");
        }
    }

    public EventItemResponse joinToEvent(String eventId, UserEntity user) {
        EventItem eventItem = eventItemRepository.getById(Long.valueOf(eventId));
        EventItemResponse response = new EventItemResponse();

        if (user == null || eventItem == null) {
            throw new MeetingApiException("msg.err.user.or.event.not.found");
        }
        if (eventItem.getActiveParticipants().contains(user)) {
            throw new MeetingApiException("user.joined.joined");
        }

        try {
            eventItem.getActiveParticipants().add(user);
            user.getEvents().add(eventItem);
            EventItem saved = eventItemRepository.save(eventItem);
            response.setEventItem(Arrays.asList(saved));
            return response;
        } catch (Exception ex) {
            log.info("event.service.join.event.exception {}", ex.getMessage());
            throw new MeetingApiException("msg.err.join.event");
        }
    }

    private Integer calculateAvgRating(List<RatingItem> ratings) {
        Integer sum = 0;
        for (RatingItem rating : ratings) {
            sum += rating.getRating();
        }

        return sum / ratings.size();
    }
}
