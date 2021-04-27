package meeting.app.api;

import meeting.app.api.converters.CategoryItemToCartCategoryItem;
import meeting.app.api.converters.CategoryItemToCategoryItemResponse;
import meeting.app.api.converters.EventItemToEventItemListElement;
import meeting.app.api.repositories.*;
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

    @Mock
    protected RatingItemRepository ratingItemRepository;
}
