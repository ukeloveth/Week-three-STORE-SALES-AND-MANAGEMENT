package models;

import lombok.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

@Data
public class Store {
    private List<Applicant> applicantList;
    private List<Staff> staffList;
    private Product[] productList;
    private double account;
    private Map<Integer,List<TransactionData>> transactionHistory;
    private List<Order> orderList;

    public Store() {
        this.applicantList = new ArrayList<>();
        this.productList = new Product[256];
        this.transactionHistory = new HashMap<>();
        this.staffList = new ArrayList<>();
        this.orderList = new LinkedList<>();
    }
}
