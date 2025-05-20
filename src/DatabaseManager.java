import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/superstore"; // your database name
    private static final String USER = "username"; // your MySQL username
    private static final String PASSWORD = "password"; // your MySQL password

    // Get connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to load items from database
    public static Map<String, Map<String, Double>> loadItems() {
        Map<String, Map<String, Double>> items = new HashMap<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT category FROM items")) {

            while (rs.next()) {
                String category = rs.getString("category");
                items.put(category, new HashMap<>());
            }

            for (String category : items.keySet()) {
                PreparedStatement ps = conn.prepareStatement("SELECT name, price FROM items WHERE category = ?");
                ps.setString(1, category);
                ResultSet rs2 = ps.executeQuery();
                while (rs2.next()) {
                    String name = rs2.getString("name");
                    double price = rs2.getDouble("price");
                    items.get(category).put(name, price);
                }
                rs2.close();
                ps.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return items;
    }

    // Method to save order to the database
    public static void saveOrder(java.util.List<SuperStoreBillingApp.CartItem> cart) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO orders (item_name, quantity, total) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            for (SuperStoreBillingApp.CartItem item : cart) {
                ps.setString(1, item.name);
                ps.setInt(2, item.quantity);
                ps.setDouble(3, item.total);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
