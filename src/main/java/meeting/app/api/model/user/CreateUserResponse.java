package meeting.app.api.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import meeting.app.api.model.utils.ErrorMessage;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {

    private ErrorMessage errorMessage;
}
