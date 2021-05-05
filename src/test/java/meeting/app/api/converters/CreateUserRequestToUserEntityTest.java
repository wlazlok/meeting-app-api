package meeting.app.api.converters;

import meeting.app.api.ConverterConfig;
import meeting.app.api.configuration.security.Role;
import meeting.app.api.model.user.CreateUserRequest;
import meeting.app.api.model.user.UserEntity;
import org.junit.jupiter.api.Test;

import static meeting.app.api.mocks.MockModel.generateCreateUserRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateUserRequestToUserEntityTest extends ConverterConfig {

    @Test
    void convert() {
        CreateUserRequest createUserRequest = generateCreateUserRequest();

        UserEntity userEntity = createUserRequestToUserEntity.convert(createUserRequest);

        assertNotNull(userEntity);
        assertEquals(createUserRequest.getPassword(), userEntity.getPassword());
        assertEquals(createUserRequest.getUsername(), userEntity.getUsername());
        assertEquals(createUserRequest.getEmail(), userEntity.getEmail());
        assertEquals(Role.USER, userEntity.getRole());
    }
}
