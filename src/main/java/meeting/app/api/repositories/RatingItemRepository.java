package meeting.app.api.repositories;

import meeting.app.api.model.rating.RatingItem;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface RatingItemRepository  extends CrudRepository<RatingItem, Long> {
}
