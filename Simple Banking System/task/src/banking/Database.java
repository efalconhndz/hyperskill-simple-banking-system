package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    public static Connection connect(String fileName) {
         Connection conn = null;

            try {
                // db parameters
                String url = "jdbc:sqlite:" + fileName;
                // create a connection to the database
                conn = DriverManager.getConnection(url);

                createCardTable(conn);

                // Disable auto-commit mode
                conn.setAutoCommit(false);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return conn;
    }

    public static void createCardTable(Connection connection) {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	number text NOT NULL,\n"
                + "	pin text NOT NULL,\n"
                + "	balance INTEGER DEFAULT 0\n"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
