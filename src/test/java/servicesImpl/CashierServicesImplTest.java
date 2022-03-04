package servicesImpl;

import enums.Gender;
import enums.Role;
import exceptions.StaffNotAuthorizedException;
import models.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CashierServicesImplTest {
    Store store1;
    Staff staff1;
    Staff staff2;
    CashierServicesImpl cashierServicesImpl;
    CustomerServicesImpl customerServicesImpl;
    Customer customer1;
    Customer customer2;
    Customer customer3;
    Customer customer4;
    Customer customer5;
    @Before
    public void setUp() throws Exception {
        store1 = new Store();
        staff1 =  new Staff(1,"johnny", Gender.MALE,"johnny@live.com","DEC-JAV-1", Role.CASHIER,"BSC");
        staff2 =  new Staff(2,"dennis", Gender.MALE,"dennis@live.com","DEC-JAV-2", Role.MANAGER,"BSC");
        cashierServicesImpl = new CashierServicesImpl();
        customerServicesImpl = new CustomerServicesImpl();
        cashierServicesImpl.fetchProductFromStore(staff1,store1,"productData.xlsx");
        customer1 = new Customer(3,"loveth",Gender.FEMALE,"loveth@decagon.com");
        customer2 = new Customer(4,"Johnny",Gender.MALE,"loveth@live.com");
        customer3 = new Customer(5,"Isioma",Gender.FEMALE,"loveth@live.com");
        customer4 = new Customer(6,"Naomi",Gender.FEMALE,"loveth@live.com");
        customer5 = new Customer(7,"Dennis",Gender.MALE,"loveth@live.com");
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

    }

    @Test
    public void shouldThrowStaffNotAuthorizedExceptionIfStaffRoleIsNotCashier(){
        assertThrows(StaffNotAuthorizedException.class,()->{
            cashierServicesImpl.fetchProductFromStore(staff2,store1,"productData.xlsx");
        });
    }

    @Test
    public void fetchProductFromStore() throws IOException {
        cashierServicesImpl.fetchProductFromStore(staff1,store1,"productData.xlsx");
        assertEquals(Optional.of(1),Optional.of(store1.getProductList()[0].getId()));
    }

    @Test
    public void printReceipt() {
        customerServicesImpl.addProductToCart(customer1,store1,5,1);
        customerServicesImpl.makeOrder(customer1,store1);
        cashierServicesImpl.sellProducts(store1,staff1);
        System.out.println(store1.getTransactionHistory());
        //assertTrue();
    }

//    @Test
//    public void shouldAssertThatStoreTransactionHistoryIsNotNullIfCustomerSuccessfullyCheckedOut() throws IOException {
//        cashierServicesImpl.fetchProductFromStore(staff1,store1,"productData.xlsx");
//        customerServicesImpl.addProductToCart(customer1,store1,store1.getProductList()[1].getId(),1);
//        customerServicesImpl.checkout(customer1,staff1,store1,cashierServicesImpl);
//        cashierServicesImpl.printReceipt(3,store1);
//        assertNotNull(store1.getTransactionHistory());
//
//    }

//    @Test
//    public void shouldAssertThatStoreTransactionHistoryIsNotNullIfCustomerSuccessfullyCheckedOutAndCustomerHasTransactionHistory() throws IOException {
//        cashierServicesImpl.fetchProductFromStore(staff1,store1,"productData.xlsx");
//        customerServicesImpl.addProductToCart(customer1,store1,store1.getProductList()[1].getId(),1);
//        customerServicesImpl.checkout(customer1,staff1,store1,cashierServicesImpl);
//        cashierServicesImpl.fetchProductFromStore(staff1,store1,"productData.xlsx");
//        customerServicesImpl.addProductToCart(customer1,store1,store1.getProductList()[1].getId(),1);
//        customerServicesImpl.checkout(customer1,staff1,store1,cashierServicesImpl);
//        cashierServicesImpl.printReceipt(3,store1);
//        assertNotNull(store1.getTransactionHistory());
//    }

    @Test
    public void cashierCanViewProduct() throws IOException {
        cashierServicesImpl.fetchProductFromStore(staff1,store1,"productData.xlsx");
        List<Product> electronicGoods = cashierServicesImpl.viewProductByCategory(store1, "CLOTHING");
        assertEquals("CLOTHING", electronicGoods.get(0).getCategory().getName());
        assertEquals("CLOTHING", electronicGoods.get(1).getCategory().getName());
        assertEquals("CLOTHING", electronicGoods.get(2).getCategory().getName());
    }

    @Test
    public  void sellProducts () {
        cashierServicesImpl.sellProducts(store1,staff1);
        assertTrue(customer4.getCart().isEmpty());
        customerServicesImpl.addProductToCart(customer4,store1,store1.getProductList()[0].getId(),1);
        customerServicesImpl.makeOrder(customer4,store1);
        cashierServicesImpl.sellProducts(store1,staff1);
        assertTrue(customer4.getCart().isEmpty());
    }
}