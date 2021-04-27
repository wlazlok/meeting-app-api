package meeting.app.api.services;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.exceptions.MeetingApiException;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.comment.CommentItemResponse;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.repositories.CommentItemRepository;
import meeting.app.api.repositories.EventItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentItemRepository commentItemRepository;

    @Autowired
    private EventItemRepository eventItemRepository;

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

    @Transactional
    public CommentItem addCommentToEvent(String content, Long eventId, UserEntity userEntity) {
        if (content == null || content.isBlank()) {
            throw new MeetingApiException("msg.err.comment.item.content.is.blank");
        }
        CommentItem commentItem = new CommentItem();
        try {
            EventItem eventItem = eventItemRepository.getById(eventId);
            if (eventItem == null) {
                throw new MeetingApiException("msg.err.event.not.found");
            }
            commentItem.setContent(content);
            commentItem.setEventItem(eventItem);
            commentItem.setUserEntity(userEntity);
            commentItem.setDate(new Date());

            return commentItemRepository.save(commentItem);
        } catch (Exception ex) {
            log.info("comment.service.add.comment.exception " + ex.getMessage());
            throw new MeetingApiException("msg.err.add.comment.to.event.error");
        }
    }

    public CommentItemResponse getCommentsForEvent(Long eventId) {
        try {
            EventItem eventItem = eventItemRepository.getById(eventId);
            if (eventItem == null) {
                throw new MeetingApiException("msg.err.event.not.found");
            }
            return CommentItemResponse.builder()
                    .commentItemList(eventItem.getComments())
                    .errorMessage(null)
                    .build();
        } catch (Exception ex) {
            log.info("event.service.get.comments.for.event.exception {}", ex.getStackTrace());
            throw new MeetingApiException("msg.err.get.comments.for.event");
        }
    }
}
