package meeting.app.api.configuration.security;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = null;

        try {
            user = userRepository.getByUsername(username);
        } catch (Exception ex) {
            log.info("user.service.load.user.exception " + ex.getMessage());
            throw new UsernameNotFoundException("msg.err.load.user.exception");
        }

        if (user == null) {
            throw new UsernameNotFoundException("msg.err.user.not.found");
        } else {
            return user;
        }
    }
}
