package meeting.app.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.app.api.ControllerMockConfig;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import meeting.app.api.model.event.EventItemListElementResponse;
import meeting.app.api.model.event.EventItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static meeting.app.api.mocks.MockModel.generateEventItem;
import static meeting.app.api.mocks.MockModel.generateEventItemListElement;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest extends ControllerMockConfig {

    private final String PATH = "/api/event";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @InjectMocks
    private EventController controller;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * getAllEvents() method
     * 1. getAllEvents OK
     * 2. getAllEvents throw Exception
     */

    @Test
    void getAllEvents() throws Exception {
        EventItem eventItem = generateEventItem();

        when(eventService.getAllEvents()).thenReturn(Arrays.asList(eventItem));

        MvcResult mvcResult = mockMvc.perform(get(PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        EventItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemResponse.class);

        assertNotNull(response);
        assertNotNull(response.getEventItem());
        assertFalse(response.getEventItem().isEmpty());
        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    void getAllEventsThrowException() throws Exception {
        EventItem eventItem = generateEventItem();

        given(eventService.getAllEvents()).willAnswer(invocationOnMock -> {
            throw new Exception("exception");
        });

        MvcResult mvcResult = mockMvc.perform(get(PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        EventItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemResponse.class);

        assertNotNull(response);
        assertNull(response.getEventItem());
        assertNotNull(response.getErrorMessage());
        verify(eventService, times(1)).getAllEvents();
    }

    /**
     * getEventsForCategory()
     * 1. getEventsForCategory OK
     * 2. getEventsForCategory throwExceptiion
     */

    @Test
    void getEventsForCategory() throws Exception {
        EventItemListElement eventItemListElement = generateEventItemListElement();

        when(eventService.getEventsForCategory(anyLong())).thenReturn(Arrays.asList(eventItemListElement));

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/list/{categoryId}", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        EventItemListElementResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemListElementResponse.class);

        assertNotNull(response);
        assertNotNull(response.getEventItemListElementList());
        verify(eventService, times(1)).getEventsForCategory(anyLong());
        ;
    }

    @Test
    void getEventsForCategoryThrowException() throws Exception {
        given(eventService.getEventsForCategory(anyLong())).willAnswer(invocationOnMock -> {
            throw new Exception("exception");
        });

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/list/{categoryId}", "4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        EventItemListElementResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemListElementResponse.class);

        assertNotNull(response);
        assertNull(response.getEventItemListElementList());
        assertNotNull(response.getErrorMessage());
        verify(eventService, times(1)).getEventsForCategory(anyLong());
    }
}
