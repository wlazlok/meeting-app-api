package meeting.app.api.controllers;

import meeting.app.api.model.category.CartCategoryItemResponse;
import meeting.app.api.model.category.CategoryItemResponse;
import meeting.app.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static meeting.app.api.utils.HandleErrorMessage.mapErrorMessage;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Method to get all categories
     *
     * @return list of all categories
     */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<List<CategoryItemResponse>> getCategories() {
        CategoryItemResponse categoryItemResponse = new CategoryItemResponse();
        try {
            List<CategoryItemResponse> categories = categoryService.getAllCategories();
            return ResponseEntity.ok().body(categories);
        } catch (Exception ex) {
            categoryItemResponse.setErrorMessages(Arrays.asList(mapErrorMessage(ex)));
            return ResponseEntity.badRequest().body(Arrays.asList(categoryItemResponse));
        }
    }

    @GetMapping("/get/carts")
    public ResponseEntity<CartCategoryItemResponse> getCartCategories() {
        CartCategoryItemResponse response = new CartCategoryItemResponse();
        try {
            response.setCartCategoryItems(categoryService.getAllCartCategoryItems());
            return  ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            response.setErrorMessage(mapErrorMessage(ex));
            return ResponseEntity.badRequest().body(response);
        }
    }
}
