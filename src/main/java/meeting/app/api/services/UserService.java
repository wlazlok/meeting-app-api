package meeting.app.api.services;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.converters.CreateUserRequestToUserEntity;
import meeting.app.api.exceptions.MeetingApiException;
import meeting.app.api.model.user.CreateUserRequest;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateUserRequestToUserEntity createUserRequestToUserEntity;

    public UserEntity getUserFromContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return getUserById(((UserEntity) principal).getId());
        } else {
            log.info("user.service.get.user.context.exception");
            throw new MeetingApiException("msg.err.get.user.from.context.error");
        }
    }

    public UserEntity getUserFromContextJWT() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            return getUserByUsername((String) principal);
        } else {
            log.info("user.service.get.user.context.exception");
            throw new MeetingApiException("msg.err.get.user.from.context.error");
        }
    }

    public boolean validateUserPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword) && password.matches(pattern);
    }

    public UserEntity createUser(CreateUserRequest createUserRequest) {
        List<String> usernames = userRepository.getAllByUsername(createUserRequest.getUsername())
                .stream()
                .map(UserEntity::getUsername)
                .collect(Collectors.toList());

        if (usernames.contains(createUserRequest.getUsername())) {
            throw new MeetingApiException("username.already.exist");
        }

        try {
            UUID uuid = UUID.randomUUID();
            createUserRequest.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
            UserEntity userEntity = createUserRequestToUserEntity.convert(createUserRequest);
            userEntity.setActivateAccountUUID(uuid);
            return userRepository.save(userEntity);
        } catch (Exception ex) {
            log.info("user.service.create.user.exception " + ex.getMessage());
            throw new MeetingApiException("msg.err.create.user.error");
        }
    }

    public void activateUserAccount(String uuid, String userId) {
        UserEntity userEntity = userRepository.getById(Long.valueOf(userId));

        if (userEntity == null) {
            throw new MeetingApiException("msg.err.user.not.found");
        }
        if (userEntity.getActivateAccountUUID() == null) {
            throw new MeetingApiException("msg.err.user.is.activated");
        }
        if (!uuid.equals(userEntity.getActivateAccountUUID().toString())) {
            throw new MeetingApiException("msg.err.incorrect.activate.link");
        }
        try {
            userEntity.setEnabled(true);
            userEntity.setActivateAccountUUID(null);
            userRepository.save(userEntity);
        } catch (Exception ex) {
            log.info("user.service.activate.user.exception " + ex.getMessage());
            throw new MeetingApiException("msg.err.activate.user");
        }
    }

    public UserEntity getUserById(Long id) {
        return userRepository.getById(id);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
