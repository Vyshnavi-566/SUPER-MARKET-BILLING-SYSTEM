package model;

public class Product {
        private int id;
        private String name;
        private String category;
        private double price;
        private int quantity;
        // product table model
        public Product(int id, String name, String category, double price, int quantity) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
            this.quantity = quantity;
        }
        //getting the details
        public int getId() { return id; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }

