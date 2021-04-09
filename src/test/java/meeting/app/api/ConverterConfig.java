package meeting.app.api;

import meeting.app.api.converters.CategoryItemToCartCategoryItem;
import meeting.app.api.converters.CategoryItemToCategoryItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConverterConfig {

    @Autowired
    protected CategoryItemToCartCategoryItem categoryItemToCartCategoryItem;

    @Autowired
    protected CategoryItemToCategoryItemResponse categoryItemToCategoryItemResponse;
}
