package ru.kuznetsov.dao.sql;

public class EmployeeSQLQuery {

    private EmployeeSQLQuery() {
    }

    public static final String SAVE_SQL = """
            INSERT INTO employee (name, role_id)
            VALUES (?, ?) ;
            """;
    public static final String UPDATE_SQL = """
            UPDATE employee
            SET name = ?,
                role_id =?
            WHERE id = ?  ;
            """;
    public static final String DELETE_SQL = """
            DELETE FROM employee
            WHERE id = ? ;
            """;
    public static final String FIND_BY_ID_SQL = """
            SELECT id, name, role_id FROM employee
            WHERE id = ?
            LIMIT 1;
            """;
    public static final String FIND_ALL_SQL = """
            SELECT id, name, role_id FROM employee;
            """;
    public static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM employee
                        WHERE id = ?
                        LIMIT 1);
            """;


}
