package meeting.app.api.repositories;

import meeting.app.api.model.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity getByUsername(String username);

    UserEntity getById(Long id);

    List<UserEntity> getAllByUsername(String username);

    Optional<UserEntity> findByUsername(String username);
}
