package meeting.app.api.repositories;

import meeting.app.api.model.comment.CommentItem;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface CommentItemRepository extends CrudRepository<CommentItem, Long> {
}
