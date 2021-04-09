package meeting.app.api.controllers;

import meeting.app.api.model.event.EventItem;
import meeting.app.api.repositories.EventItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventItemRepository eventItemRepository;

    @GetMapping
    public ResponseEntity<List<EventItem>> getAllEvents() {
        Iterable<EventItem> dbElements = eventItemRepository.findAll();
        List<EventItem> events = new ArrayList<>();
        dbElements.forEach(events::add);

        return ResponseEntity.ok(events);
    }
}
