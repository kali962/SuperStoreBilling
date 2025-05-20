import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.util.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class SuperStoreBillingApp extends JFrame {
    private JComboBox<String> categoryCombo;
    private JComboBox<String> itemCombo;
    private JTextField quantityField;
    private DefaultTableModel cartModel;
    private java.util.List<CartItem> cart = new ArrayList<>();
    private Map<String, Map<String, Double>> items = new HashMap<>();
    private JTextArea billArea; // Add JTextArea for bill preview

    public SuperStoreBillingApp() {
        setTitle("Alee's Super Store");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JOptionPane.showMessageDialog(this, "Welcome to Alee's Super Store!", "Welcome", JOptionPane.INFORMATION_MESSAGE);

        setupItems();
        initComponents();
    }

    private void setupItems() {
        // Using DatabaseManager to load items from DB
        items = DatabaseManager.loadItems();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        categoryCombo = new JComboBox<>(items.keySet().toArray(new String[0]));
        itemCombo = new JComboBox<>();
        quantityField = new JTextField(5);
        JButton addButton = new JButton("Add to Cart");

        updateItems();

        categoryCombo.addActionListener(e -> updateItems());

        addButton.addActionListener(e -> addItemToCart());

        topPanel.add(new JLabel("Category:"));
        topPanel.add(categoryCombo);
        topPanel.add(new JLabel("Item:"));
        topPanel.add(itemCombo);
        topPanel.add(new JLabel("Quantity:"));
        topPanel.add(quantityField);
        topPanel.add(addButton);

        cartModel = new DefaultTableModel(new Object[]{"Item", "Price", "Quantity", "Total"}, 0);
        JTable cartTable = new JTable(cartModel);

        JButton generateButton = new JButton("Generate Bill (PDF)");
        generateButton.addActionListener(e -> generatePDFBill());

        // Bill preview area
        billArea = new JTextArea(10, 40);
        billArea.setEditable(false);

        // Create a split pane for the cart table and bill preview
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(cartTable), new JScrollPane(billArea));
        splitPane.setDividerLocation(500);

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(generateButton, BorderLayout.SOUTH);
    }

    private void updateItems() {
        String category = (String) categoryCombo.getSelectedItem();
        itemCombo.removeAllItems();
        items.get(category).forEach((k, v) -> itemCombo.addItem(k));
    }

    private void addItemToCart() {
        String category = (String) categoryCombo.getSelectedItem();
        String item = (String) itemCombo.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Please select an item!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double price = items.get(category).get(item);
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double total = price * quantity;

        cart.add(new CartItem(item, price, quantity, total));
        cartModel.addRow(new Object[]{item, price, quantity, total});
        quantityField.setText("");

        updateBillPreview();
    }

    private void updateBillPreview() {
        StringBuilder billText = new StringBuilder();
        double grandTotal = 0;
        for (CartItem item : cart) {
            billText.append(item.name).append(" x").append(item.quantity).append(" = Rs. ").append(item.total).append("\n");
            grandTotal += item.total;
        }
        double gst = grandTotal * 0.05;
        double finalAmount = grandTotal + gst;

        billText.append("-----------------------------\n");
        billText.append("Subtotal: Rs. ").append(grandTotal).append("\n");
        billText.append("GST (5%): Rs. ").append(gst).append("\n");
        billText.append("Total: Rs. ").append(finalAmount).append("\n");

        billArea.setText(billText.toString());
    }

    private void generatePDFBill() {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("Bill.pdf"));
            doc.open();

            doc.add(new Paragraph("Alee's Super Store - Final Bill"));
            doc.add(new Paragraph("----------------------------------"));
            double grandTotal = 0;
            for (CartItem item : cart) {
                doc.add(new Paragraph(item.name + " x" + item.quantity + " = Rs. " + item.total));
                grandTotal += item.total;
            }
            double gst = grandTotal * 0.05;
            double finalAmount = grandTotal + gst;
            doc.add(new Paragraph("----------------------------------"));
            doc.add(new Paragraph("Subtotal: Rs. " + grandTotal));
            doc.add(new Paragraph("GST (5%): Rs. " + gst));
            doc.add(new Paragraph("Total: Rs. " + finalAmount));
            doc.close();

            saveOrderToDatabase(); // Save order in database

            JOptionPane.showMessageDialog(this, "Bill generated and saved as Bill.pdf", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating bill!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveOrderToDatabase() {
        DatabaseManager.saveOrder(cart); // Now using DatabaseManager to save order
    }

    class CartItem {
        String name;
        double price;
        int quantity;
        double total;

        CartItem(String name, double price, int quantity, double total) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.total = total;
        }
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL Driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new SuperStoreBillingApp().setVisible(true));
    }
}
