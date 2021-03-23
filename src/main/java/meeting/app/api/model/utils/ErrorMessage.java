package meeting.app.api.model.utils;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorMessage {

    String errorCode;
    String errorMessage;
    HttpStatus status;
}
