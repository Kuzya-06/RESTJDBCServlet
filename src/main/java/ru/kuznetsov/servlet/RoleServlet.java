package ru.kuznetsov.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dto.RoleIncomingDto;
import ru.kuznetsov.dto.RoleOutGoingDto;
import ru.kuznetsov.dto.RoleUpdateDto;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.service.RoleService;
import ru.kuznetsov.service.impl.RoleServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@WebServlet(urlPatterns = {"/role/*"})
public class RoleServlet extends HttpServlet {
    private final transient RoleService roleService;
    private final ObjectMapper objectMapper;

    public RoleServlet() {
        this.roleService = RoleServiceImpl.getInstance();
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
        log.info(getClass().getName()+" Begin doGet");
        setJsonHeader(resp);

        String responseAnswer = "";
        try {
            log.info(req.getPathInfo());
            String[] pathPart = req.getPathInfo().split("/");
            log.info("pathPart [1]" + pathPart[1]);
            if ("all".equals(pathPart[1])) {
                log.info("all");
                List<RoleOutGoingDto> roleDtoList = roleService.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(roleDtoList);
            } else {
                Integer roleId = Integer.parseInt(pathPart[1]);
                log.info(String.valueOf(roleId));
                RoleOutGoingDto roleDto = roleService.findById(roleId);
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(roleDto);
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
        log.info(getClass().getName()+" doDelete");
        setJsonHeader(resp);
        String responseAnswer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            Integer roleId = Integer.parseInt(pathPart[1]);
            if (roleService.delete(roleId)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Bad request.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info(getClass().getName()+" Begin doPost");
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = null;
        Optional<RoleIncomingDto> roleResponse;
        try {
            roleResponse = Optional.ofNullable(objectMapper.readValue(json, RoleIncomingDto.class));
            RoleIncomingDto role = roleResponse.orElseThrow(IllegalArgumentException::new);
            responseAnswer = objectMapper.writeValueAsString(roleService.save(role));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Incorrect role Object.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info(getClass().getName()+" Begin doPut");
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = "";
        Optional<RoleUpdateDto> roleResponse;
        try {
            roleResponse = Optional.ofNullable(objectMapper.readValue(json, RoleUpdateDto.class));
            RoleUpdateDto roleUpdateDto = roleResponse.orElseThrow(IllegalArgumentException::new);
            roleService.update(roleUpdateDto);
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Incorrect role Object.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }
}
