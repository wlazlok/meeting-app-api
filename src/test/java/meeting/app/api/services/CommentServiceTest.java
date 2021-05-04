package meeting.app.api.services;

import meeting.app.api.ServiceMockConfig;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.comment.CommentItemResponse;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static meeting.app.api.mocks.MockModel.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest extends ServiceMockConfig {

    @InjectMocks
    private CommentService underTest;

    /**
     * getAllComments() method
     * 1. getAllComments OK
     * 2. getAllComments throw Exception
     * 3. getAllComments UserEntity is Null
     * getAllComments EventItem is null
     */

    @Test
    void getAllComments() {
        CommentItem commentItem = generateCommentItem();
        Iterable<CommentItem> comments = Arrays.asList(commentItem);

        when(commentItemRepository.findAll()).thenReturn(comments);

        List<CommentItem> result = underTest.getAllComments();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(commentItemRepository, times(1)).findAll();
    }

    @Test
    void getAllCommentsThrowException() {
        given(commentItemRepository.findAll()).willAnswer(invocationOnMock -> {
            throw new Exception("exception");
        });

        try {
            underTest.getAllComments();
            fail("Should throw exception");
        } catch (Exception ex) {
            verify(commentItemRepository, times(1)).findAll();
        }
    }

    @Test
    void getAllCommentsUserIsNull() {
        CommentItem commentItem = generateCommentItem();
        commentItem.setUserEntity(null);
        Iterable<CommentItem> comments = Arrays.asList(commentItem);

        when(commentItemRepository.findAll()).thenReturn(comments);

        List<CommentItem> result = underTest.getAllComments();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertNull(result.get(0).getUserId());
        verify(commentItemRepository, times(1)).findAll();
    }

    @Test
    void getAllCommentsEventIsNull() {
        CommentItem commentItem = generateCommentItem();
        commentItem.setEventItem(null);
        Iterable<CommentItem> comments = Arrays.asList(commentItem);

        when(commentItemRepository.findAll()).thenReturn(comments);

        List<CommentItem> result = underTest.getAllComments();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertNull(result.get(0).getEventId());
        verify(commentItemRepository, times(1)).findAll();
    }

    /**
     * getCommentsForUser() method
     * 1. getCommentsForUser OK
     * 2. getCommentsForUser throw Exception
     * 3.getCommentsForUser EventItem is null
     */

    @Test
    void getCommentsForUser() {
        CommentItem commentItem = generateCommentItem();
        UserEntity userEntity = generateUserEntity();
        userEntity.setComments(Arrays.asList(commentItem));

        List<CommentItem> result = underTest.getCommentsForUser(userEntity);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getCommentsForUserThrowException() {
        UserEntity userEntity = generateUserEntity();
        userEntity.setComments(null);

        try {
            underTest.getCommentsForUser(userEntity);
            fail("Should throw exception");
        } catch (Exception ex) {

        }
    }

    @Test
    void getCommentsForUserEventIsNull() {
        UserEntity userEntity = generateUserEntity();
        CommentItem commentItem = generateCommentItem();
        commentItem.setEventItem(null);
        userEntity.setComments(Arrays.asList(commentItem));

        List<CommentItem> result = underTest.getCommentsForUser(userEntity);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertNull(result.get(0).getEventId());
    }

    /**
     * getCommentsForEvent()
     */

    @Test
    void getCommentsForEventPass() {
        Long eventId = 5L;
        EventItem eventItem = generateEventItem();

        when(eventItemRepository.getById(anyLong())).thenReturn(eventItem);

        CommentItemResponse result = underTest.getCommentsForEvent(eventId);

        assertNotNull(result);
        assertNotNull(result.getCommentItemList());
        verify(eventItemRepository, times(1)).getById(anyLong());
    }

    @Test
    void getCommentsForEventAndEventNotFound() {
        Long eventId = 5L;

        when(eventItemRepository.getById(anyLong())).thenReturn(null);

        try {
            CommentItemResponse result = underTest.getCommentsForEvent(eventId);
            fail("Test should throw exception");
        } catch (Exception ex) {

        }
    }

    /**
     * addCommentToEvent()
     */

    @Test
    void addCommentToEventPass() {
        String content = "test";
        Long eventId = 5L;
        UserEntity userEntity = generateUserEntity();
        EventItem eventItem = generateEventItem();
        CommentItem commentItem = generateCommentItem();

        when(eventItemRepository.getById(anyLong())).thenReturn(eventItem);
        when(commentItemRepository.save(any(CommentItem.class))).thenReturn(commentItem);

        CommentItem result = underTest.addCommentToEvent(content, eventId, userEntity);

        assertNotNull(result);
        verify(eventItemRepository, times(1)).getById(anyLong());
        verify(commentItemRepository, times(1)).save(any(CommentItem.class));
    }

    @Test
    void addCommentToEventContentIsNull() {
        String content = null;
        Long eventId = 5L;
        UserEntity userEntity = generateUserEntity();

        try {
            CommentItem result = underTest.addCommentToEvent(content, eventId, userEntity);
            fail("Test should throw exception");
        } catch (Exception ex) {
            verify(eventItemRepository, times(0)).getById(anyLong());
            verify(commentItemRepository, times(0)).save(any(CommentItem.class));
        }
    }

    @Test
    void addCommentToEventContentIsBlank() {
        String content = "";
        Long eventId = 5L;
        UserEntity userEntity = generateUserEntity();

        try {
            CommentItem result = underTest.addCommentToEvent(content, eventId, userEntity);
            fail("Test should throw exception");
        } catch (Exception ex) {
            verify(eventItemRepository, times(0)).getById(anyLong());
            verify(commentItemRepository, times(0)).save(any(CommentItem.class));
        }
    }

    @Test
    void addCommentToEventAndEventItemIsNull() {
        String content = "test";
        Long eventId = 5L;
        UserEntity userEntity = generateUserEntity();

        when(eventItemRepository.getById(anyLong())).thenReturn(null);

        try {
            CommentItem result = underTest.addCommentToEvent(content, eventId, userEntity);
            fail("Test should throw exception");
        } catch (Exception ex) {
            ;
            verify(eventItemRepository, times(1)).getById(anyLong());
            verify(commentItemRepository, times(0)).save(any(CommentItem.class));
        }
    }

    @Test
    void addCommentToEventSaveThrowException() {
        String content = "test";
        Long eventId = 5L;
        UserEntity userEntity = generateUserEntity();
        EventItem eventItem = generateEventItem();

        when(eventItemRepository.getById(anyLong())).thenReturn(eventItem);
        given(commentItemRepository.save(any(CommentItem.class))).willAnswer(invocationOnMock -> {
            throw new Exception();
        });
        try {
            CommentItem result = underTest.addCommentToEvent(content, eventId, userEntity);
            fail("Test should throw exception");
        } catch (Exception ex) {
            verify(eventItemRepository, times(1)).getById(anyLong());
            verify(commentItemRepository, times(1)).save(any(CommentItem.class));
        }
    }
}
