package meeting.app.api;

import meeting.app.api.converters.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConverterConfig {

    @Autowired
    protected CategoryItemToCartCategoryItem categoryItemToCartCategoryItem;

    @Autowired
    protected CategoryItemToCategoryItemResponse categoryItemToCategoryItemResponse;

    @Autowired
    protected EventItemToEventItemListElement eventItemToEventItemListElement;

    @Autowired
    protected CreateUserRequestToUserEntity createUserRequestToUserEntity;
}
