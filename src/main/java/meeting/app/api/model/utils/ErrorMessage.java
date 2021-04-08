package meeting.app.api.model.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class ErrorMessage {

    private String errorCode;
    private String errorMessage;
    private HttpStatus status;
}
