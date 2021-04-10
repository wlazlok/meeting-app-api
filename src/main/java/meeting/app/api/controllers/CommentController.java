package meeting.app.api.controllers;

import meeting.app.api.model.comment.CommentItemResponse;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.services.CommentService;
import meeting.app.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static meeting.app.api.utils.HandleErrorMessage.mapErrorMessage;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<CommentItemResponse> getAllComments() {
        CommentItemResponse response = new CommentItemResponse();

        try {
            response.setCommentItemList(commentService.getAllComments());
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            response.setErrorMessage(mapErrorMessage(ex));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<CommentItemResponse> getCommentsForUser() {
        CommentItemResponse response = new CommentItemResponse();

        try {
            UserEntity contextUser = userService.getUserFromContext();
            response.setCommentItemList(commentService.getCommentsForUser(contextUser));
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            response.setErrorMessage(mapErrorMessage(ex));
            return ResponseEntity.badRequest().body(response);
        }
    }
}
