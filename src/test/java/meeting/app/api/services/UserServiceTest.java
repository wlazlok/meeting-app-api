package meeting.app.api.services;

import meeting.app.api.ServiceMockConfig;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import meeting.app.api.model.user.ChangePasswordRequest;
import meeting.app.api.model.user.CreateUserRequest;
import meeting.app.api.model.user.ResetPasswordRequest;
import meeting.app.api.model.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static meeting.app.api.mocks.MockModel.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends ServiceMockConfig {

    @InjectMocks
    private UserService underTest;

    /**
     * getUserById() method
     */

    @Test
    void getUserById() {
        UserEntity userEntity = generateUserEntity();
        Long userId = 5L;

        when(userRepository.getById(anyLong())).thenReturn(userEntity);

        UserEntity result = underTest.getUserById(userId);

        assertNotNull(result);
        assertEquals(userEntity, result);
        verify(userRepository, times(1)).getById(anyLong());
    }

    /**
     * createUser()
     */

    @Test
    void createUserOk() {
        CreateUserRequest createUserRequest = generateCreateUserRequest();
        createUserRequest.setUsername("test123");
        UserEntity userEntity = generateUserEntity();

        when(userRepository.getAllByUsername(anyString())).thenReturn(Arrays.asList(userEntity));
        when(createUserRequestToUserEntity.convert(any(CreateUserRequest.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity result = underTest.createUser(createUserRequest);

        assertNotNull(result);
        verify(userRepository, times(1)).getAllByUsername(anyString());
        verify(createUserRequestToUserEntity, times(1)).convert(any(CreateUserRequest.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void createUserUsernameExist() {
        CreateUserRequest createUserRequest = generateCreateUserRequest();
        UserEntity userEntity = generateUserEntity();

        when(userRepository.getAllByUsername(anyString())).thenReturn(Arrays.asList(userEntity));

        try {
            underTest.createUser(createUserRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getAllByUsername(anyString());
            verify(createUserRequestToUserEntity, times(0)).convert(any(CreateUserRequest.class));
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void createUserThrowException() {
        CreateUserRequest createUserRequest = generateCreateUserRequest();
        createUserRequest.setUsername("test123");
        UserEntity userEntity = generateUserEntity();

        when(userRepository.getAllByUsername(anyString())).thenReturn(Arrays.asList(userEntity));
        given(createUserRequestToUserEntity.convert(any(CreateUserRequest.class))).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        try {
            underTest.createUser(createUserRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getAllByUsername(anyString());
            verify(createUserRequestToUserEntity, times(1)).convert(any(CreateUserRequest.class));
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    /**
     * activateUserAccount()
     */

    @Test
    void activateUserAccountOk() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        UserEntity userEntity = generateUserEntity();
        userEntity.setActivateAccountUUID(uuid);

        when(userRepository.getById(anyLong())).thenReturn(userEntity);

        underTest.activateUserAccount(uuid.toString(), userId);

        verify(userRepository, times(1)).getById(anyLong());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void activateUserAccountUserEntityIsNull() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        UserEntity userEntity = generateUserEntity();
        userEntity.setActivateAccountUUID(uuid);

        when(userRepository.getById(anyLong())).thenReturn(null);

        try {
            underTest.activateUserAccount(uuid.toString(), userId);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getById(anyLong());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void activateUserUUIDIsNull() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        UserEntity userEntity = generateUserEntity();
        userEntity.setActivateAccountUUID(null);

        when(userRepository.getById(anyLong())).thenReturn(userEntity);

        try {
            underTest.activateUserAccount(uuid.toString(), userId);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getById(anyLong());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void activateUserUUIDNotEqual() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        UserEntity userEntity = generateUserEntity();
        userEntity.setActivateAccountUUID(UUID.randomUUID());

        when(userRepository.getById(anyLong())).thenReturn(userEntity);

        try {
            underTest.activateUserAccount(uuid.toString(), userId);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getById(anyLong());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void activateUserThrowException() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        UserEntity userEntity = generateUserEntity();
        userEntity.setActivateAccountUUID(uuid);

        when(userRepository.getById(anyLong())).thenReturn(userEntity);
        given(userRepository.save(any(UserEntity.class))).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        try {
            underTest.activateUserAccount(uuid.toString(), userId);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getById(anyLong());
            verify(userRepository, times(1)).save(any(UserEntity.class));
        }
    }

    /**
     * resetPassword()
     */

    @Test
    void resetPasswordOk() {
        ResetPasswordRequest resetPasswordRequest = generateResetPasswordRequest();
        UserEntity userEntity = generateUserEntity();
        resetPasswordRequest.setUsername(userEntity.getUsername());
        resetPasswordRequest.setEmail(userEntity.getEmail());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity result = underTest.resetPassword(resetPasswordRequest);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void resetPasswordUserIsEmpty() {
        ResetPasswordRequest resetPasswordRequest = generateResetPasswordRequest();
        UserEntity userEntity = generateUserEntity();
        resetPasswordRequest.setUsername(userEntity.getUsername());
        resetPasswordRequest.setEmail(userEntity.getEmail());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        try {
            underTest.resetPassword(resetPasswordRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).findByUsername(anyString());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void resetPasswordUsernameNotEqual() {
        ResetPasswordRequest resetPasswordRequest = generateResetPasswordRequest();
        UserEntity userEntity = generateUserEntity();
        resetPasswordRequest.setUsername("test54432");
        resetPasswordRequest.setEmail(userEntity.getEmail());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        try {
            underTest.resetPassword(resetPasswordRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).findByUsername(anyString());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void resetPasswordEmailNotEqual() {
        ResetPasswordRequest resetPasswordRequest = generateResetPasswordRequest();
        UserEntity userEntity = generateUserEntity();
        resetPasswordRequest.setUsername(userEntity.getUsername());
        resetPasswordRequest.setEmail("test@5234.com");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        try {
            underTest.resetPassword(resetPasswordRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).findByUsername(anyString());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void resetPasswordThrowException() {
        ResetPasswordRequest resetPasswordRequest = generateResetPasswordRequest();
        UserEntity userEntity = generateUserEntity();
        resetPasswordRequest.setUsername(userEntity.getUsername());
        resetPasswordRequest.setEmail(userEntity.getEmail());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        given(userRepository.save(any(UserEntity.class))).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        try {
            underTest.resetPassword(resetPasswordRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).findByUsername(anyString());
            verify(userRepository, times(1)).save(any(UserEntity.class));
        }
    }

    /**
     * getUserEvents()
     */

    @Test
    void getUserEventsOk() {
        UserEntity userEntity = generateUserEntity();
        EventItem eventItem = generateEventItem();
        userEntity.getEvents().add(eventItem);
        EventItemListElement eventItemListElement = generateEventItemListElement();

        when(eventItemToEventItemListElement.convert(any(EventItem.class))).thenReturn(eventItemListElement);

        List<EventItemListElement> result = underTest.getUserEvents(userEntity);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(eventItemToEventItemListElement, times(1)).convert(any(EventItem.class));
    }

    @Test
    void getUserEventsThrowException() {
        UserEntity userEntity = generateUserEntity();
        EventItem eventItem = generateEventItem();
        userEntity.getEvents().add(eventItem);

        given(eventItemToEventItemListElement.convert(any(EventItem.class))).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        try {
            underTest.getUserEvents(userEntity);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(eventItemToEventItemListElement, times(1)).convert(any(EventItem.class));
        }
    }

    /**
     * getUserByUsername()
     */

    @Test
    void getUserByUsername() {
        String username = "test";
        UserEntity userEntity = generateUserEntity();

        when(userRepository.getByUsername(anyString())).thenReturn(userEntity);

        UserEntity result = underTest.getUserByUsername(username);

        assertNotNull(result);
        verify(userRepository, times(1)).getByUsername(anyString());
    }

    /**
     * changePasswordFromLink()
     */

    @Test
    void changePasswordFromLinkOk() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        ChangePasswordRequest changePasswordRequest = generateChangePasswordRequest();
        UserEntity userEntity = generateUserEntity();
        userEntity.setUsername(changePasswordRequest.getUsername());
        userEntity.setResetPasswordUUID(uuid);

        when(userRepository.getById(anyLong())).thenReturn(userEntity);

        underTest.changePasswordFromLink(uuid.toString(), userId, changePasswordRequest);

        verify(userRepository, times(1)).getById(anyLong());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void changePasswordFromLinkUserEntityIsNull() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        ChangePasswordRequest changePasswordRequest = generateChangePasswordRequest();
        UserEntity userEntity = generateUserEntity();
        userEntity.setUsername(changePasswordRequest.getUsername());
        userEntity.setResetPasswordUUID(uuid);

        when(userRepository.getById(anyLong())).thenReturn(null);

        try {
            underTest.changePasswordFromLink(uuid.toString(), userId, changePasswordRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getById(anyLong());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void changePasswordFromLinkUsernameNotEqual() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        ChangePasswordRequest changePasswordRequest = generateChangePasswordRequest();
        UserEntity userEntity = generateUserEntity();
        userEntity.setUsername("test123890123");
        userEntity.setResetPasswordUUID(uuid);

        when(userRepository.getById(anyLong())).thenReturn(userEntity);

        try {
            underTest.changePasswordFromLink(uuid.toString(), userId, changePasswordRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getById(anyLong());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void changePasswordFromLinkUUIDIsNull() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        ChangePasswordRequest changePasswordRequest = generateChangePasswordRequest();
        UserEntity userEntity = generateUserEntity();
        userEntity.setUsername(userEntity.getUsername());
        userEntity.setResetPasswordUUID(null);

        when(userRepository.getById(anyLong())).thenReturn(userEntity);

        try {
            underTest.changePasswordFromLink(uuid.toString(), userId, changePasswordRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getById(anyLong());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void changePasswordFromLinkPasswordsNotEquals() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        ChangePasswordRequest changePasswordRequest = generateChangePasswordRequest();
        changePasswordRequest.setConfirmPassword("qwerty");
        UserEntity userEntity = generateUserEntity();
        userEntity.setUsername(userEntity.getUsername());
        userEntity.setResetPasswordUUID(uuid);

        when(userRepository.getById(anyLong())).thenReturn(userEntity);

        try {
            underTest.changePasswordFromLink(uuid.toString(), userId, changePasswordRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getById(anyLong());
            verify(userRepository, times(0)).save(any(UserEntity.class));
        }
    }

    @Test
    void changePasswordFromLinkThrowException() {
        UUID uuid = UUID.randomUUID();
        String userId = "5";
        ChangePasswordRequest changePasswordRequest = generateChangePasswordRequest();
        UserEntity userEntity = generateUserEntity();
        userEntity.setUsername(userEntity.getUsername());
        userEntity.setResetPasswordUUID(uuid);

        when(userRepository.getById(anyLong())).thenReturn(userEntity);
        given(userRepository.save(any(UserEntity.class))).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        try {
            underTest.changePasswordFromLink(uuid.toString(), userId, changePasswordRequest);
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(userRepository, times(1)).getById(anyLong());
            verify(userRepository, times(1)).save(any(UserEntity.class));
        }
    }
}
