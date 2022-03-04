import enums.Gender;
import enums.Role;
import models.Customer;
import models.Staff;
import models.Store;
import servicesImpl.CashierServicesImpl;
import servicesImpl.CustomerServicesImpl;
import java.io.IOException;
public class Main {
    public static void main(String[] args) throws IOException {
        Store store1 = new Store();
        Staff staff1 =  new Staff(1,"johnny", Gender.MALE,"johnny@live.com","DEC-JAV-1", Role.CASHIER,"BSC");
        Staff staff2 =  new Staff(2,"dennis", Gender.MALE,"dennis@live.com","DEC-JAV-2", Role.MANAGER,"BSC");
        CustomerServicesImpl customerServicesImpl = new CustomerServicesImpl();
        CashierServicesImpl cashierServices = new CashierServicesImpl();
        cashierServices.fetchProductFromStore(staff1,store1,"productData.xlsx");
        Customer customer1 = new Customer(3,"loveth",Gender.FEMALE,"loveth@decagon.com");
        Customer customer2 = new Customer(4,"Johnny",Gender.MALE,"loveth@live.com");
        Customer customer3 = new Customer(5,"Lizzy",Gender.FEMALE,"loveth@live.com");
        Customer customer4 = new Customer(6,"Naomi",Gender.FEMALE,"loveth@live.com");
        Customer customer5 = new Customer(7,"Dennis",Gender.MALE,"loveth@live.com");
        customer1.setWallet(1_000_000.00);
        customer2.setWallet(1_000_000.00);
        customer3.setWallet(1_000_000.00);
        customer4.setWallet(1_000_000.00);
        customer5.setWallet(1_000_000.00);
        customerServicesImpl.addProductToCart(customer1,store1,store1.getProductList()[2].getId(),5);
        customerServicesImpl.addProductToCart(customer2,store1,store1.getProductList()[1].getId(),9);
        customerServicesImpl.addProductToCart(customer3,store1,store1.getProductList()[3].getId(),2);
        customerServicesImpl.addProductToCart(customer4,store1,store1.getProductList()[1].getId(),1);
        customerServicesImpl.addProductToCart(customer5,store1,store1.getProductList()[4].getId(),4);
        customerServicesImpl.makeOrder(customer1,store1);
        customerServicesImpl.makeOrder(customer2,store1);
        customerServicesImpl.makeOrder(customer3,store1);
        customerServicesImpl.makeOrder(customer4,store1);
        customerServicesImpl.makeOrder(customer5,store1);
        cashierServices.sellProducts(store1,staff1);
        System.out.println(store1.getAccount());
    }
}