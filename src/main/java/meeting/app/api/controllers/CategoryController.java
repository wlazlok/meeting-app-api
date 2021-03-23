package meeting.app.api.controllers;

import meeting.app.api.model.category.CategoryItemResponse;
import meeting.app.api.model.utils.ErrorMessage;
import meeting.app.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<List<CategoryItemResponse>> getCategories() {
        CategoryItemResponse categoryItemResponse = new CategoryItemResponse();

        try {
            List<CategoryItemResponse> categories = categoryService.getAllCategories();
            return ResponseEntity.ok().body(categories);
        } catch (Exception ex) {
            categoryItemResponse.setErrorMessages(Arrays.asList(handleErrorMessage(ex)));
            return ResponseEntity.badRequest().body(Arrays.asList(categoryItemResponse));
        }

    }

    private ErrorMessage handleErrorMessage(Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorMessage(ex.getMessage());
        errorMessage.setErrorCode(ex.getMessage());
        errorMessage.setStatus(HttpStatus.BAD_REQUEST);

        return errorMessage;
    }
}
