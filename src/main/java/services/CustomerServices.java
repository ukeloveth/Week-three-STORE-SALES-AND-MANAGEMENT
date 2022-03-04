package services;

import models.Customer;
import models.Staff;
import models.Store;
import servicesImpl.CashierServicesImpl;

public interface CustomerServices extends ViewProductByCategoryImpl {
    void addProductToCart(Customer customer, Store store, Integer id,Integer quantity);
    void removeProductFromCart(Customer customer, Integer productId, Integer quantity);
    void makeOrder(Customer customer,Store store);
}
