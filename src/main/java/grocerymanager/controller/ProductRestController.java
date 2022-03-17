package grocerymanager.controller;

import grocerymanager.model.Product;
import grocerymanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/products")
public class ProductRestController {
    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("{id}")
    public Product findById(@PathVariable int id) {
        return productService.findById(id);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        product.setId(0);
        productService.save(product);
        return product;
    }

    @PutMapping("{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
        Product foundProduct = productService.findById(id);
        foundProduct.updateFrom(product);
        return productService.save(foundProduct);
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
    }

    @PostMapping("{id}/replenish/{amount}")
    public Product incrementQuantity(@PathVariable int id, @PathVariable int amount) {
        return productService.incrementQuantity(id, amount);
    }

    @PostMapping("{id}/deduct/{amount}")
    public Product decrementQuantity(@PathVariable int id, @PathVariable int amount) {
        return productService.decrementQuantity(id, amount);
    }
}
