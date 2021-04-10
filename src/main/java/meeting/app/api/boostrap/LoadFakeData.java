package meeting.app.api.boostrap;

import meeting.app.api.configuration.security.Role;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.repositories.CategoryItemRepository;
import meeting.app.api.repositories.CommentItemRepository;
import meeting.app.api.repositories.EventItemRepository;
import meeting.app.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        UserEntity userEntity = loadFakeUser();
        CategoryItem categoryItem = loadCategoryItem();
        loadEventItemWithComment(categoryItem, userEntity);
        loadAdminUser();
    }

    public CategoryItem loadCategoryItem() {
        CategoryItem categoryItem = new CategoryItem();

        categoryItem.setName("Test Category");
        categoryItem.setCloudinaryId("gjsdfsvnsephtuz57xww");

        return categoryItemRepository.save(categoryItem);
    }

    public void loadEventItemWithComment(CategoryItem categoryItem, UserEntity userEntity) {
        CommentItem commentItem = CommentItem.builder()
                .date(new Date())
                .content("comment #1")
                .build();

        CommentItem commentItem_2 = CommentItem.builder()
                .date(new Date())
                .content("comment #2")
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

        EventItem eventItem_2 = EventItem.builder()
                .city("Warszawa")
                .street("Warszawska 11")
                .date(new Date())
                .description("description event 2")
                .active(false)
                .maxParticipants(14)
                .build();

        commentItemRepository.save(commentItem);
        eventItemRepository.saveAll(Arrays.asList(eventItem, eventItem_2));

        commentItem.setEventItem(eventItem);
        commentItem_2.setEventItem(eventItem_2);
        commentItem.setUserEntity(userEntity);
        commentItem_2.setUserEntity(userEntity);

        commentItemRepository.saveAll(Arrays.asList(commentItem, commentItem_2));

        categoryItem.setEvents(Arrays.asList(eventItem, eventItem_2));

        categoryItemRepository.save(categoryItem);

        eventItem.setCategoryId(categoryItem);
        eventItem_2.setCategoryId(categoryItem);

        eventItemRepository.saveAll(Arrays.asList(eventItem, eventItem_2));

        userEntity.setComments(Arrays.asList(commentItem, commentItem_2));
        userRepository.save(userEntity);
    }

    public UserEntity loadFakeUser() {
        UserEntity userEntity = UserEntity.builder()
                .password(passwordEncoder.encode("test"))
                .username("test")
                .role(Role.USER)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();

        return userRepository.save(userEntity);
    }

    public void loadAdminUser() {
        UserEntity userEntity = UserEntity.builder()
                .password(passwordEncoder.encode("admin"))
                .username("admin")
                .role(Role.ADMIN)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();

        userRepository.save(userEntity);
    }
}
