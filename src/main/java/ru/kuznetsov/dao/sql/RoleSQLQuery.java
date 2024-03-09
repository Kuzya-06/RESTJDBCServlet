package ru.kuznetsov.dao.sql;

public class RoleSQLQuery {
    private RoleSQLQuery() {
    }

    public static final String SAVE_SQL = """
            INSERT INTO role (name)
            VALUES (?) ;
            """;
    public static final String UPDATE_SQL = """
            UPDATE role
            SET name = ?
            WHERE id = ?  ;
            """;
    public static final String DELETE_SQL = """
            DELETE FROM role
            WHERE id = ? ;
            """;
    public static final String FIND_BY_ID_SQL = """
            SELECT id, name FROM role
            WHERE id = ?
            LIMIT 1;
            """;
    public static final String FIND_ALL_SQL = """
            SELECT id, name FROM role ;
            """;
    public static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM role
                        WHERE id = ?
                        LIMIT 1);
            """;


}
