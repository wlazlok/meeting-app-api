package meeting.app.api.controllers;

import meeting.app.api.services.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageControllerTest {

    private final String PATH = "/cloud";

    private MockMvc mockMvc;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ImageController controller;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * upload() method
     * 1. upload OK
     * 2. upload throw Exception
     */

    @Test
    void upload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());

        when(cloudinaryService.upload(any())).thenReturn("hjpdnasd");

        MvcResult mvcResult = mockMvc.perform(multipart(PATH + "/upload")
                .file(file))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertEquals("hjpdnasd", response);
        verify(cloudinaryService, times(1)).upload(any());
    }

    @Test
    void uploadThrowException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());

        given(cloudinaryService.upload(any())).willAnswer(invocationOnMock -> {
            throw new Exception("error");
        });

        MvcResult mvcResult = mockMvc.perform(multipart(PATH + "/upload")
                .file(file))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertEquals("error", response);
        verify(cloudinaryService, times(1)).upload(any());
    }
}
