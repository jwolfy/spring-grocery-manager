package grocerymanager.controller;

import grocerymanager.model.Product;
import grocerymanager.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static grocerymanager.utils.ProductMockData.product_1;
import static grocerymanager.utils.ProductMockData.product_2;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllProducts() throws Exception {
        when(productService.findAll()).thenReturn(List.of(product_1(), product_2()));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":1,\"name\":\"Coffee\",\"quantity\":4}," +
                                "{\"id\":2,\"name\":\"Jam\",\"quantity\":2}]"));
    }

    @Test
    void shouldIncrementQuantityByGivenAmount() throws Exception {
        when(productService.incrementQuantity(1, 10)).thenReturn(new Product(1, "Coffee", 14));

        mockMvc.perform(post("/api/products/1/replenish/10"))
                .andExpect(content().json(
                        "{\"id\":1,\"name\":\"Coffee\",\"quantity\":14}"));
    }

    @Test
    void shouldDecrementQuantityByGivenAmount() throws Exception {
        when(productService.decrementQuantity(1, 5)).thenReturn(new Product(1, "Coffee", 0));

        mockMvc.perform(post("/api/products/1/deduct/5"))
                .andExpect(content().json(
                        "{\"id\":1,\"name\":\"Coffee\",\"quantity\":0}"));
    }
}