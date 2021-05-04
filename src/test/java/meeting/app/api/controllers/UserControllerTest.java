package meeting.app.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.app.api.ControllerMockConfig;
import meeting.app.api.exceptions.MeetingApiException;
import meeting.app.api.model.event.EventItemListElement;
import meeting.app.api.model.event.EventItemListElementResponse;
import meeting.app.api.model.user.*;
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

import static meeting.app.api.controllers.MapObject.asJsonString;
import static meeting.app.api.mocks.MockModel.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest extends ControllerMockConfig {

    private final String PATH = "/api/user";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * createUser()
     */

    @Test
    void createUserOk() throws Exception {
        UserEntity userEntity = generateUserEntity();
        CreateUserRequest createUserRequest = generateCreateUserRequest();

        when(userService.validateUserPassword(anyString(), anyString())).thenReturn(true);
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userEntity);
        doNothing().when(emailService).sendActivateEmail(any(UserEntity.class), anyString());

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createUserRequest)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        CreateUserResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreateUserResponse.class);

        assertNotNull(response);
        assertNull(response.getErrorMessage());
        verify(userService, times(1)).validateUserPassword(anyString(), anyString());
        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
        verify(emailService, times(1)).sendActivateEmail(any(UserEntity.class), anyString());
    }

    @Test
    void createUserPasswordNotValid() throws Exception {
        CreateUserRequest createUserRequest = generateCreateUserRequest();

        when(userService.validateUserPassword(anyString(), anyString())).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createUserRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        CreateUserResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreateUserResponse.class);

        assertNotNull(response);
        assertNotNull(response.getErrorMessage());
        verify(userService, times(1)).validateUserPassword(anyString(), anyString());
        verify(userService, times(0)).createUser(any(CreateUserRequest.class));
        verify(emailService, times(0)).sendActivateEmail(any(UserEntity.class), anyString());
    }

    @Test
    void createUserThrowException() throws Exception {
        UserEntity userEntity = generateUserEntity();
        CreateUserRequest createUserRequest = generateCreateUserRequest();

        when(userService.validateUserPassword(anyString(), anyString())).thenReturn(true);
        given(userService.createUser(any(CreateUserRequest.class))).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createUserRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        CreateUserResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreateUserResponse.class);

        assertNotNull(response);
        assertNotNull(response.getErrorMessage());
        verify(userService, times(1)).validateUserPassword(anyString(), anyString());
        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
        verify(emailService, times(0)).sendActivateEmail(any(UserEntity.class), anyString());
    }

    /**
     * activateAccount()
     */

    @Test
    void activateAccountOk() throws Exception {
        String uuid = "123123123";
        String usr = "8";

        doNothing().when(userService).activateUserAccount(anyString(), anyString());

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", uuid)
                .param("usr", usr))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(userService, times(1)).activateUserAccount(anyString(), anyString());
    }

    @Test
    void activateAccountThrowException() throws Exception {
        String uuid = "123123123";
        String usr = "8";

        doThrow(new MeetingApiException("msg.err.activate.user")).when(userService).activateUserAccount(anyString(), anyString());

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", uuid)
                .param("usr", usr))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(userService, times(1)).activateUserAccount(anyString(), anyString());
    }

    /**
     * resetPasswordAndSendEmail()
     */

    @Test
    void resetPasswordAndSendEmailOk() throws Exception {
        ResetPasswordRequest resetPasswordRequest = generateResetPasswordRequest();
        UserEntity userEntity = generateUserEntity();

        when(userService.resetPassword(any(ResetPasswordRequest.class))).thenReturn(userEntity);
        doNothing().when(emailService).sendResetEmail(any(UserEntity.class), anyString());

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resetPasswordRequest)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(emailService, times(1)).sendResetEmail(any(UserEntity.class), anyString());
        verify(userService, times(1)).resetPassword(any(ResetPasswordRequest.class));
    }

    @Test
    void resetPasswordAndSendEmailThrowException() throws Exception {
        ResetPasswordRequest resetPasswordRequest = generateResetPasswordRequest();
        UserEntity userEntity = generateUserEntity();

        given(userService.resetPassword(any(ResetPasswordRequest.class))).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resetPasswordRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(emailService, times(0)).sendResetEmail(any(UserEntity.class), anyString());
        verify(userService, times(1)).resetPassword(any(ResetPasswordRequest.class));
    }

    /**
     * getEventsForUser()
     */

    @Test
    void getEventsForUserOk() throws Exception {
        UserEntity userEntity = generateUserEntity();
        EventItemListElement eventItemListElement = generateEventItemListElement();

        when(userService.getUserFromContext()).thenReturn(userEntity);
        when(userService.getUserEvents(any(UserEntity.class))).thenReturn(Arrays.asList(eventItemListElement));

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        EventItemListElementResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemListElementResponse.class);

        assertNotNull(response);
        assertNull(response.getErrorMessage());
        verify(userService, times(1)).getUserFromContext();
        verify(userService, times(1)).getUserEvents(any(UserEntity.class));
    }

    @Test
    void getEventsForUserThrowException() throws Exception {
        UserEntity userEntity = generateUserEntity();

        given(userService.getUserFromContext()).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        EventItemListElementResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventItemListElementResponse.class);

        assertNotNull(response);
        assertNotNull(response.getErrorMessage());
        verify(userService, times(1)).getUserFromContext();
        verify(userService, times(0)).getUserEvents(any(UserEntity.class));
    }

    /**
     * changePasswordFromLink()
     */

    @Test
    void changePasswordFromLinkOk() throws Exception {
        String uuid = "12314123";
        String userId = "5";
        ChangePasswordRequest changePasswordRequest = generateChangePasswordRequest();

        when(userService.validateUserPassword(anyString(), anyString())).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/change")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", uuid)
                .param("usr", userId)
                .content(asJsonString(changePasswordRequest)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(userService, times(1)).validateUserPassword(anyString(), anyString());
        verify(userService, times(1)).changePasswordFromLink(anyString(), anyString(), any(ChangePasswordRequest.class));
    }

    @Test
    void changePasswordFromLinkPasswordNotvalid() throws Exception {
        String uuid = "12314123";
        String userId = "5";
        ChangePasswordRequest changePasswordRequest = generateChangePasswordRequest();

        when(userService.validateUserPassword(anyString(), anyString())).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/change")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", uuid)
                .param("usr", userId)
                .content(asJsonString(changePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(userService, times(1)).validateUserPassword(anyString(), anyString());
        verify(userService, times(0)).changePasswordFromLink(anyString(), anyString(), any(ChangePasswordRequest.class));
    }

    @Test
    void changePasswordFromLinkPasswordThrowException() throws Exception {
        String uuid = "12314123";
        String userId = "5";
        ChangePasswordRequest changePasswordRequest = generateChangePasswordRequest();

        when(userService.validateUserPassword(anyString(), anyString())).thenReturn(true);
        doThrow(new MeetingApiException("msg.err.change.password.from.link.error")).when(userService).changePasswordFromLink(anyString(), anyString(), any(ChangePasswordRequest.class));

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/change")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", uuid)
                .param("usr", userId)
                .content(asJsonString(changePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(userService, times(1)).validateUserPassword(anyString(), anyString());
        verify(userService, times(1)).changePasswordFromLink(anyString(), anyString(), any(ChangePasswordRequest.class));
    }
}
