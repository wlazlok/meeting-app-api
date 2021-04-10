package meeting.app.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.app.api.ControllerMockConfig;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.comment.CommentItemResponse;
import meeting.app.api.model.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static meeting.app.api.mocks.MockModel.generateCommentItem;
import static meeting.app.api.mocks.MockModel.generateUserEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends ControllerMockConfig {

    private final String PATH = "/api/comment";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    /**
     * getAllComments() method
     * 1. getAllComments OK
     * 2. getAllComments throw Exception
     */

    @Test
    void getAllComments() throws Exception {
        CommentItem commentItem = generateCommentItem();

        when(commentService.getAllComments()).thenReturn(Arrays.asList(commentItem));

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        CommentItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CommentItemResponse.class);

        assertNotNull(response);
        assertFalse(response.getCommentItemList().isEmpty());
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    void getAllCommentsThrowException() throws Exception {
        given(commentService.getAllComments()).willAnswer(invocationOnMock -> {
            throw new Exception("exception");
        });

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        CommentItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CommentItemResponse.class);

        assertNotNull(response);
        assertNull(response.getCommentItemList());
        assertNotNull(response.getErrorMessage());
        verify(commentService, times(1)).getAllComments();
    }

    /**
     * getCommentsForUser()
     * 1. getCommentsForUser OK
     * 2. getCommentsForUser throw Exception
     */

    @Test
    void getCommentsForUser() throws Exception {
        UserEntity userEntity = generateUserEntity();
        CommentItem commentItem = generateCommentItem();

        when(userService.getUserFromContext()).thenReturn(userEntity);
        when(commentService.getCommentsForUser(any(UserEntity.class))).thenReturn(Arrays.asList(commentItem));

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        CommentItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CommentItemResponse.class);

        assertNotNull(response);
        assertFalse(response.getCommentItemList().isEmpty());
        assertNull(response.getErrorMessage());
        verify(userService, times(1)).getUserFromContext();
        verify(commentService, times(1)).getCommentsForUser(any(UserEntity.class));
    }

    @Test
    void getCommentsForUserThrowException() throws Exception {
        UserEntity userEntity = generateUserEntity();

        when(userService.getUserFromContext()).thenReturn(userEntity);
        given(commentService.getCommentsForUser(any(UserEntity.class))).willAnswer(invocationOnMock -> {
            throw new Exception("exception");
        });

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        CommentItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CommentItemResponse.class);

        assertNotNull(response);
        assertNull(response.getCommentItemList());
        assertNotNull(response.getErrorMessage());
        verify(userService, times(1)).getUserFromContext();
        verify(commentService, times(1)).getCommentsForUser(any(UserEntity.class));
    }
}