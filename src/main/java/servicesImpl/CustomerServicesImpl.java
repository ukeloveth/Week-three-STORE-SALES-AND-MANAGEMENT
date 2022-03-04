package servicesImpl;

import exceptions.CartIsEmptyException;
import exceptions.ProductDoesNotExistException;
import exceptions.QuantityIsGreaterException;
import models.*;
import services.CustomerServices;

import java.util.*;

public class CustomerServicesImpl implements CustomerServices {
    @Override
    public void addProductToCart(Customer customer, Store store, Integer id, Integer quantity) {
        Map<Product,Integer> cartItems = customer.getCart();
        Product[] productListFromCart = cartItems.keySet().toArray(new Product[0]);
        Product[] productListFromStore = store.getProductList();
        Product foundProductInStore = Arrays.stream(productListFromStore)
                .filter(product -> Objects.equals(product.getId(), id))
                .findFirst().orElse(null);
        if (foundProductInStore == null || foundProductInStore.getQuantity() == 0){
            throw new ProductDoesNotExistException("Product does not exist or is out of stock");
        }
        Product foundProductInCart = Arrays.stream(productListFromCart)
                .filter(product -> Objects.equals(product.getId(), id))
                .findFirst().orElse(null);
        if (foundProductInCart != null){
            if (foundProductInCart.getQuantity() <= foundProductInStore.getQuantity()){
                int initialQuantity = cartItems.get(foundProductInCart);
                int newQuantity = initialQuantity + quantity;
                if ((newQuantity <= foundProductInStore.getQuantity())){
                    cartItems.put(foundProductInStore,newQuantity);
                }else {
                    throw new QuantityIsGreaterException("Quantity you are trying to add to 1cart is greater than quantity in store");
                }
            }
        }else{
            if (quantity <= foundProductInStore.getQuantity()){
                cartItems.put(foundProductInStore,quantity);
            }else {
                throw new QuantityIsGreaterException("Quantity you are trying to add to cart is greater than quantity in store");
            }
        }
    }

    @Override
    public void removeProductFromCart(Customer customer, Integer productId, Integer quantity) {
        Map<Product,Integer> cartItems = customer.getCart();
        if (cartItems.isEmpty()) {
            throw new CartIsEmptyException("Cart is empty");
        }
        Product productToRemove = null;
        for (Map.Entry<Product,Integer> item : cartItems.entrySet()){
            if (Objects.equals(item.getKey().getId(), productId)) {
                productToRemove = item.getKey();
                break;
            }
        }
        if (productToRemove != null) {
            if ((cartItems.get(productToRemove) - quantity) >= 0) {
                quantity = cartItems.get(productToRemove) - quantity;
                cartItems.put(productToRemove,quantity);
                customer.setCart(cartItems);
                cartItems.remove(productToRemove,0);
                System.out.println("Product has been removed");
            }else{
                throw new QuantityIsGreaterException("Quantity you want to remove is greater than quantity in cart");

            }
        }else{
            throw new ProductDoesNotExistException("Product does not exist in the cart");
        }
    }

    @Override
    public void makeOrder(Customer customer,Store store) {
        for(Map.Entry<Product,Integer> item: customer.getCart().entrySet()){

                Product productFromCart = item.getKey();
                Integer quantity = item.getValue();
                Order newOrder = new Order(customer,productFromCart,quantity);
                store.getOrderList().add(newOrder);

        }

    }

}
