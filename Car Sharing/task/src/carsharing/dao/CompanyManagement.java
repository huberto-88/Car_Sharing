package carsharing.dao;

import carsharing.Car;
import carsharing.CarSharingDB;
import carsharing.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyManagement implements ManagementInterface {
    private CarSharingDB carSharingDB;

    public CompanyManagement(CarSharingDB carSharingDB) {
        this.carSharingDB = carSharingDB;
    }

    @Override
    public List<Company> getCompaniesList() {
        List<Company> allCompanies = new ArrayList<>();
        try {
            Statement statement = carSharingDB
                    .getConnection()
                    .createStatement();
            String query = "" +
                    "SELECT * FROM COMPANY;";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int resultId = resultSet.getInt("id");
                String resultName = resultSet.getString("name");
                allCompanies.add(new Company(resultId, resultName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCompanies;
    }

    @Override
    public Company getCompanyById(int id) {
        try {
            Statement statement = carSharingDB.getConnection().createStatement();
            String query = "" +
                    "SELECT * FROM COMPANY WHERE id=" + id;
            ResultSet resultSet = statement.executeQuery(query);
            int resultId = resultSet.getInt("id");
            String resultName = resultSet.getString("name");
            return new Company(resultId, resultName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createCompany(String name) {
        try {
            Statement statement = carSharingDB
                    .getConnection()
                    .createStatement();
            String query = "" +
                    "INSERT INTO COMPANY (NAME) VALUES('" + name + "');";
            statement.executeUpdate(query);
            System.out.println("The company was created!\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCompany(String name) {
        try {
            Statement statement = carSharingDB
                    .getConnection()
                    .createStatement();
            String query = "" +
                    "DELETE FROM COMPANY WHERE name='" + name + "';";
            statement.executeUpdate(query);
            System.out.println("Company " + name + " has been deleted!\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropDatabase() {
        try {
            Statement statement = carSharingDB
                    .getConnection()
                    .createStatement();
            String query = "" +
                    "DELETE FROM COMPANY;";
            statement.executeUpdate(query);
            System.out.println("Database has been deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Car> getCarList(int companyId) {
        List<Car> carList = new ArrayList<>();

        try {
            Statement statement = carSharingDB
                    .getConnection()
                    .createStatement();

//            String query = "" +
//                    "SELECT * FROM CAR " +
//                    "INNER JOIN COMPANY " +
//                    "ON CAR.company_id = COMPANY.id " +
//                    "WHERE COMPANY.name='" + companyName + "';";

            String query ="SELECT CAR.id as id, CAR.name as name, CAR.company_id as company_id FROM CAR " +
                    "    LEFT JOIN CUSTOMER ON CAR.id = CUSTOMER.rented_car_id " +
                    "WHERE company_id = '" + companyId + "' AND CUSTOMER.rented_car_id is null;";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int company_id = resultSet.getInt("company_id");
                carList.add(new Car(id, name, company_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carList;
    }

    @Override
    public void createCar(String carName, int companyId) {
        try {
            Statement statement = carSharingDB
                    .getConnection()
                    .createStatement();
            String query = "" +
                    "INSERT INTO CAR (name, COMPANY_id) " +
                    "VALUES ('" + carName + "', " + companyId + ");";
            statement.executeUpdate(query);
            System.out.println("The car was created!\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCompanyId(String companyName) {
        int id = 0;
        try {
            Statement statement = carSharingDB.getConnection().createStatement();
            String query = "" +
                    "SELECT * FROM COMPANY " +
                    "WHERE name='" + companyName + "';";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void createCustomer(String name) {
        try {
            Statement statement = carSharingDB.getConnection().createStatement();
            String query = "" +
                    "INSERT INTO customer(name) " +
                    "VALUES ('" + name + "');";
            statement.executeUpdate(query);
            System.out.println("Customer " + name + " was added!");

        } catch (SQLException e) {
            System.out.println("Customer not added!");
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getCustomerList() {
        List<Customer> customerList = new ArrayList<>();

        try {
            Statement statement = carSharingDB
                    .getConnection()
                    .createStatement();
            String query = "" +
                    "SELECT * FROM customer;";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Optional<Integer> car_id = Optional.of(resultSet.getInt("rented_car_id"));
                int carId = car_id.orElse(0);

                customerList.add(new Customer(id, name, carId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    @Override
    public String getRentedCar(Customer customer) {
        String answer = "You didn't rent a car!\n";
        if (customer.getRentedCarId() < 1) {
            return answer;
        }

        try {
            Statement statement = carSharingDB.getConnection().createStatement();
            String query = "" +
                    "SELECT c.NAME CAR_NAME, co.ID CO_ID, co.NAME CO_NAME FROM CAR c " +
                    "JOIN COMPANY co " +
                    "ON c.COMPANY_ID = co.ID " +
                    "WHERE c.ID = " + customer.getRentedCarId() + ";";

            ResultSet resultSet = statement.executeQuery(query);

            String companyName = null;
            String carName = null;

            while (resultSet.next()) {
                carName = resultSet.getString("CAR_NAME");
                companyName = resultSet.getString("CO_NAME");
            }
            answer = "" +
                    "Your rented car:\n" +
                    carName + " \n" +
                    "Company:\n" +
                    companyName + "\n";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @Override
    public void rentACar(Customer customer, int carIndex, String carName) {
        int customerId = customer.getId();
        try {
            Statement statement = carSharingDB.getConnection().createStatement();
            String query = "" +
                    "UPDATE customer " +
                    "SET rented_car_id = " + carIndex + " " +
                    "WHERE id=" + customerId + ";";
            statement.executeUpdate(query);
            System.out.println("You rented '" + carName + "'");
            customer.setRentedCarId(carIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnRentedCar(Customer customer) {
        int carId = customer.getRentedCarId();

        try {
            Statement statement = carSharingDB.getConnection().createStatement();
            String query = "" +
                    "UPDATE customer " +
                    "SET rented_car_id = NULL " +
                    "WHERE id = " + customer.getId() + ";";
            statement.executeUpdate(query);

            query = "" +
                    "SELECT name FROM car " +
                    "WHERE id=" + carId + ";";
            ResultSet resultSet = statement.executeQuery(query);

            String returnedCarName = null;
            while (resultSet.next()) {
                returnedCarName = resultSet.getString("name");
            }

            customer.setRentedCarId(0);
            System.out.println("You've returned a rented car!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}