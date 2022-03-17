package grocerymanager.utils;

import grocerymanager.model.Product;

public class ProductMockData {
    public static Product product_1() {
        return new Product(1, "Coffee", 4);
    }

    public static Product product_2() {
        return new Product(2, "Jam", 2);
    }
}
