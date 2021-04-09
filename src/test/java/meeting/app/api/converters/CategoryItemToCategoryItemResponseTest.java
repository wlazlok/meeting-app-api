package meeting.app.api.converters;

import meeting.app.api.ConverterConfig;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static meeting.app.api.mocks.MockModel.generateCategoryItem;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryItemToCategoryItemResponseTest extends ConverterConfig {

    @Test
    void convertWithEvents() {
        CategoryItem categoryItem = generateCategoryItem();

        CategoryItemResponse categoryItemResponse = categoryItemToCategoryItemResponse.convert(categoryItem);

        assertNotNull(categoryItemResponse);
        assertEquals(categoryItem.getId(), categoryItemResponse.getId());
        assertEquals(categoryItem.getName(), categoryItemResponse.getName());
        assertEquals(categoryItem.getCloudinaryId(), categoryItemResponse.getCloudinaryId());
        assertNotNull(categoryItemResponse.getEvents());
        assertFalse(categoryItemResponse.getEvents().isEmpty());
    }

    @Test
    void convertWithoutEvents() {
        CategoryItem categoryItem = generateCategoryItem();
        categoryItem.setEvents(Collections.emptyList());

        CategoryItemResponse categoryItemResponse = categoryItemToCategoryItemResponse.convert(categoryItem);

        assertNotNull(categoryItemResponse);
        assertEquals(categoryItem.getId(), categoryItemResponse.getId());
        assertEquals(categoryItem.getName(), categoryItemResponse.getName());
        assertEquals(categoryItem.getCloudinaryId(), categoryItemResponse.getCloudinaryId());
        assertNotNull(categoryItemResponse.getEvents());
        assertTrue(categoryItemResponse.getEvents().isEmpty());
    }
}
