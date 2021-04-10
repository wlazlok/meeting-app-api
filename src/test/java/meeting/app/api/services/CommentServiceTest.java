package meeting.app.api.services;

import meeting.app.api.ServiceMockConfig;
import meeting.app.api.model.comment.CommentItem;
import meeting.app.api.model.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static meeting.app.api.mocks.MockModel.generateCommentItem;
import static meeting.app.api.mocks.MockModel.generateUserEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

;

public class CommentServiceTest extends ServiceMockConfig {

    @InjectMocks
    private CommentService underTest;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

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

}
