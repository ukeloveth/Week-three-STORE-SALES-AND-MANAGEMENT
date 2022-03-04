package services;

import models.Customer;
import models.Order;
import models.Staff;
import models.Store;

import java.io.IOException;
import java.util.List;

public interface CashierServices extends ViewProductByCategoryImpl {
    void fetchProductFromStore(Staff staff, Store store, String filename) throws IOException;
    void printReceipt(Integer customerId, Store store);
    void sellProducts(Store store, Staff staff);
    List<Order> sortOrders(List<Order> orderList);
}
