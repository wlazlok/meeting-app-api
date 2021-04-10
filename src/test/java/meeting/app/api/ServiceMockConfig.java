package meeting.app.api;

import meeting.app.api.converters.CategoryItemToCartCategoryItem;
import meeting.app.api.converters.CategoryItemToCategoryItemResponse;
import meeting.app.api.converters.EventItemToEventItemListElement;
import meeting.app.api.repositories.CategoryItemRepository;
import meeting.app.api.repositories.CommentItemRepository;
import meeting.app.api.repositories.EventItemRepository;
import meeting.app.api.repositories.UserRepository;
import org.mockito.Mock;

public class ServiceMockConfig {

    @Mock
    protected CategoryItemRepository categoryItemRepository;

    @Mock
    protected CategoryItemToCategoryItemResponse categoryItemToCategoryItemResponse;

    @Mock
    protected CategoryItemToCartCategoryItem categoryItemToCartCategoryItem;

    @Mock
    protected EventItemRepository eventItemRepository;

    @Mock
    protected EventItemToEventItemListElement eventItemToEventItemListElement;

    @Mock
    protected CommentItemRepository commentItemRepository;

    @Mock
    protected UserRepository userRepository;
}
