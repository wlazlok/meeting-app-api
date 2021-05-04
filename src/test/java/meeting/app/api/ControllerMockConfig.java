package meeting.app.api;

import meeting.app.api.services.*;
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

    @Mock
    protected EmailService emailService;
}
