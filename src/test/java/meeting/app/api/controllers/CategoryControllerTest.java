package meeting.app.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import meeting.app.api.model.category.CategoryItemResponse;
import meeting.app.api.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static meeting.app.api.mocks.MockModel.generateCategoryItemResponse;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest {

    private final String PATH = "/api/category";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

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
}
