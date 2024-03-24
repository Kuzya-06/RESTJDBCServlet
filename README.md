# RESTJDBCServlet
Проект по теме Servlet JDBC REST без SPRING

Задача:

1) Сделать REST сервис с использованием JDBC и Servlet
2) Функционал любой на выбор, минимум CRUD сервис с несколькими видами entity
3) Запрещено использовать Spring, Hibernate
4) Можно использовать Mapstruckt
5) Должны быть реализованы связи ManyToOne(OneToMany), ManyToMany
6) Связи должны быть отражены в коде
7) Сервлет должен возвращать DTO, не возвращаем Entity, принимать также DTO
8) Должны быть unit тесты, использовать Mockito и Junit
9) Для проверки работы репозитория(DAO) с БД использовать
    testcontainers: https://testcontainers.com/, https://habr.com/ru/articles/444982/
10) Покрытие тестами должно быть больше 80%
11) БД Postgres.
    
    docker run -p 5432:5432 --name postgres-db-Aston -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=123 -e POSTGRES_DB=Aston -e PGDATA=/var/lib/postgresql/data/pgdata -v d:\docker\aston:/var/lib/postgresql/data -d postgres:16.0

       1) Установить БД с помощью скрипта [createDB.sql](src/main/resources/sql/createDB.sql).
       2) Инициализировать БД с помощью скрипта [data.sql](src/main/resources/sql/data.sql).
12) Ставим плагин SonarLint.

### Employee:

GET http://localhost:8080/emp/all - получить всех работников

GET http://localhost:8080/emp/{id} - получить работника с {Id}

POST http://localhost:8080/emp - создать нового работника

    {
    "name": "Алексей Алексеев",
    "role": {
            "id": 4
            }     
    }

PUT http://localhost:8080/emp - изменить имя работника

    {
    "id": 10,
    "name": "Валентина Колобкова",
    "role": {
        "id": 4,
        "name": "Программист DB"
        } ,
    "projectList": []
    }

DELETE http://localhost:8080/emp/{id} - удалить работника с {Id}



### Role:

GET http://localhost:8080/role/all - получить все роли

GET http://localhost:8080/role/{roleId} - получить роль с {roleId}

POST http://localhost:8080/role - создать новую роль

    {
    "name": "New role name"
    }

PUT http://localhost:8080/role - изменить роль

    {
    "id": 7,
    "name": "Engineer"
    }

DELETE http://localhost:8080/role/{roleId} - удалить роль с {roleId}



### Project:

GET http://localhost:8080/project/all - получить все номера project

GET http://localhost:8080/project/{projectId} - получить project {projectId}

POST http://localhost:8080/project - сохранить в базу новый project
   
    {
    "name": "new Project"
    }

DELETE http://localhost:8080/project/{projectId} - удалить project {projectId}

PUT http://localhost:8080/project - изменить наименование project

    {
    "id": 6,
    "name": "Edit Project"
    }

DELETE http://localhost:8080/project/{projectId}/deleteEmployee/{employeeId} - удалить работника из проекта
* http://localhost:8080/project/4/deleteEmployee/12

PUT http://localhost:8080/project/{projectId}/addEmployee/{employeeId} - добавить работника в проект
* http://localhost:8080/project/5/addEmployee/13
