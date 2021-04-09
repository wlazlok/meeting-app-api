package meeting.app.api.boostrap;

import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.repositories.CategoryItemRepository;
import meeting.app.api.repositories.CommentItemRepository;
import meeting.app.api.repositories.EventItemRepository;
import meeting.app.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
public class LoadFakeData implements CommandLineRunner {

    @Autowired
    private CategoryItemRepository categoryItemRepository;

    @Autowired
    private EventItemRepository eventItemRepository;

    @Autowired
    private CommentItemRepository commentItemRepository;

    @Override
    public void run(String... args) throws Exception {
        CategoryItem categoryItem = loadCategoryItem();
        loadEventItemWithComment(categoryItem);
    }

    public CategoryItem loadCategoryItem() {
        CategoryItem categoryItem = new CategoryItem();

        categoryItem.setName("Test Category");
        categoryItem.setCloudinaryId("gjsdfsvnsephtuz57xww");

        return categoryItemRepository.save(categoryItem);
    }

    public void loadEventItemWithComment(CategoryItem categoryItem) {
        CommentItem commentItem = CommentItem.builder()
                .date(new Date())
                .content("content")
                .build();

        EventItem eventItem = EventItem.builder()
                .city("Krakow")
                .street("Test 11")
                .date(new Date())
                .description("description")
                .active(true)
                .maxParticipants(5)
                .comments(Arrays.asList(commentItem))
                .build();

        commentItemRepository.save(commentItem);
        eventItemRepository.save(eventItem);

        commentItem.setEventItem(eventItem);

        commentItemRepository.save(commentItem);

        categoryItem.setEvents(Arrays.asList(eventItem));

        categoryItemRepository.save(categoryItem);

        eventItem.setCategoryId(categoryItem);

        eventItemRepository.save(eventItem);
    }
}
