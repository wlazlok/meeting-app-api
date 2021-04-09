package meeting.app.api.model.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import meeting.app.api.model.utils.ErrorMessage;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class CartCategoryItemResponse {

    private List<CartCategoryItem> cartCategoryItems;

    private ErrorMessage errorMessage;
}
