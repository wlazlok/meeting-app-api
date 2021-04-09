package meeting.app.api.mocks;

import meeting.app.api.model.category.CartCategoryItem;
import meeting.app.api.model.category.CartCategoryItemResponse;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.event.EventItem;

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

}
