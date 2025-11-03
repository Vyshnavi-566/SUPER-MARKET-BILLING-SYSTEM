package mainapp;

import model.Product;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class BillingTest {

    //  Test for Subtotal Calculation
    @Test
    void testSubtotalCalculation() {
        // User selected Rice (2 qty) and Sugar (1 qty)
        List<Product> products = List.of(
                new Product(1, "Rice", "Grocery", 60.0, 50),
                new Product(2, "Sugar", "Grocery", 40.0, 30)
        );

        Map<String, Integer> cart = new HashMap<>();
        cart.put("Rice", 2);
        cart.put("Sugar", 1);

        double subtotal = 0.0;
        for (Map.Entry<String, Integer> item : cart.entrySet()) {
            for (Product p : products) {
                if (p.getName().equalsIgnoreCase(item.getKey())) {
                    subtotal += p.getPrice() * item.getValue();
                }
            }
        }

        assertEquals(160.0, subtotal, 0.01,
                "Subtotal should be correct (2*60 + 1*40 = 160)");
    }

    //  Test for GST Calculation
    @Test
    void testGSTCalculation() {
        double subtotal = 160.0;
        double gstRate = 0.05; // 5%
        double gst = subtotal * gstRate;

        assertEquals(8.0, gst, 0.01, "GST should be 5% of subtotal");
    }

    //  Test for Out of Stock Condition
    @Test
    void testOutOfStock() {
        Product sugar = new Product(2, "Sugar", "Grocery", 40.0, 30); // Stock = 30
        int requestedQty = 35; // User requested more than available

        boolean isOutOfStock = requestedQty > sugar.getQuantity();

        assertTrue(isOutOfStock, "Should display 'Out of Stock' when requested quantity exceeds stock");
    }
}
