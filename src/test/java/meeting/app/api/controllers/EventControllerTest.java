package meeting.app.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.app.api.ControllerMockConfig;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import meeting.app.api.model.event.EventItemListElementResponse;
import meeting.app.api.model.event.EventItemResponse;
import meeting.app.api.model.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static meeting.app.api.mocks.MockModel.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest extends ControllerMockConfig {

    private final String PATH = "/api/event";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @InjectMocks
    private EventController controller;

    @BeforeEach
    public void init() {
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

    /**
     * addRatingToEvent()
     */

    @Test
    void addRatingToEventPass() throws Exception {
        Integer rating = 5;
        Long eventId = 5L;
        UserEntity userEntity = generateUserEntity();
        EventItemResponse eventItemResponse = generateEventItemResponse();

        when(userService.getUserFromContext()).thenReturn(userEntity);
        when(eventService.addRatingToEvent(anyInt(), anyLong(), any(UserEntity.class))).thenReturn(eventItemResponse);

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/add/rating/{eventId}/{rating}", eventId, rating)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        EventItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemResponse.class);

        assertNotNull(response);
        assertFalse(response.getEventItem().isEmpty());
        verify(userService, times(1)).getUserFromContext();
    }

    @Test
    void addRatingToEventThrowException() throws Exception {
        Integer rating = 5;
        Long eventId = 5L;

        given(userService.getUserFromContext()).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/add/rating/{eventId}/{rating}", eventId, rating)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        EventItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemResponse.class);

        assertNotNull(response);
        assertNotNull(response.getErrorMessage());
        verify(userService, times(1)).getUserFromContext();
    }

    @Test
    void addRatingToEventRatingIsLessThen0() throws Exception {
        Integer rating = 0;
        Long eventId = 5L;

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/add/rating/{eventId}/{rating}", eventId, rating)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        EventItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemResponse.class);

        assertNotNull(response);
        assertNotNull(response.getErrorMessage());
        verify(userService, times(0)).getUserFromContext();
    }

    @Test
    void addRatingToEventRatingIsGreaterThen5() throws Exception {
        Integer rating = 10;
        Long eventId = 5L;

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/add/rating/{eventId}/{rating}", eventId, rating)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        EventItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemResponse.class);

        assertNotNull(response);
        assertNotNull(response.getErrorMessage());
        verify(userService, times(0)).getUserFromContext();
    }

    /**
     * joinToEvent()
     */

    @Test
    void joinToEventPass() throws Exception {
        UserEntity userEntity = generateUserEntity();
        EventItemResponse eventItemResponse = generateEventItemResponse();
        String eventId = "5";

        when(userService.getUserFromContext()).thenReturn(userEntity);
        when(eventService.joinToEvent(anyString(), any(UserEntity.class))).thenReturn(eventItemResponse);

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/join/{eventId}", eventId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        EventItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemResponse.class);

        assertNotNull(response);
        assertNull(response.getErrorMessage());
        verify(userService, times(1)).getUserFromContext();
        verify(eventService, times(1)).joinToEvent(anyString(), any(UserEntity.class));
    }

    @Test
    void joinToEventThrowException() throws Exception {
        UserEntity userEntity = generateUserEntity();
        EventItemResponse eventItemResponse = generateEventItemResponse();
        String eventId = "5";

        when(userService.getUserFromContext()).thenReturn(userEntity);
        given(eventService.joinToEvent(anyString(), any(UserEntity.class))).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/join/{eventId}", eventId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        EventItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemResponse.class);

        assertNotNull(response);
        assertNotNull(response.getErrorMessage());
        verify(userService, times(1)).getUserFromContext();
        verify(eventService, times(1)).joinToEvent(anyString(), any(UserEntity.class));
    }
}