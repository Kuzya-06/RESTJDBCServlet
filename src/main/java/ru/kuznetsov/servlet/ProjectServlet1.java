package ru.kuznetsov.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectOutGoingDto;
import ru.kuznetsov.dto.ProjectOutIdNameEmployeeDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.entity.Project1;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.service.ProjectService;
import ru.kuznetsov.service.ProjectService1;
import ru.kuznetsov.service.impl.ProjectServiceImpl;
import ru.kuznetsov.service.impl.ProjectServiceImpl1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Slf4j
@WebServlet(urlPatterns = {"/project1/*"})
public class ProjectServlet1 extends HttpServlet {
    private final transient ProjectService1 projectService1 = ProjectServiceImpl1.getInstance();
    private final ObjectMapper objectMapper;

    public ProjectServlet1() {
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
        log.info("Begin doGet");
        setJsonHeader(resp);

        String responseAnswer = "";
        try {
            log.info("pathInfo = " + req.getPathInfo());
            String[] pathPart = req.getPathInfo().split("/");

            if ("all".equals(pathPart[1])) {
                log.info("all");

                List<ProjectOutIdNameEmployeeDto> projectDtoList = projectService1.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(projectDtoList);
            } else {
                Integer projId = Integer.parseInt(pathPart[1]);
                log.info("Ввели id = " + (projId));
                ProjectOutIdNameEmployeeDto byId = projectService1.findById(projId);
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(byId);
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
