package banking;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        String filename = null;

        for (int i = 0; i < args.length; i = i + 2) {
            if (args[i].equals("-fileName")) {
                filename = args[i + 1];
            }
        }

        if (filename != null) {
            try (Connection connection = Database.connect(filename)) {
                Menu menu = new Menu(connection);
                menu.printMenu(-1);
            }
        } else {
            System.out.println("The argument fileName is required to start the application.");
        }
    }
}