package meeting.app.api.services;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.converters.CategoryItemToCategoryItemResponse;
import meeting.app.api.exceptions.MeetingApiException;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;
import meeting.app.api.repositories.CategoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CategoryService {

    @Autowired
    private CategoryItemRepository categoryItemRepository;

    @Autowired
    private CategoryItemToCategoryItemResponse categoryItemToCategoryItemResponse;

    public CategoryItemResponse saveCategory(CategoryItem categoryItem) {
        CategoryItem savedCategory;
        try {
            savedCategory = categoryItemRepository.save(categoryItem);
        } catch (Exception e) {
            log.info("category.service.save.exception: " + e.getMessage());
            throw new MeetingApiException("msg.err.category.save.exception");
        }

        return categoryItemToCategoryItemResponse.convert(savedCategory);
    }

    public List<CategoryItemResponse> getAllCategories() {
        List<CategoryItemResponse> categories = new ArrayList<>();

        try {
            Iterable<CategoryItem> categoriesFound = categoryItemRepository.findAll();
            for (CategoryItem categoryItem : categoriesFound) {
                categories.add(categoryItemToCategoryItemResponse.convert(categoryItem));
            }
        } catch (Exception e) {
            log.info("category.service.getCategories.exception: " + e.getMessage());
            throw new MeetingApiException("msg.err.category.get.categories.exception");
        }

        return categories;
    }
}
