package meeting.app.api.mocks;

import meeting.app.api.model.category.CartCategoryItem;
import meeting.app.api.model.category.CartCategoryItemResponse;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.comment.CommentItemRequest;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.event.EventItemListElement;
import meeting.app.api.model.event.EventItemResponse;
import meeting.app.api.model.rating.RatingItem;
import meeting.app.api.model.user.*;

import java.util.ArrayList;
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

    public static RatingItem generateRatingItem() {
        EventItem eventItem = generateEventItem();
        UserEntity userEntity = generateUserEntity();

        return RatingItem.builder()
                .rating(5)
                .eventItem(eventItem)
                .userEntity(userEntity)
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
                .ratings(new ArrayList<>())
                .activeParticipants(new ArrayList<>())
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
                .email("test@email.com")
                .events(new ArrayList<>())
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
    
    public static EventItemResponse generateEventItemResponse() {
        EventItem eventItem = generateEventItem();

        return EventItemResponse.builder()
                .eventItem(Arrays.asList(eventItem))
                .errorMessage(null)
                .build();
    }
    
    public static CreateUserResponse generateCreateUserResponse() {
        return new CreateUserResponse();
    }

    public static CreateUserRequest generateCreateUserRequest() {
        return CreateUserRequest.builder()
                .username("test")
                .email("test@email.com")
                .password("test")
                .confirmPassword("test")
                .build();
    }

    public static ResetPasswordRequest generateResetPasswordRequest() {
        return ResetPasswordRequest.builder()
                .username("test")
                .email("test@email.com")
                .build();
    }

    public static ChangePasswordRequest generateChangePasswordRequest() {
        return ChangePasswordRequest.builder()
                .username("test")
                .password("test123")
                .confirmPassword("test123")
                .build();
    }
}
