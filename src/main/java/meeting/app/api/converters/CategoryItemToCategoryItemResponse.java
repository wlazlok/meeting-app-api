package meeting.app.api.converters;

import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryItemToCategoryItemResponse implements Converter<CategoryItem, CategoryItemResponse> {

    @Override
    public CategoryItemResponse convert(CategoryItem categoryItem) {
        CategoryItemResponse categoryItemResponse = new CategoryItemResponse();

        categoryItemResponse.setId(categoryItem.getId());
        categoryItemResponse.setName(categoryItem.getName());
        categoryItemResponse.setCloudinaryId(categoryItem.getCloudinaryId());

        return categoryItemResponse;
    }
}
