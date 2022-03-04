package models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Order{
//    private Integer id;
    private Customer customer;
    private Product product;
    private Integer quantity;
}
