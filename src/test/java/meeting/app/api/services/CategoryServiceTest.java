package meeting.app.api.services;

import meeting.app.api.ServiceMockConfig;
import meeting.app.api.model.category.CartCategoryItem;
import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.model.category.CategoryItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static meeting.app.api.mocks.MockModel.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CategoryServiceTest extends ServiceMockConfig {

    @InjectMocks
    private CategoryService underTest;

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
        when(categoryItemToCategoryItemResponse.convert(any())).thenReturn(converterCategory);

        CategoryItemResponse savedCategory = underTest.saveCategory(any());

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
            underTest.saveCategory(any());
            fail("Should throw exception");
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
        when(categoryItemToCategoryItemResponse.convert(any())).thenReturn(categoryItemResponse);

        List<CategoryItemResponse> responses = underTest.getAllCategories();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(categoryItemResponse, responses.get(0));

        verify(categoryItemRepository, times(1)).findAll();
        verify(categoryItemToCategoryItemResponse, times(1)).convert(any());
    }

    @Test
    void getAllCategoriesThrowException() {
        given(categoryItemRepository.findAll()).willAnswer(invocationOnMock -> {
            throw new Exception("error");
        });

        try {
            underTest.getAllCategories();
            fail("Should throw exception");
        } catch (Exception ex) {
            assertNotNull(ex);
            assertEquals("msg.err.category.get.categories.exception", ex.getMessage());
        }

        verify(categoryItemRepository, times(1)).findAll();
        verify(categoryItemToCategoryItemResponse, times(0)).convert(any());
    }

    /**
     * getAllCartCategoryItems() method
     * 1. getAllCartCategoryItems OK
     * 2. getAllCartCategoryItems throw Exception
     */

    @Test
    void getAllCartCategoryItems() {
        CategoryItem categoryItem = generateCategoryItem();
        CartCategoryItem cartCategoryItem = generateCartCategoryItem();
        Iterable<CategoryItem> categories = Arrays.asList(categoryItem);

        when(categoryItemRepository.findAll()).thenReturn(categories);
        when(categoryItemToCartCategoryItem.convert(any(CategoryItem.class))).thenReturn(cartCategoryItem);

        List<CartCategoryItem> cartCategories = underTest.getAllCartCategoryItems();

        assertNotNull(cartCategories);
        assertFalse(cartCategories.isEmpty());
        verify(categoryItemRepository, times(1)).findAll();
        verify(categoryItemToCartCategoryItem, times(1)).convert(any(CategoryItem.class));
    }

    @Test
    void getAllCartCategoryItemsThrowException() {
        given(categoryItemRepository.findAll()).willAnswer(invocationOnMock -> {
            throw new Exception("error");
        });

        try {
            underTest.getAllCartCategoryItems();
            fail("Should throw exception");
        } catch (Exception ex) {
            assertEquals("msg.err.category.get.all.cart.categories", ex.getMessage());
            verify(categoryItemRepository, times(1)).findAll();
            verify(categoryItemToCartCategoryItem, times(0)).convert(any(CategoryItem.class));
        }
    }
}
