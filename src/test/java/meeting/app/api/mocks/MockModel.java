package meeting.app.api.mocks;

import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;

public class MockModel {

    public static CategoryItem generateCategoryItem() {
        CategoryItem categoryItem = new CategoryItem();

        categoryItem.setId(1L);
        categoryItem.setName("Category Item");
        categoryItem.setImage(new byte[]{11, 12, 13, 14, 15});

        return categoryItem;
    }

    public static CategoryItemResponse generateCategoryItemResponse() {
        CategoryItemResponse categoryItemResponse = new CategoryItemResponse();

        categoryItemResponse.setId(1L);
        categoryItemResponse.setName("Category Item");
        categoryItemResponse.setImage(new byte[]{11, 12, 13, 14, 15});

        return categoryItemResponse;
    }

}
