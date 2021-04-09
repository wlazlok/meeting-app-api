package meeting.app.api.converters;

import meeting.app.api.model.category.CartCategoryItem;
import meeting.app.api.model.category.CategoryItem;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class CategoryItemToCartCategoryItem implements Converter<CategoryItem, CartCategoryItem>{

    @Override
    public CartCategoryItem convert(CategoryItem categoryItem) {

        return CartCategoryItem.builder()
                .id(categoryItem.getId())
                .name(categoryItem.getName())
                .cloudinaryId(categoryItem.getCloudinaryId())
                .build();
    }
}
