package meeting.app.api.model.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import meeting.app.api.model.utils.ErrorMessage;
import java.util.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentItemResponse {

    private List<CommentItem> commentItemList;

    private ErrorMessage errorMessage;
}
