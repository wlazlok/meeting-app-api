package meeting.app.api;

import meeting.app.api.services.CategoryService;
import meeting.app.api.services.EventService;
import org.mockito.Mock;

public class ControllerMockConfig {

    @Mock
    protected CategoryService categoryService;

    @Mock
    protected EventService eventService;
}
