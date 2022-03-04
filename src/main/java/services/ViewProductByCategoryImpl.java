package services;

import models.Product;
import models.Store;

import java.util.ArrayList;
import java.util.List;

public interface ViewProductByCategoryImpl {
    default List<Product> viewProductByCategory(Store store, String category){
        List<Product> productArrayList = new ArrayList<>();
        for(int i = 0; i< store.getProductList().length; i++){
            Product product = store.getProductList()[i];
            if(product.getCategory().getName().equals(category))
                productArrayList.add(product);
        }
        return productArrayList;
    }
}
