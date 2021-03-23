package meeting.app.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MeetingApiException.class)
    public ResponseEntity<Object> handleMeetingApiException(String msg) {
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }
}
