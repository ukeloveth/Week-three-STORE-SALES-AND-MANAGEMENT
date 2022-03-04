package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    private Integer id;
    private  String name;
    private String brand;
    private String modelName;
    private double price;
    private Integer quantity;
    private Category category;
}
