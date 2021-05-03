package meeting.app.api.controllers;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.model.user.CreateUserRequest;
import meeting.app.api.model.user.CreateUserResponse;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.model.utils.ErrorMessage;
import meeting.app.api.services.EmailService;
import meeting.app.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static meeting.app.api.utils.HandleErrorMessage.mapErrorCode;
import static meeting.app.api.utils.HandleErrorMessage.mapErrorMessage;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest user) {
        CreateUserResponse response = new CreateUserResponse();

        if (!userService.validateUserPassword(user.getPassword(), user.getConfirmPassword())) {
            response.setErrorMessage(mapErrorCode("msg.err.invalid.password"));
            return ResponseEntity.badRequest().body(response);
        }

        try {
            UserEntity savedUser = userService.createUser(user);
            emailService.sendEmail(savedUser, "localhost");
            log.info("Activation email has been send to: {}", user.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.setErrorMessage(mapErrorMessage(ex));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("id") String uuid, @RequestParam("usr") String userId) {
        try {
            userService.activateUserAccount(uuid, userId);
            log.info("Account was activated for userId: {}", userId);
            return ResponseEntity.ok("Aktywowano konto");
        } catch (Exception ex) {
            ErrorMessage errorMessage = mapErrorMessage(ex);
            return ResponseEntity.badRequest().body(errorMessage.getErrorMessage());
        }
    }
}
