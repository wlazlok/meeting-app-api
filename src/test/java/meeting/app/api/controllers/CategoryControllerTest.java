package meeting.app.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.app.api.ControllerMockConfig;
import meeting.app.api.model.category.CartCategoryItem;
import meeting.app.api.model.category.CartCategoryItemResponse;
import meeting.app.api.model.category.CategoryItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static meeting.app.api.mocks.MockModel.generateCartCategoryItem;
import static meeting.app.api.mocks.MockModel.generateCategoryItemResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest extends ControllerMockConfig {

    private final String PATH = "/api/category";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @InjectMocks
    private CategoryController controller;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * getCategories() method
     * 1. getCategories OK
     * 2. getCategories throw Exception
     */

    @Test
    void getCategories() throws Exception {
        CategoryItemResponse categoryItemResponse = generateCategoryItemResponse();

        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(categoryItemResponse));

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/get")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<CategoryItemResponse> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoriesThrowException() throws Exception {

        given(categoryService.getAllCategories()).willAnswer(invocationOnMock -> {
            throw new Exception("error");
        });

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/get")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        List<CategoryItemResponse> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertNotNull(response);
        verify(categoryService, times(1)).getAllCategories();
    }

    /**
     * getCartCategories() method
     * 1. getCartCategories OK
     * 2. getCartCategories throw Exception
     */

    @Test
    void getCartCategories() throws Exception {
        CartCategoryItem cartCategoryItem = generateCartCategoryItem();

        when(categoryService.getAllCartCategoryItems()).thenReturn(Arrays.asList(cartCategoryItem));

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/get/carts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        CartCategoryItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CartCategoryItemResponse.class);

        assertNotNull(response);
        assertFalse(response.getCartCategoryItems().isEmpty());
        assertNull(response.getErrorMessage());
        verify(categoryService, times(1)).getAllCartCategoryItems();
    }

    @Test
    void getCartCategoriesThrowException() throws Exception {
        given(categoryService.getAllCartCategoryItems()).willAnswer(invocationOnMock -> {
            throw new Exception();
        });

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/get/carts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        CartCategoryItemResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CartCategoryItemResponse.class);

        assertNotNull(response);
        assertNull(response.getCartCategoryItems());
        assertNotNull(response.getErrorMessage());
        verify(categoryService, times(1)).getAllCartCategoryItems();
    }
}
