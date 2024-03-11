package ru.kuznetsov.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.service.ProjectService;
import ru.kuznetsov.service.impl.ProjectServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@ExtendWith(
        MockitoExtension.class
)
class ProjectServletTest {
    private static ProjectService mockProjectService;
    @InjectMocks
    private static ProjectServlet projectServlet;
    private static ProjectServiceImpl oldInstance;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(ProjectService mock) {
        try {
            Field instance = ProjectServiceImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (ProjectServiceImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockProjectService = Mockito.mock(ProjectService.class);
        setMock(mockProjectService);
        projectServlet = new ProjectServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = ProjectServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockProjectService);
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn("project/all").when(mockRequest).getPathInfo();

        projectServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockProjectService).findAll();
    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.doReturn("project/2").when(mockRequest).getPathInfo();

        projectServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockProjectService).findById(Mockito.anyInt());
    }

    // ------------------------
    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("project/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockProjectService).findById(100);

        projectServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn("department/2q").when(mockRequest).getPathInfo();

        projectServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException, NotFoundException {
        Mockito.doReturn("department/2").when(mockRequest).getPathInfo();

        projectServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockProjectService).delete(Mockito.anyInt());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.doReturn("department/a100").when(mockRequest).getPathInfo();

        projectServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String expectedName = "New project";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"name\":\"" + expectedName + "\"}",
                null
        ).when(mockBufferedReader).readLine();

        projectServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<ProjectIncomingDto> argumentCaptor = ArgumentCaptor.forClass(ProjectIncomingDto.class);
        Mockito.verify(mockProjectService).save(argumentCaptor.capture());

        ProjectIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String expectedName = "Update project";

        Mockito.doReturn("project/").when(mockRequest).getPathInfo();
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"id\": 4,\"name\": \"" +
                        expectedName + "\"}",
                null
        ).when(mockBufferedReader).readLine();

        projectServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<ProjectUpdateDto> argumentCaptor =
                ArgumentCaptor.forClass(ProjectUpdateDto.class);
        Mockito.verify(mockProjectService).update(argumentCaptor.capture());

        ProjectUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPutBadRequest() throws IOException {
        Mockito.doReturn("project/").when(mockRequest).getPathInfo();
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{Bad json:1}",
                null
        ).when(mockBufferedReader).readLine();

        projectServlet.doPut(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}