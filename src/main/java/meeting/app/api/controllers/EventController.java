package meeting.app.api.controllers;

import meeting.app.api.model.event.EventItemListElementResponse;
import meeting.app.api.model.event.EventItemResponse;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.services.EventService;
import meeting.app.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static meeting.app.api.utils.HandleErrorMessage.mapErrorCode;
import static meeting.app.api.utils.HandleErrorMessage.mapErrorMessage;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<EventItemResponse> getAllEvents() {
        EventItemResponse response = new EventItemResponse();

        try {
            response.setEventItem(eventService.getAllEvents());
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            response.setErrorMessage(mapErrorMessage(ex));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list/{categoryId}")
    public ResponseEntity<EventItemListElementResponse> getEventsForCategory(@PathVariable("categoryId") String categoryId) {
        EventItemListElementResponse response = new EventItemListElementResponse();

        try {
            response.setEventItemListElementList(eventService.getEventsForCategory(Long.valueOf(categoryId)));
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            response.setErrorMessage(mapErrorMessage(ex));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/add/rating/{eventId}/{rating}")
    public ResponseEntity<EventItemResponse> addRatingToEvent(@PathVariable Integer rating, @PathVariable Long eventId) {
        EventItemResponse response = new EventItemResponse();
        if (rating <= 0 || rating > 5) {
            response.setErrorMessage(mapErrorCode("msg.err.incorrect.rating"));
            return ResponseEntity.badRequest().body(response);
        }
        try {
            UserEntity userEntity = userService.getUserFromContext();
            return ResponseEntity.ok().body(eventService.addRatingToEvent(rating, eventId, userEntity));
        } catch (Exception ex) {
            response.setErrorMessage(mapErrorMessage(ex));
            return ResponseEntity.badRequest().body(response);
        }
    }
}
