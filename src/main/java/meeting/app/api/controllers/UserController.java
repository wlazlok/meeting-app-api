package meeting.app.api.controllers;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.model.user.*;
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
            emailService.sendActivateEmail(savedUser, "localhost");
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

    @PostMapping("/reset")
    public ResponseEntity<String> resetPasswordAndSendEmail(@RequestBody ResetPasswordRequest request) {

        try {
            UserEntity userEntity = userService.resetPassword(request);
            emailService.sendResetEmail(userEntity, "localhost");
            log.info("Password reset for userId: {}", userEntity.getId());
            return ResponseEntity.ok("Na podany adres email został wysłany link resetujący hasło");
        } catch (Exception ex) {
            log.info("User controller reset password {}", ex.getMessage());
            ErrorMessage errorMessage = mapErrorMessage(ex);
            return ResponseEntity.badRequest().body(errorMessage.getErrorMessage());
        }
    }

    @PostMapping("/change")
    public ResponseEntity<String> changePasswordFromLink(@RequestParam("id") String uuid,
                                                         @RequestParam("usr") String userId,
                                                         @RequestBody ChangePasswordRequest request) {

        if (!userService.validateUserPassword(request.getPassword(), request.getConfirmPassword())) {
            ErrorMessage errorMessage = mapErrorCode("msg.err.invalid.password");
            return ResponseEntity.badRequest().body(errorMessage.getErrorMessage());
        }

        try {
            userService.changePasswordFromLink(uuid, userId, request);
            log.info("Password changed from user {}", request.getUsername());
            return ResponseEntity.ok("Hasło zmienione pomyślnie");
        } catch (Exception ex) {
            log.info("User controller change password from link {}", ex.getMessage());
            ErrorMessage errorMessage = mapErrorMessage(ex);
            return ResponseEntity.badRequest().body(errorMessage.getErrorMessage());
        }
    }
}
