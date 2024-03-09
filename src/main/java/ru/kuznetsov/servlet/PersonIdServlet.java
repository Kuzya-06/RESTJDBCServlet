package ru.kuznetsov.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dao.PersonDAO;
import ru.kuznetsov.entity.Person;

import java.io.IOException;
@Slf4j
@WebServlet(name = "PersonIdServlet", urlPatterns = {"/id"})
public class PersonIdServlet extends HttpServlet {

    private PersonDAO personDao;
    @Override
    public void init() throws ServletException {
        personDao = new PersonDAO();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id= Integer.parseInt(req.getParameter("id"));
        log.info("doGet id = "+id);

        Person byId = personDao.getById(id);

        // Отправка списка DTO в формате JSON
        resp.setContentType("application/json");
        resp.getWriter().write(new ObjectMapper().writeValueAsString(byId));
    }
}
