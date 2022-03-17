package grocerymanager.service;

import grocerymanager.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(int id);
    Product save(Product product);
    void deleteById(int id);
    Product incrementQuantity(int id, int amount);
    Product decrementQuantity(int id, int amount);
}
