package grocerymanager.service;

import grocerymanager.model.Product;
import grocerymanager.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED)
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    @Override
    public Product findById(int id) {
        log.info("Fetching product by id: {} ", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found by id " + id));

    }

    @Override
    public Product save(Product product) {
        log.info("Saving product: {} ", product);
        return productRepository.save(product);
    }

    @Override
    public void deleteById(int id) {
        productRepository.deleteById(id);
        log.info("Product with id: {} deleted", id);
    }

    @Override
    public Product incrementQuantity(int id, int amount) {
        Product product = getProductForUpdate(id);
        product.setQuantity(product.getQuantity() + amount);
        log.info("Product with id: {} incrementing quantity by {}", id, amount);
        return productRepository.save(product);
    }

    @Override
    public Product decrementQuantity(int id, int amount) {
        Product product = getProductForUpdate(id);
        if (product.getQuantity() >= amount) {
            product.setQuantity(product.getQuantity() - amount);
            log.info("Product with id: {} decrementing quantity by {}", id, amount);
            return productRepository.save(product);
        } else {
            log.warn("Attempted to set negative quantity for product with id: {}", id);
            throw new RuntimeException("Negative quantity is not allowed");
        }
    }

    private Product getProductForUpdate(int id) {
        return productRepository.findByIdAndLock(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found by id " + id));
    }
}
