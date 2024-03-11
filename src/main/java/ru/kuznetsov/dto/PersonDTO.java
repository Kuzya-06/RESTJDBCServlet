package ru.kuznetsov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
Класс UserDTO для передачи данных о пользователе
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    // Другие поля и геттеры/сеттеры


}

