package meeting.app.api.services;

import meeting.app.api.ServiceMockConfig;
import meeting.app.api.model.user.UserEntity;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static meeting.app.api.mocks.MockModel.generateUserEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Ignore
    @Test
    void getUserFromContext() {
        //todo
    }
}
