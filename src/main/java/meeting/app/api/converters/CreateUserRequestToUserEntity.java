package meeting.app.api.converters;

import meeting.app.api.configuration.security.Role;
import meeting.app.api.model.user.CreateUserRequest;
import meeting.app.api.model.user.UserEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestToUserEntity implements Converter<CreateUserRequest, UserEntity> {

    @Override
    public UserEntity convert(CreateUserRequest createUserRequest) {
        return UserEntity.builder()
                .password(createUserRequest.getPassword())
                .username(createUserRequest.getUsername())
                .email(createUserRequest.getEmail())
                .isEnabled(false)
                .role(Role.USER)
                .build();
    }
}
