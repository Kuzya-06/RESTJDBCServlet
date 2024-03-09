package ru.kuznetsov.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.kuznetsov.dao.PersonDAO;
import ru.kuznetsov.dto.PersonDTO;
import ru.kuznetsov.entity.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "PersonServlet", value = "/person")
public class PersonServlet extends HttpServlet {
    private PersonDAO personDao;
  //  private final ObjectMapper objectMapper;

//    public PersonServlet(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }


//    private static void setJsonHeader(HttpServletResponse resp) {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//    }
//
//    private static String getJson(HttpServletRequest req) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        BufferedReader postData = req.getReader();
//        String line;
//        while ((line = postData.readLine()) != null) {
//            sb.append(line);
//        }
//        return sb.toString();
//    }


    @Override
    public void init() throws ServletException {
        personDao = new PersonDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Person> people = personDao.getAllPerson();
        // Преобразование списка пользователей в список DTO
        List<PersonDTO> userDTOs = convertToDTO(people);
        // Отправка списка DTO в формате JSON
        resp.setContentType("application/json");
        resp.getWriter().write(new ObjectMapper().writeValueAsString(userDTOs));
    }

    // Другие методы сервлета

    private List<PersonDTO> convertToDTO(List<Person> users) {
        return users.stream()
                .map(user -> {
                    PersonDTO dto = new PersonDTO();
                    dto.setId(user.getId());
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    dto.setDateOfBirth(user.getDateOfBirth());
                    // Преобразование других полей
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

//        setJsonHeader(resp);
//        String json = getJson(req);
//
//        String responseAnswer = null;
//        Optional<PersonDTO> personResponse;
//        try {
//            personResponse = Optional.ofNullable(objectMapper.readValue(json, PersonDTO.class));
//            PersonDTO personDTO = personResponse.orElseThrow(IllegalArgumentException::new);
//            responseAnswer = objectMapper.writeValueAsString(personDao.save(personDTO));
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            responseAnswer = "Incorrect phoneNumber Object.";
//        }
//        PrintWriter printWriter = resp.getWriter();
//        printWriter.write(responseAnswer);
//        printWriter.flush();


        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String birthYear = req.getParameter("dateOfBirth");
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setDateOfBirth(birthYear);
        try {
            personDao.save(person);
            resp.setStatus(HttpServletResponse.SC_CREATED);
           // resp.setContentType("application/json");
        } catch (SQLException e){
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

}