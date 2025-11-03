package mainapp;
import dao.ProductDAO;
import model.Product;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import exception.ProductNotFoundException;

public class SupermarketApp {

    private static final double GST_RATE = 0.05;

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.getAllProducts();
        Map<String, Integer> cart = new LinkedHashMap<>();
        System.out.println("------------>HAPPY SUPERMARKET LOGIN<-------------");
        String username = "vyshu";
        String password = "vyshu123";
        int attempts = 3;
        boolean loggedIn = false;
        while (attempts > 0) {
            System.out.print("Enter Username: ");
            String inputUser = sc.nextLine();

            System.out.print("Enter Password: ");
            String inputPass = sc.nextLine();

            if (inputUser.equals(username) && inputPass.equals(password)) {
                System.out.println("\nLogin Successful! Welcome " + inputUser + "!");
                loggedIn = true;
                break;
            } else {
                attempts--;
                System.out.println(" Invalid credentials. Attempts left: " + attempts);
            }
        }

        if (!loggedIn) {
            System.out.println("Too many failed attempts. Exiting...");
            return;
        }

        System.out.println("====================================================");
        System.out.println("            WELCOME TO HAPPY SUPERMARKET!!!        ");
        System.out.println("====================================================");
        System.out.println("Available Products:\n");
        System.out.printf("%-5s %-15s %-10s %-10s\n", "ID", "Product", "Price(₹)", "Stock");
        System.out.println("----------------------------------------------------");
        //Displays the total items to the customer
        for (Product p : products) {
            System.out.printf("%-5d %-15s %-10.2f %-10d\n",
                    p.getId(), p.getName(), p.getPrice(), p.getQuantity());
        }
        System.out.println("----------------------------------------------------");

        while (true) {
            System.out.print("Enter Product ID (0 to finish): ");
            int id = sc.nextInt();

            if (id == 0) break;

            // Find the product from the list
            Product selectedProduct;
            try {
                selectedProduct = products.stream()
                        .filter(p -> p.getId() == id)
                        .findFirst()
                        .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found!"));
            } catch (ProductNotFoundException e) {
                System.out.println(" " + e.getMessage());
                continue; // ask again instead of crashing
            }


            System.out.print("Enter Quantity: ");
            int qty = sc.nextInt();

            // Check stock limit
            if (qty > selectedProduct.getQuantity()) {
                System.out.println("Only " + selectedProduct.getQuantity() + " units of " + selectedProduct.getName() + " are available. Please enter a lower quantity.");
                continue;
            } else if (qty <= 0) {
                System.out.println("Quantity must be at least 1.");
                continue;
            }

            // Add valid product to cart
            cart.put(selectedProduct.getName(),
                    cart.getOrDefault(selectedProduct.getName(), 0) + qty);

        }

        // If cart is empty
        if (cart.isEmpty()) {
            System.out.println(" No items selected. Exiting...");
            return;
        }

        // Display selected products for confirmation
        System.out.println("\n************************************************");
        System.out.println("              SELECTED PRODUCTS             ");
        System.out.println("************************************************");
        System.out.printf("%-15s %-10s\n", "Item", "Quantity");
        System.out.println("----------------------------------------------------");
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            System.out.printf("%-15s %-10d\n", entry.getKey(), entry.getValue());
        }
        System.out.println("----------------------------------------------------");

        System.out.print("Confirm your products? (yes/no): ");
        String confirm = sc.next();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Order cancelled.");
            return;
        }

        // Print final bill
        printBill(dao, cart);

        System.out.println(" Thank you for shopping with Happy Supermarket!");
        System.out.println("====================================================");
    }

    private static void printBill(ProductDAO dao, Map<String, Integer> cart) throws SQLException {
        double subtotal = 0.0;

        System.out.println("\n***************************************************");
        System.out.println("                     BILL RECEIPT                   ");
        System.out.println("***************************************************");
        System.out.printf("%-15s %-10s %-10s\n", "Item", "Qty", "Price");
        System.out.println("----------------------------------------------------");

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String product = entry.getKey();
            int qty = entry.getValue();
            double price = getProductPriceByName(dao, product);
            double total = price * qty;
            subtotal += total;
            System.out.printf("%-15s %-10d ₹%-10.2f\n", product, qty, total);
        }

        double gst = subtotal * GST_RATE;
        double finalAmount = subtotal + gst;

        System.out.println("----------------------------------------------------");
        System.out.printf("%-20s ₹%-10.2f\n", "Subtotal:", subtotal);
        System.out.printf("%-20s ₹%-10.2f\n", "GST (5%):", gst);
        System.out.printf("%-20s ₹%-10.2f\n", "Final Amount:", finalAmount);
        System.out.println("----------------------------------------------------");
        System.out.println("Date & Time: " + LocalDateTime.now());
        System.out.println("====================================================");
    }

    private static double getProductPriceByName(ProductDAO dao, String productName) throws SQLException {
        for (Product p : dao.getAllProducts()) {
            if (p.getName().equalsIgnoreCase(productName)) {
                return p.getPrice();
            }
        }
        return 0;
    }
}