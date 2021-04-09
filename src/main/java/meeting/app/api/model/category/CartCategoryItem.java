package meeting.app.api.model.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class CartCategoryItem {

    private Long id;

    private String name;

    private String cloudinaryId;
}
