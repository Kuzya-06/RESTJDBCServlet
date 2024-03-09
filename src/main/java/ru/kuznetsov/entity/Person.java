package ru.kuznetsov.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 Класс User для представления сущности пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Integer id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    // Другие поля и геттеры/сеттеры
}

