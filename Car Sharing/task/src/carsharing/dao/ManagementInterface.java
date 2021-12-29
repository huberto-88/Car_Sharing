package carsharing.dao;

import carsharing.Car;
import carsharing.Customer;

import java.util.List;

public interface ManagementInterface {

    List<Company> getCompaniesList();

    Company getCompanyById(int id);

    void createCompany(String name);

    void deleteCompany(String name);

    void dropDatabase();

    List<Car> getCarList(int companyId);

    void createCar(String carName, int companyId);

    int getCompanyId(String companyName);

    void createCustomer(String name);

    List<Customer> getCustomerList();

    String getRentedCar(Customer customer);

    void rentACar(Customer customerId, int carIndex, String carName);

    void returnRentedCar(Customer customer);
}
