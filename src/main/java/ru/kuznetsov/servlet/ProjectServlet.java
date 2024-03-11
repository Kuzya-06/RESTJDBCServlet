package ru.kuznetsov.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectOutGoingDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.service.ProjectService;
import ru.kuznetsov.service.impl.ProjectServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Slf4j
@WebServlet(urlPatterns = {"/project/*"})
public class ProjectServlet extends HttpServlet {
    private final transient ProjectService projectService = ProjectServiceImpl.getInstance();
    private final ObjectMapper objectMapper;

    public ProjectServlet() {
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
                List<ProjectOutGoingDto> projectDtoList = projectService.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(projectDtoList);
            } else {
                Integer projId = Integer.parseInt(pathPart[1]);
                log.info("Ввели id = " + (projId));
                ProjectOutGoingDto projectDto = projectService.findById(projId);
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(projectDto);
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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            Integer projectId = Integer.parseInt(pathPart[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            if (req.getPathInfo().contains("/deleteEmployee/")) {
                if ("deleteEmployee".equals(pathPart[2])) {
                    Integer empId = Integer.parseInt(pathPart[3]);
                    projectService.deleteEmployeeFromProject(projectId, empId);
                }
            } else {
                projectService.delete(projectId);
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Bad request. ";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = null;
        Optional<ProjectIncomingDto> projResponse;
        try {
            projResponse = Optional.ofNullable(objectMapper.readValue(json, ProjectIncomingDto.class));
            ProjectIncomingDto project = projResponse.orElseThrow(IllegalArgumentException::new);
            responseAnswer = objectMapper.writeValueAsString(projectService.save(project));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Incorrect project Object.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = "";
        Optional<ProjectUpdateDto> projectResponse;
        try {
            if (req.getPathInfo().contains("/addEmployee/")) {
                String[] pathPart = req.getPathInfo().split("/");
                if (pathPart.length > 3 && "addEmployee".equals(pathPart[2])) {
                    Integer projId = Integer.parseInt(pathPart[1]);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    Integer empId = Integer.parseInt(pathPart[3]);
                    projectService.addEmployeeToProject(projId, empId);
                }
            } else {
                projectResponse = Optional.ofNullable(objectMapper.readValue(json, ProjectUpdateDto.class));
                ProjectUpdateDto projectUpdateDto = projectResponse.orElseThrow(IllegalArgumentException::new);
                projectService.update(projectUpdateDto);
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Incorrect project Object.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }
}
