package meeting.app.api.controllers;

import meeting.app.api.model.event.EventItemListElementResponse;
import meeting.app.api.model.event.EventItemResponse;
import meeting.app.api.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static meeting.app.api.utils.HandleErrorMessage.mapErrorMessage;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

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
}
