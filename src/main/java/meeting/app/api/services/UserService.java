package meeting.app.api.services;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.converters.CreateUserRequestToUserEntity;
import meeting.app.api.exceptions.MeetingApiException;
import meeting.app.api.model.user.ChangePasswordRequest;
import meeting.app.api.model.user.CreateUserRequest;
import meeting.app.api.model.user.ResetPasswordRequest;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.repositories.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
            userEntity.setAccountNonLocked(true);
            userEntity.setCredentialsNonExpired(true);
            userEntity.setActivateAccountUUID(null);
            userEntity.setAccountNonExpired(true);
            userRepository.save(userEntity);
        } catch (Exception ex) {
            log.info("user.service.activate.user.exception " + ex.getMessage());
            throw new MeetingApiException("msg.err.activate.user");
        }
    }

    public UserEntity resetPassword(ResetPasswordRequest request) {
        Optional<UserEntity> user = userRepository.findByUsername(request.getUsername());

        if (user.isEmpty()) {
            throw new MeetingApiException("msg.err.user.not.found");
        }

        if (!user.get().getUsername().equals(request.getUsername())) {
            throw new MeetingApiException("msg.err.email.not.equal");
        }

        String newPassword = generateRandomPassword();

        try {
            UUID uuid = UUID.randomUUID();
            user.get().setPassword(passwordEncoder.encode(newPassword));
            user.get().setResetPasswordUUID(uuid);
            return userRepository.save(user.get());
        } catch (Exception ex) {
            log.info("user.service.reset.password.and.send.email.user.exception " + ex.getMessage());
            throw new MeetingApiException("msg.err.reset.password");
        }
    }

    public void changePasswordFromLink(String uuid, String userId, ChangePasswordRequest request) {
        UserEntity userEntity = userRepository.getById(Long.valueOf(userId));

        if (userEntity == null) {
            throw new MeetingApiException("msg.err.user.not.found");
        }
        if (!userEntity.getUsername().equals(request.getUsername())) {
            throw new MeetingApiException("msg.err.incorrect.username");
        }
        if (userEntity.getResetPasswordUUID() == null || !uuid.equals(userEntity.getResetPasswordUUID().toString())) {
            throw new MeetingApiException("msg.err.user.change.password.no.request");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new MeetingApiException("msg.err.invalid.password");
        }

        try {
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            userEntity.setResetPasswordUUID(null);
            userRepository.save(userEntity);
        } catch (Exception ex) {
            log.info("msg.err.change.password.from.link.exception " + ex.getMessage());
            throw new MeetingApiException("msg.err.change.password.from.link.error");
        }
    }

    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        return RandomStringUtils.random( 15, characters );
    }

    public UserEntity getUserById(Long id) {
        return userRepository.getById(id);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
