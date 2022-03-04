package utils;

import models.Order;

import java.util.Comparator;

public class OrderSorter implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getQuantity().compareTo(o2.getQuantity());
    }
}
