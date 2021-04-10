package meeting.app.api.services;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.exceptions.MeetingApiException;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.repositories.CommentItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentItemRepository commentItemRepository;

    public List<CommentItem> getAllComments() {
        List<CommentItem> comments = new ArrayList<>();

        try {
            Iterable<CommentItem> commentsFound = commentItemRepository.findAll();
            for (CommentItem commentItem : commentsFound) {
                if (commentItem.getUserEntity() != null) {
                    commentItem.setUserId(commentItem.getUserEntity().getId());
                }
                if (commentItem.getEventItem() != null) {
                    commentItem.setEventId(commentItem.getEventItem().getId());
                }
                comments.add(commentItem);
            }
            return comments;
        } catch (Exception ex) {
            log.info("comment.service.get.all.comments.exception " + ex.getMessage());
            throw new MeetingApiException("msg.err.get.all.comments.error");
        }
    }

    public List<CommentItem> getCommentsForUser(UserEntity userEntity) {
        List<CommentItem> comments = new ArrayList<>();

        try {
            Iterable<CommentItem> commentsFound = userEntity.getComments();
            for (CommentItem commentItem : commentsFound) {
                if (commentItem.getEventItem() != null) {
                    commentItem.setEventId(commentItem.getEventItem().getId());
                }
                commentItem.setUserId(userEntity.getId());
                comments.add(commentItem);
            }
            return comments;
        } catch (Exception ex) {
            log.info("comment.service.get.comments.for.user " + ex.getMessage());
            throw new MeetingApiException("msg.err.get.comments.for.user.error");
        }
    }
}
