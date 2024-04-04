package ru.kuznetsov.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dto.EmployeeIncomingDto;
import ru.kuznetsov.dto.EmployeeOutGoingDto;
import ru.kuznetsov.dto.EmployeeUpdateDto;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.service.EmployeeService;
import ru.kuznetsov.service.impl.EmployeeServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Slf4j
@WebServlet(urlPatterns = {"/emp1/*"})
public class EmployeeServlet1 extends HttpServlet {
    private final transient EmployeeService employeeService = EmployeeServiceImpl.getInstance();
    private final ObjectMapper objectMapper;

    public EmployeeServlet1() {
        this.objectMapper = new ObjectMapper();
    }

    private static void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    private static String getJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader postData = req.getReader();
        String line;
        while ((line = postData.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info(getClass().getName() + " Begin doGet");
        setJsonHeader(resp);

        String responseAnswer = "";
        try {
            log.info("in try "+req.getPathInfo());
            String[] pathPart = req.getPathInfo().split("/");
            if ("all".equals(pathPart[1])) {
                log.info("all");
                List<EmployeeOutGoingDto> empDtoList = employeeService.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(empDtoList);
            } else {
                Integer userId = Integer.parseInt(pathPart[1]);
                log.info(String.valueOf(userId));
                EmployeeOutGoingDto empDto = employeeService.findById(userId);
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(empDto);
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = e.getMessage();
            log.warn(responseAnswer);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Bad request.";
            log.warn(responseAnswer);
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }





}
