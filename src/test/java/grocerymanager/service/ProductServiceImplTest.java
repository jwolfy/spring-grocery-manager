package grocerymanager.service;

import grocerymanager.model.Product;
import grocerymanager.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static grocerymanager.utils.ProductMockData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product_1(), product_2()));

        List<Product> products = productService.findAll();

        assertEquals(2, products.size());
    }

    @Test
    void shouldFindProductById() {
        when(productRepository.findById(2)).thenReturn(Optional.of(product_2()));

        Product product = productService.findById(2);

        assertEquals(2, product.getId());
        assertEquals("Jam", product.getName());
        assertEquals(2, product.getQuantity());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfNoProductFound() {
        assertThrows(EntityNotFoundException.class, () -> productService.findById(2));
    }

    @Test
    void shouldSaveProduct() {
        when(productRepository.save(any())).then(returnsFirstArg());

        Product product = productService.save(product_1());

        assertEquals(1, product.getId());
        assertEquals("Coffee", product.getName());
        assertEquals(4, product.getQuantity());
    }

    @Test
    void deleteShouldCallDeleteById() {
        productService.deleteById(2);

        verify(productRepository, times(1)).deleteById(2);
    }

    @Test
    void shouldIncrementQuantityByGivenAmount() {
        when(productRepository.findById(2)).thenReturn(Optional.of(product_2()));
        when(productRepository.save(any())).then(returnsFirstArg());

        Product product = productService.incrementQuantity(2, 10);

        assertEquals(12, product.getQuantity());
    }

    @Test
    void shouldDecrementQuantityByGivenAmount() {
        when(productRepository.findById(2)).thenReturn(Optional.of(product_2()));
        when(productRepository.save(any())).then(returnsFirstArg());

        Product product = productService.decrementQuantity(2, 2);

        assertEquals(0, product.getQuantity());
    }

    @Test
    void shouldThrowExceptionIfResultingQuantityIsNegative() {
        when(productRepository.findById(2)).thenReturn(Optional.of(product_2()));

        assertThrows(RuntimeException.class, () -> productService.decrementQuantity(2, 10));
    }
}