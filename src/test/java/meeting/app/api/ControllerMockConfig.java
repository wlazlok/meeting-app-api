package meeting.app.api;

import meeting.app.api.controllers.CommentController;
import meeting.app.api.services.CategoryService;
import meeting.app.api.services.CommentService;
import meeting.app.api.services.EventService;
import meeting.app.api.services.UserService;
import org.mockito.Mock;

public class ControllerMockConfig {

    @Mock
    protected CategoryService categoryService;

    @Mock
    protected EventService eventService;

    @Mock
    protected CommentService commentService;

    @Mock
    protected UserService userService;
}
