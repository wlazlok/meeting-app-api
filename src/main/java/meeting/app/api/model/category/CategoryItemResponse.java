package meeting.app.api.model.category;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import meeting.app.api.model.utils.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryItemResponse {

    private Long id;
    private String name;
    private byte[] image;
    private List<ErrorMessage> errorMessages = new ArrayList<>();

}
