package meeting.app.api.controllers;

import meeting.app.api.model.comment.CommentItemRequest;
import meeting.app.api.model.comment.CommentItemResponse;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.services.CommentService;
import meeting.app.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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

    @GetMapping("/event/{eventId}")
    public ResponseEntity<CommentItemResponse> getCommentsForEvent(@PathVariable("eventId") String eventId) {
        try {
            return ResponseEntity.ok().body(commentService.getCommentsForEvent(Long.valueOf(eventId)));
        } catch (Exception ex) {
            CommentItemResponse response = CommentItemResponse.builder()
                    .commentItemList(null)
                    .errorMessage(mapErrorMessage(ex))
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<CommentItemResponse> addCommentToEvent(@RequestBody CommentItemRequest request) {
        CommentItemResponse response = new CommentItemResponse();
        try {
            UserEntity userEntity = userService.getUserFromContext();
            response.setCommentItemList(Arrays.asList(commentService.addCommentToEvent(request.getContent(), request.getEventId(), userEntity)));
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            response.setErrorMessage(mapErrorMessage(ex));
            return ResponseEntity.badRequest().body(response);
        }
    }
}
