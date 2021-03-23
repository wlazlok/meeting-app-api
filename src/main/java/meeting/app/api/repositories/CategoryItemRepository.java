package meeting.app.api.repositories;

import meeting.app.api.model.category.CategoryItem;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface CategoryItemRepository extends CrudRepository<CategoryItem, Long> {

}
