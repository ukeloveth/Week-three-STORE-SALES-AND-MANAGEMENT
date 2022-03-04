package servicesImpl;

import enums.Role;
import exceptions.CartIsEmptyException;
import exceptions.ProductDoesNotExistException;
import exceptions.PurchaseCouldNotBeValidatedException;
import exceptions.StaffNotAuthorizedException;
import models.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import services.CashierServices;
import services.StaffServices;
import utils.OrderSorter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class CashierServicesImpl implements CashierServices, StaffServices {
    @Override
    public void fetchProductFromStore(Staff staff, Store store,String fileName) throws IOException {
        if (!Role.CASHIER.equals(staff.getRole())){
            throw new StaffNotAuthorizedException("You are not authorized to perform this action");
        }
        String fileToReadFrom = "src/productData/"+fileName;

        try {
            File file = new File(fileToReadFrom);
            FileInputStream fis = new FileInputStream(file);

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            Product[] productsList = new Product[sheet.getLastRowNum()];
            for (int i = 1; i <= sheet.getLastRowNum() ; i++) {
                XSSFRow row = sheet.getRow(i);
                Product product = new Product();
                Category category = new Category();
                //System.out.println(row.getSheet());
                //if(row.getRowNum() != 0){
                    for (int j = 1; j < row.getLastCellNum(); j++) {
                        XSSFCell cell = row.getCell(j);
                        switch (cell.getCellType()){
                            case STRING:
                                switch (cell.getColumnIndex()) {
                                    case 2 :
                                        product.setName(cell.getStringCellValue());
                                        break;
                                    case 3 :
                                        product.setBrand(cell.getStringCellValue());
                                    case 4 :
                                        product.setModelName(cell.getStringCellValue());
                                    case 7 :
                                        category.setName(cell.getStringCellValue());
                                    case 8 :
                                        category.setDescription(cell.getStringCellValue());
                                        product.setCategory(category);
                                        productsList[i-1] = product;
                                        store.setProductList(productsList);
                                        break;
                                    default :
                                };
                                break;
                            case NUMERIC:
                                //System.out.println(cell.getNumericCellValue());
                                switch (cell.getColumnIndex()) {
                                    case 1 :
                                        //System.out.println(cell.getNumericCellValue());
                                        product.setId((int) cell.getNumericCellValue());
                                        break;
                                    case 5 :
                                        product.setPrice(cell.getNumericCellValue());
                                        break;
                                    case 6 :
                                        product.setQuantity((int) cell.getNumericCellValue());
                                        break;
                                    default :
                                }
                                break;
                            default: throw new RuntimeException("Exception");
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printReceipt(Integer customerId, Store store) {
        Map<Integer, List<TransactionData>> storeTransactionHistory = store.getTransactionHistory();
        List<TransactionData> customerListOfTransactions = storeTransactionHistory.get(customerId);
        TransactionData customerLastTransaction = customerListOfTransactions.get(customerListOfTransactions.size() - 1);
        System.out.println(customerLastTransaction);
    }

    @Override
    public List<Order> sortOrders(List<Order> orderList) {

        List<Order> dischargeOrderList = new LinkedList<>();

        List<Order> compared = new ArrayList<>();

        for (Order order: orderList) {

            List<Order> similarOrders = orderList.stream()
                    .filter(p-> !Objects.equals(p.getCustomer(), order.getCustomer()) & Objects.equals(p.getProduct(), order.getProduct()) & !compared.contains(order))
                    .sorted(Comparator.comparingInt(Order::getQuantity))
                    .collect(Collectors.toList());

            compared.addAll(similarOrders);

            if(!similarOrders.isEmpty()){
                for (int i = 0; i < similarOrders.size(); i++) {
                    Order sortedOrder = similarOrders.get(i);

                    if (order.getQuantity() < sortedOrder.getQuantity()) {
                        dischargeOrderList.add(order);
                    } else {

                        dischargeOrderList.add(sortedOrder);

                        if(i== similarOrders.size()-1){
                            dischargeOrderList.add(order);
                        }
                    }
                }
            }else {


                if(!compared.contains(order)){
                    dischargeOrderList.add(order);
                }
            }
        }
        return dischargeOrderList;
    }

    @Override
    public void sellProducts(Store store, Staff staff) {

        List<Order> orderList = this.sortOrders(store.getOrderList());
        for (Order item : orderList) {
            System.out.println(item);
        }

        List<Order> ordersSettled = new ArrayList<>();

        if (staff.getRole().equals(Role.CASHIER)) {

            for (Order order : orderList) {

                Customer customer = order.getCustomer();
                Product product = order.getProduct();

                Integer quantity = order.getQuantity();

                double totalPrice = product.getPrice() * quantity;

                if (customer.getWallet() < totalPrice) {

                    throw new PurchaseCouldNotBeValidatedException("Purchase could not be validated, please check your balance and try again");
                }else{

                    if(product.getQuantity()>= quantity){

                        store.setAccount(store.getAccount() + totalPrice);
                        customer.setWallet(customer.getWallet() - totalPrice);
                        product.setQuantity(product.getQuantity() - quantity);
                        Map<Product,Integer> cart = customer.getCart();

                        cart.clear();
                        customer.setCart(cart);


                        Map<Integer, List<TransactionData>> transactionData = store.getTransactionHistory();

                        if (transactionData.get(customer.getId()) != null) {
                            List<TransactionData> customerListOfTransactions = transactionData.get(customer.getId());
                            TransactionData transaction = new TransactionData(customer.getName(), LocalDateTime.now(), totalPrice);
                            customerListOfTransactions.add(transaction);
                            transactionData.put(customer.getId(), customerListOfTransactions);
                            store.setTransactionHistory(transactionData);
                        } else {
                            List<TransactionData> newCustomerListOfTransactions = new ArrayList<>();
                            TransactionData transaction = new TransactionData(customer.getName(), LocalDateTime.now(), totalPrice);
                            newCustomerListOfTransactions.add(transaction);
                            transactionData.put(customer.getId(), newCustomerListOfTransactions);
                            store.setTransactionHistory(transactionData);
                        }

                        printReceipt(customer.getId(), store);

                        ordersSettled.add(order);
                    }else{

                        throw new ProductDoesNotExistException("Product is out of stock or below required quantity");
                    }

                }


            }
            }else {

                throw new StaffNotAuthorizedException("You are not authorized to perform this action");
        }


        orderList.removeAll(ordersSettled);

        store.setOrderList(orderList);

    }


    @Override
    public void work(Staff staff) {
        System.out.println(staff.getName() + "is working");
    }
}
