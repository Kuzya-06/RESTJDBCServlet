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
import ru.kuznetsov.dto.EmployeeIncomingDto;
import ru.kuznetsov.dto.EmployeeUpdateDto;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.service.EmployeeService;
import ru.kuznetsov.service.impl.EmployeeServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@ExtendWith(
        MockitoExtension.class
)
class EmployeeServletTest {
    private static EmployeeService mockEmployeeService;
    @InjectMocks
    private static EmployeeServlet employeeServlet;
    private static EmployeeServiceImpl oldInstance;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(EmployeeService mock) {
        try {
            Field instance = EmployeeServiceImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (EmployeeServiceImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockEmployeeService = Mockito.mock(EmployeeService.class);
        setMock(mockEmployeeService);
        employeeServlet = new EmployeeServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = EmployeeServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockEmployeeService);
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn("emp/all").when(mockRequest).getPathInfo();

        employeeServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockEmployeeService).findAll();
    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.doReturn("emp/2").when(mockRequest).getPathInfo();

        employeeServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockEmployeeService).findById(Mockito.anyInt());
    }

    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("emp/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockEmployeeService).findById(100);

        employeeServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn("emp/2q").when(mockRequest).getPathInfo();

        employeeServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException, NotFoundException {
        Mockito.doReturn("emp/2").when(mockRequest).getPathInfo();

        employeeServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockEmployeeService).delete(Mockito.anyInt());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteNotFound() throws IOException, NotFoundException {
        Mockito.doReturn("emp/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockEmployeeService).delete(100);

        employeeServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(mockEmployeeService).delete(100);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.doReturn("user/a100").when(mockRequest).getPathInfo();

        employeeServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String expectedName = "New Name";


        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"name\":\"" + expectedName + "\"" +
                        ", \"role\":{\"id\":4,\"name\":\"Программист DB\"} " +
                        "}",
                null
        ).when(mockBufferedReader).readLine();

        employeeServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<EmployeeIncomingDto> argumentCaptor = ArgumentCaptor.forClass(EmployeeIncomingDto.class);
        Mockito.verify(mockEmployeeService).save(argumentCaptor.capture());

        EmployeeIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String expectedName = "New Name";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\": 1," +
                        "\"name\":\"" + expectedName + "\"" +

                        ", \"role\":{\"id\":4}, " +

                        "\"projectList\": [{\"id\": 2}]" +
                        "}",
                null
        ).when(mockBufferedReader).readLine();

        employeeServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<EmployeeUpdateDto> argumentCaptor = ArgumentCaptor.forClass(EmployeeUpdateDto.class);
        Mockito.verify(mockEmployeeService).update(argumentCaptor.capture());

        EmployeeUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPutBadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{Bad json:1}",
                null
        ).when(mockBufferedReader).readLine();

        employeeServlet.doPut(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


}