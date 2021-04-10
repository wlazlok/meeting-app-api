package meeting.app.api.services;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.exceptions.MeetingApiException;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

    public UserEntity getUserById(Long id) {
        return userRepository.getById(id);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
