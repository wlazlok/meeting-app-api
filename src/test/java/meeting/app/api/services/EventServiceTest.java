package meeting.app.api.services;

import meeting.app.api.ServiceMockConfig;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static meeting.app.api.mocks.MockModel.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class EventServiceTest extends ServiceMockConfig {

    @InjectMocks
    private EventService underTest;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * getAllEvents()
     * 1. getAllEvents OK
     * 2. getAllEvents throw Exception
     */

    @Test
    void getAllEvents() {
        EventItem eventItem = generateEventItem();
        Iterable<EventItem> events = Arrays.asList(eventItem);

        when(eventItemRepository.findAll()).thenReturn(events);

        List<EventItem> result = underTest.getAllEvents();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(eventItemRepository, times(1)).findAll();
    }

    @Test
    void getAllEventsThrowException() {
        given(eventItemRepository.findAll()).willAnswer(invocationOnMock -> {
            throw new Exception("exception");
        });

        try {
            underTest.getAllEvents();
            fail("Should throw exception");
        } catch (Exception ex) {
            assertNotNull(ex);
            assertEquals("msg.err.event.get.all.events", ex.getMessage());
            verify(eventItemRepository, times(1)).findAll();
        }
    }

    /**
     * getEventsForCategory() method
     * 1. getEventsForCategory OK
     * 2. getEventsForCategory (categoryItem isNotPresent)
     * 3. getEventsForCategory throw Exception
     */

    @Test
    void getEventsForCategory() {
        Long categoryId = 5L;
        EventItemListElement eventItemListElement = generateEventItemListElement();
        CategoryItem categoryItem = generateCategoryItem();

        when(categoryItemRepository.findById(anyLong())).thenReturn(Optional.of(categoryItem));
        when(eventItemToEventItemListElement.convert(any(EventItem.class))).thenReturn(eventItemListElement);

        List<EventItemListElement> result = underTest.getEventsForCategory(categoryId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(categoryItemRepository, times(1)).findById(anyLong());
        verify(eventItemToEventItemListElement, times(1)).convert(any(EventItem.class));
    }

    @Test
    void getEventsForCategoryAndCategoryItemNotPresent() {
        Long categoryId = 500L;

        when(categoryItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        List<EventItemListElement> result = underTest.getEventsForCategory(categoryId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryItemRepository, times(1)).findById(anyLong());
        verify(eventItemToEventItemListElement, times(0)).convert(any(EventItem.class));
    }

    @Test
    void getEventsForCategoryThrowException() {
        Long categoryId = 5L;

        given(categoryItemRepository.findById(anyLong())).willAnswer(invocationOnMock -> {
            throw new Exception("exception");
        });

        try {
            underTest.getEventsForCategory(categoryId);
            fail("Should throw exception");
        } catch (Exception ex) {
            assertEquals("msg.err.event.get.events.for.category", ex.getMessage());
            verify(categoryItemRepository, times(1)).findById(anyLong());
            verify(eventItemToEventItemListElement, times(0)).convert(any(EventItem.class));
        }
    }
}
