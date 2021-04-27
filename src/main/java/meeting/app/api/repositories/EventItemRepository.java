package meeting.app.api.repositories;

import meeting.app.api.model.event.EventItem;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface EventItemRepository extends CrudRepository<EventItem, Long> {

    EventItem getById(Long id);
}
