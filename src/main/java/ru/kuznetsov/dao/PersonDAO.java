package ru.kuznetsov.dao;

import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dto.PersonDTO;
import ru.kuznetsov.entity.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 Класс UserDao для работы с базой данных через JDBC
 */
@Slf4j
public class PersonDAO {
    // Методы для работы с базой данных
    private static final String URL = "jdbc:postgresql://localhost:5432/Aston";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "123";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setSchema("task3");
            log.info(connection.toString());
            log.info(connection.getCatalog());
            log.info(connection.getSchema());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public List<Person> getAllPerson() {

        List<Person> people = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Person person = new Person();

                person.setId(resultSet.getInt("person_id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setDateOfBirth(resultSet.getString("date_of_birth"));
                // System.out.println("getAllPerson() "+ people);
                people.add(person);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        log.info("getAllPerson() = " + people);
        return people;
    }

    public Person getById(int id) {
        String personById = "SELECT * FROM Person WHERE person_id=?";
        Person person = new Person();
        log.info("Method getById. ID = " + id);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(personById);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                person.setId(resultSet.getInt("person_id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setDateOfBirth(resultSet.getString("date_of_birth"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        log.info(person.toString());
        return person;
    }

    public void save(Person person) throws SQLException {
        String savePerson = "INSERT INTO Person (first_name, last_name, date_of_birth) VALUES ( ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(savePerson);
        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getLastName());
        preparedStatement.setString(3, person.getDateOfBirth());
        int i = preparedStatement.executeUpdate();

        log.info("Кол-во записей person в БД = " + i);
      //  return person;

    }


}
