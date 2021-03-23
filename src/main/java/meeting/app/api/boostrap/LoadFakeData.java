package meeting.app.api.boostrap;

import meeting.app.api.model.category.CategoryItem;
import meeting.app.api.services.CategoryService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Component
public class LoadFakeData implements CommandLineRunner {

    @Autowired
    private CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        loadCategoryItem();
    }

    public void loadCategoryItem() throws IOException {
        CategoryItem categoryItem = new CategoryItem();

        categoryItem.setName("Test Category");
        categoryItem.setImage(loadImage());

        categoryService.saveCategory(categoryItem);
    }

    public byte[] loadImage() throws IOException {
        InputStream imageFile = this.getClass().getResourceAsStream("/images/running.jpg");
        return IOUtils.toByteArray(imageFile);
    }
}
