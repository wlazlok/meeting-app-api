package meeting.app.api.boostrap;

import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadFakeData implements CommandLineRunner {

    @Autowired
    private CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        loadCategoryItem();
    }

    public void loadCategoryItem() {
        CategoryItem categoryItem = new CategoryItem();

        categoryItem.setName("Test Category");
        categoryItem.setCloudinaryId("gjsdfsvnsephtuz57xww");

        categoryService.saveCategory(categoryItem);
    }
}
