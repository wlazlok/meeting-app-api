package meeting.app.api.converters;

import meeting.app.api.ConverterConfig;
import meeting.app.api.model.category.CartCategoryItem;
import meeting.app.api.model.category.CategoryItem;
import org.junit.jupiter.api.Test;

import static meeting.app.api.mocks.MockModel.generateCategoryItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryItemToCartCategoryItemTest extends ConverterConfig {

    @Test
    void convert() {
        CategoryItem categoryItem = generateCategoryItem();

        CartCategoryItem cartCategoryItem = categoryItemToCartCategoryItem.convert(categoryItem);

        assertNotNull(cartCategoryItem);
        assertEquals(categoryItem.getId(), cartCategoryItem.getId());
        assertEquals(categoryItem.getName(), cartCategoryItem.getName());
        assertEquals(categoryItem.getCloudinaryId(), cartCategoryItem.getCloudinaryId());
    }
}
