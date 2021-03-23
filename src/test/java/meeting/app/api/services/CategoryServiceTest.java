package meeting.app.api.services;

import meeting.app.api.converters.CategoryItemToCategoryItemResponse;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;
import meeting.app.api.repositories.CategoryItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static meeting.app.api.mocks.MockModel.generateCategoryItem;
import static meeting.app.api.mocks.MockModel.generateCategoryItemResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryItemRepository categoryItemRepository;

    @Mock
    private CategoryItemToCategoryItemResponse converter;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * saveCategory() method
     * 1. saveCategory OK
     * 2. saveCategory throw Exception
     */

    @Test
    void saveCategory() {
        CategoryItem categoryItemResponse = generateCategoryItem();
        CategoryItemResponse converterCategory = generateCategoryItemResponse();

        when(categoryItemRepository.save(any())).thenReturn(categoryItemResponse);
        when(converter.convert(any())).thenReturn(converterCategory);

        CategoryItemResponse savedCategory = categoryService.saveCategory(any());

        assertNotNull(savedCategory);
        assertEquals(converterCategory, savedCategory);
        verify(categoryItemRepository, times(1)).save(any());
    }

    @Test
    void saveCategoryThrowException() {
        given(categoryItemRepository.save(any())).willAnswer(invocationOnMock -> {
            throw new Exception("error");
        });

        try {
            categoryService.saveCategory(any());
        } catch (Exception ex) {
            assertNotNull(ex);
            assertEquals("msg.err.category.save.exception", ex.getMessage());
        }

        verify(categoryItemRepository, times(1)).save(any());
    }

    /**
     * getAllCategories() method
     * 1. getAllCategories OK
     * 2. getAllCategories throw Exception
     */

    @Test
    void getAllCategories() {
        CategoryItemResponse categoryItemResponse = generateCategoryItemResponse();
        CategoryItem categoryItem = generateCategoryItem();
        Iterable<CategoryItem> iterable = Arrays.asList(categoryItem);

        when(categoryItemRepository.findAll()).thenReturn(iterable);
        when(converter.convert(any())).thenReturn(categoryItemResponse);

        List<CategoryItemResponse> responses = categoryService.getAllCategories();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(categoryItemResponse, responses.get(0));

        verify(categoryItemRepository, times(1)).findAll();
        verify(converter, times(1)).convert(any());
    }

    @Test
    void getAllCategoriesThrowException() {
        given(categoryItemRepository.findAll()).willAnswer(invocationOnMock -> {
            throw new Exception("error");
        });

        try {
            categoryService.getAllCategories();
        } catch (Exception ex) {
            assertNotNull(ex);
            assertEquals("msg.err.category.get.categories.exception", ex.getMessage());
        }

        verify(categoryItemRepository, times(1)).findAll();
        verify(converter, times(0)).convert(any());
    }
}
