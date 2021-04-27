package meeting.app.api.mocks;

import meeting.app.api.model.category.CartCategoryItem;
import meeting.app.api.model.category.CartCategoryItemResponse;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.comment.CommentItemRequest;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import meeting.app.api.model.user.UserEntity;

import java.util.Arrays;
import java.util.Date;

public class MockModel {

    public static CategoryItem generateCategoryItem() {
        CategoryItem categoryItem = new CategoryItem();

        categoryItem.setId(1L);
        categoryItem.setName("Category Item");
        categoryItem.setCloudinaryId("gjsdfsvnsephtuz57xww");
        categoryItem.setEvents(Arrays.asList(new EventItem()));

        return categoryItem;
    }

    public static CategoryItemResponse generateCategoryItemResponse() {
        CategoryItemResponse categoryItemResponse = new CategoryItemResponse();

        categoryItemResponse.setId(1L);
        categoryItemResponse.setName("Category Item");
        categoryItemResponse.setCloudinaryId("gjsdfsvnsephtuz57xww");
        categoryItemResponse.setEvents(Arrays.asList(generateEventItem()));

        return categoryItemResponse;
    }

    public static CartCategoryItem generateCartCategoryItem() {
        return CartCategoryItem.builder()
                .id(10L)
                .name("CartCategoryItem")
                .cloudinaryId("gjsdfsvnsephtuz57xww")
                .build();
    }

    public static EventItem generateEventItem() {
        return EventItem.builder()
                .id(10L)
                .city("Test")
                .street("Testowa")
                .date(new Date())
                .description("Description")
                .active(true)
                .maxParticipants(5)
                .comments(Arrays.asList(new CommentItem()))
                .categoryId(generateCategoryItem())
                .build();
    }

    public static CartCategoryItemResponse generateCartCategoryItemResponse() {
        return CartCategoryItemResponse.builder()
                .cartCategoryItems(Arrays.asList(generateCartCategoryItem()))
                .build();
    }

    public static EventItemListElement generateEventItemListElement() {
        return EventItemListElement.builder()
                .id(5L)
                .date(new Date())
                .city("Cracow")
                .maxParticipants(5)
                .build();
    }

    public static UserEntity generateUserEntity() {
        return UserEntity.builder()
                .password("test")
                .username("test")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
    }

    public static CommentItem generateCommentItem() {
        EventItem eventItem = generateEventItem();
        UserEntity userEntity = generateUserEntity();

        return CommentItem.builder()
                .date(new Date())
                .userEntity(userEntity)
                .content("content")
                .eventItem(eventItem)
                .build();
    }

    public static CommentItemRequest generateCommentItemRequest() {
        return CommentItemRequest.builder()
                .content("test")
                .eventId(5L)
                .build();
    }
}
