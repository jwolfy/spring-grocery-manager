package grocerymanager.service;

import grocerymanager.model.Product;
import grocerymanager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found by id " + id));

    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product incrementQuantity(int id, int amount) {
        Product product = findById(id);
        product.setQuantity(product.getQuantity() + amount);
        return productRepository.save(product);
    }

    @Override
    public Product decrementQuantity(int id, int amount) {
        Product product = findById(id);
        if (product.getQuantity() >= amount) {
            product.setQuantity(product.getQuantity() - amount);
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Negative quantity is not allowed");
        }
    }
}
