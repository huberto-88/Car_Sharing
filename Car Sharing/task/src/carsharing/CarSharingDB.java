package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CarSharingDB {
    private String DB_URL = "jdbc:h2:./src/carsharing/db/";
    private String DB_NAME = "carsharingDB";
    private Connection connection;

    public void createDB(String[] args) {
        if (args.length > 0 && args[0] != null) {
            DB_NAME = args[1];
        }

        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(DB_URL + DB_NAME);
            this.connection.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try (Statement statement = getConnection().createStatement()) {
            String createCompanyTable = "" +
                    "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50) UNIQUE NOT NULL);";
            statement.executeUpdate(createCompanyTable);

            String createCarTable = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "id INT AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL UNIQUE," +
                    "company_id INT NOT NULL," +
                    "PRIMARY KEY (id)," +
                    "FOREIGN KEY (company_id) REFERENCES COMPANY(id)" +
                    ");";


            //                    "" +
//                    "CREATE TABLE IF NOT EXISTS CAR " +
//                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
//                    "name VARCHAR(50) NOT NULL UNIQUE, " +
//                    "COMPANY_id INTEGER NOT NULL, " +
//                    "FOREIGN KEY (COMPANY_id) REFERENCES COMPANY(id)" +
//                    ");";
            statement.executeUpdate(createCarTable);

            String createCustomerTable = "" +
                    "CREATE TABLE IF NOT EXISTS customer " +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50) NOT NULL UNIQUE, " +
                    "rented_car_id INTEGER, " +
                    "FOREIGN KEY (rented_car_id) REFERENCES car(id)" +
                    ");";
            statement.executeUpdate(createCustomerTable);

            statement.executeUpdate("ALTER TABLE COMPANY ALTER COLUMN id RESTART WITH 1");
            statement.executeUpdate("ALTER TABLE CAR ALTER COLUMN id RESTART WITH 1");
            statement.executeUpdate("ALTER TABLE customer ALTER COLUMN id RESTART WITH 1");

        } catch (SQLException e) {
            System.out.println("Creating statement failed");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}