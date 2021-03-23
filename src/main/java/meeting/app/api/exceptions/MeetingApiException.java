package meeting.app.api.exceptions;

public class MeetingApiException extends RuntimeException {

    public MeetingApiException() {};

    public MeetingApiException(String msg) {super(msg);}
}
