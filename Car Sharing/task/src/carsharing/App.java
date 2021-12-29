package carsharing;

import carsharing.dao.Company;
import carsharing.dao.CompanyManagement;
import carsharing.dao.ManagementInterface;

import java.util.List;
import java.util.Scanner;

public class App {
    private CarSharingDB carSharingDB;
    private Scanner scanner = new Scanner(System.in);
    private ManagementInterface management;

    public App() {
        this.carSharingDB = new CarSharingDB();
        this.management = new CompanyManagement(carSharingDB);
    }

    public void start(String[] args) {
        carSharingDB.createDB(args);
        displayMenu();
    }

    private void displayMenu() {
        while (true) {
            System.out.println("" +
                    "1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit"
            );

            int request = Integer.parseInt(scanner.nextLine());
            switch (request) {
                case 1:
                    managerMenu();
                    break;
                case 2:
                    getCustomerList();
                    break;
                case 3:
                    createCustomer();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Such option doesn't exist.");
            }
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        management.createCustomer(name);
    }

    private void getCustomerList() {
        List<Customer> customerList = management.getCustomerList();
        if (customerList.isEmpty()) {
            System.out.println("The customer list is empty!\n");
        } else {
            System.out.println("Customer list:");
            customerList.forEach(customer ->
                    System.out.println(customer.getId() + ". " + customer.getName()));
            System.out.println("0. Back");

            int request = Integer.parseInt(scanner.nextLine()) - 1;
            if (request == -1) {
                return;
            }

            customerMenu(customerList.get(request));
        }
    }

    private void customerMenu(Customer customer) {
        while (true) {
            System.out.println("" +
                    "1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");

            int request = Integer.parseInt(scanner.nextLine());
            switch (request) {
                case 1:
                    rentACar(customer);
                    break;
                case 2:
                    returnRentedCar(customer);
                    break;
                case 3:
                    myRentedCar(customer);
                    break;
                case 0:
                    return;
            }
        }
    }

    private void returnRentedCar(Customer customer) {
        if (customer.getRentedCarId() < 1) {
            System.out.println("You didn't rent a car!");
            return;
        }

        management.returnRentedCar(customer);
    }

    private void rentACar(Customer customer) {
        if (customer.getRentedCarId() > 0) {
            System.out.println("You've already rented a car!");
            return;
        }
        System.out.println("Choose a company:");
        List<Company> companiesList = management.getCompaniesList();

        companiesList.forEach(System.out::println);
        System.out.println("0. Back");

        int requestCompany = Integer.parseInt(scanner.nextLine()) - 1;
        if (requestCompany == -1) {
            return;
        }

        int chosenCompany = companiesList.get(requestCompany).getId();

        System.out.println("Choose a car:");
        List<Car> carList = management.getCarList(chosenCompany);
        for (int i = 0; i < carList.size(); i++) {
            System.out.println(i + 1 + ". " + carList.get(i).getName());
        }
        System.out.println("0. Back");

        int carRequest = Integer.parseInt(scanner.nextLine()) - 1;
        if (carRequest == -1) {
            return;
        }

        int carIndex = carList.get(carRequest).getId();
        String carName = carList.get(carRequest).getName();

        management.rentACar(customer, carIndex, carName);
    }

    private void myRentedCar(Customer customer) {
        String answer = management.getRentedCar(customer);
        System.out.println(answer);
    }

    private void managerMenu() {
        while (true) {
            System.out.println("" +
                    "1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");

            int request = Integer.parseInt(scanner.nextLine());
            switch (request) {
                case 1:
                    getCompaniesList();
                    break;
                case 2:
                    createCompany();
                    break;
                case 8:
                    deleteCompany();
                    break;
                case 88:
                    dropDatabase();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void getCompaniesList() {
        List<Company> companiesList = management.getCompaniesList();
        if (companiesList.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println();
            System.out.println("Choose a company:");
            companiesList.forEach(System.out::println);
            System.out.println("0. Back");
            int companyId = Integer.parseInt(scanner.nextLine()) - 1;
            if (companyId == -1) {
                return;
            }
            displayCompanyMenu(companiesList.get(companyId));
        }
    }

    private void createCompany() {
        System.out.println();
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        management.createCompany(name);
    }

    private void deleteCompany() {
        System.out.println("Enter the company name which has to be deleted:");
        management.deleteCompany(scanner.nextLine());
    }

    private void dropDatabase() {
        management.dropDatabase();
    }


    private void displayCompanyMenu(Company company) {
        System.out.println(company.getName() + " company");
        while (true) {
            System.out.println("" +
                    "1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back"
            );
            int request = Integer.parseInt(scanner.nextLine());
            switch (request) {
                case 1:
                    getCarList(company.getId());
                    break;
                case 2:
                    createCar(company.getName());
                    break;
                case 0:
                    return;
            }
        }
    }

    private void getCarList(int companyId) {
        List<Car> carList = management.getCarList(companyId);
        if (carList.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            for (int i = 0; i < carList.size(); i++) {
                System.out.println(i + 1 + ". " + carList.get(i).getName());
            }
        }
        System.out.println();
    }

    private void createCar(String companyName) {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        int companyId = management.getCompanyId(companyName);
        management.createCar(carName, companyId);
    }
}
