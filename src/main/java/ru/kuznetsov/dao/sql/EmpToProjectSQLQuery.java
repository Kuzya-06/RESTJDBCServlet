package ru.kuznetsov.dao.sql;

public class EmpToProjectSQLQuery {
    private EmpToProjectSQLQuery() {
    }

    public static final String SAVE_SQL = """
            INSERT INTO employeeproject (employee_id, project_id)
            VALUES (?, ?);
            """;
    public static final String UPDATE_SQL = """
            UPDATE employeeproject
            SET employee_id = ?,
                department_id = ?
            WHERE id = ?;
            """;
    public static final String DELETE_SQL = """
            DELETE FROM employeeproject
            WHERE id = ? ;
            """;
    public static final String FIND_BY_ID_SQL = """
            SELECT id, employee_id, project_id FROM employeeproject
            WHERE id = ?
            LIMIT 1;
            """;
    public static final String FIND_ALL_SQL = """
            SELECT id, employee_id, project_id FROM employeeproject;
            """;
    public static final String FIND_ALL_BY_USERID_SQL = """
            SELECT id, employee_id, project_id FROM employeeproject
            WHERE employee_id = ?;
            """;
    public static final String FIND_ALL_BY_DEPARTMENT_ID_SQL = """
            SELECT id, employee_id, project_id FROM employeeproject
            WHERE project_id = ?;
            """;
    public static final String FIND_BY_USERID_AND_DEPARTMENT_ID_SQL = """
            SELECT id, employee_id, project_id FROM employeeproject
            WHERE employee_id = ? AND project_id = ?
            LIMIT 1;
            """;
    public static final String DELETE_BY_USERID_SQL = """
            DELETE FROM employeeproject
            WHERE employee_id = ?;
            """;
    public static final String DELETE_BY_DEPARTMENT_ID_SQL = """
            DELETE FROM employeeproject
            WHERE project_id = ?;
            """;
    public static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM employeeproject
                        WHERE id = ?
                        LIMIT 1);
            """;
}
