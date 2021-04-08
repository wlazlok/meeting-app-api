package meeting.app.api.model.category;

import lombok.Data;
import meeting.app.api.model.utils.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryItemResponse {

    private Long id;
    private String name;
    private String cloudinaryId;
    private List<ErrorMessage> errorMessages = new ArrayList<>();

}
