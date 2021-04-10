package meeting.app.api.repositories;

import meeting.app.api.model.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity getByUsername(String username);

    UserEntity getById(Long id);
}
