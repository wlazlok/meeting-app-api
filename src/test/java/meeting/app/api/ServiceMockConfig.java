package meeting.app.api;

import meeting.app.api.converters.CategoryItemToCartCategoryItem;
import meeting.app.api.converters.CategoryItemToCategoryItemResponse;
import meeting.app.api.repositories.CategoryItemRepository;
import org.mockito.Mock;

public class ServiceMockConfig {

    @Mock
    protected CategoryItemRepository categoryItemRepository;

    @Mock
    protected CategoryItemToCategoryItemResponse categoryItemToCategoryItemResponse;

    @Mock
    protected CategoryItemToCartCategoryItem categoryItemToCartCategoryItem;
}
