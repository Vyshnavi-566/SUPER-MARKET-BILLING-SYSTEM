package dao;
import db.DBConnection;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import exception.ProductNotFoundException;

public class ProductDAO {
        public List<Product> getAllProducts() {
            List<Product> products = new ArrayList<>();
            String query = "SELECT * FROM product";
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return products;
        }

        public double getProductPrice(int id) throws SQLException {
            String query = "SELECT price FROM product WHERE product_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getDouble("price");
                else { throw new ProductNotFoundException("Product with ID " + id + " not found!"); }
            }catch(SQLException | ProductNotFoundException e){
                e.printStackTrace();
            }
            return 0;
        }

        public String getProductName(int id) throws SQLException {
            String query = "SELECT product_name FROM product WHERE product_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getString("product_name");
            }
            return null;
        }
    }