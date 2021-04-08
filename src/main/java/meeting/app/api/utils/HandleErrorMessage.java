package meeting.app.api.utils;

import meeting.app.api.model.utils.ErrorMessage;
import org.springframework.http.HttpStatus;
import java.util.Locale;
import java.util.ResourceBundle;

public class HandleErrorMessage {

    public static ErrorMessage mapErrorMessage(Exception ex) {
        final Locale locale = new Locale("pl", "PL");
        final ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        String errorMessage = "";

        try {
            errorMessage = bundle.getString(ex.getMessage());
        } catch (Exception e) {
            errorMessage = "Niezidentyfikowany błąd";
        }

        return new ErrorMessage().builder()
                .errorMessage(errorMessage)
                .errorCode(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
