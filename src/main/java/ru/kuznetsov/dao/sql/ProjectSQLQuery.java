package ru.kuznetsov.dao.sql;

public class ProjectSQLQuery {

    private ProjectSQLQuery() {
    }

    public static final String SAVE_SQL = """
            INSERT INTO project (name)
            VALUES (?) ;
            """;
    public static final String UPDATE_SQL = """
            UPDATE project
            SET name = ?
            WHERE id = ?  ;
            """;
    public static final String DELETE_SQL = """
            DELETE FROM project
            WHERE id = ? ;
            """;
    public static final String FIND_BY_ID_SQL = """
            SELECT id, name FROM project
            WHERE id = ?
            LIMIT 1;
            """;
    public static final String FIND_ALL_SQL = """
            SELECT id, name FROM project;
            """;
    public static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM project
                        WHERE id = ?
                        LIMIT 1);
            """;
}
