package meeting.app.api.mocks;

import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;

public class MockModel {

    public static CategoryItem generateCategoryItem() {
        CategoryItem categoryItem = new CategoryItem();

        categoryItem.setId(1L);
        categoryItem.setName("Category Item");
        categoryItem.setCloudinaryId("gjsdfsvnsephtuz57xww");

        return categoryItem;
    }

    public static CategoryItemResponse generateCategoryItemResponse() {
        CategoryItemResponse categoryItemResponse = new CategoryItemResponse();

        categoryItemResponse.setId(1L);
        categoryItemResponse.setName("Category Item");
        categoryItemResponse.setCloudinaryId("gjsdfsvnsephtuz57xww");

        return categoryItemResponse;
    }

}
