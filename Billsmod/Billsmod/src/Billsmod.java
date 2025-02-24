import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

public class Billsmod {
    private static final String url = "jdbc:mysql://localhost:3306/billingDB";
    private static final String user = "root";
    private static final String password = "manoj@123";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showSplashScreen());
    }

    private static void showSplashScreen() {
        JWindow splashScreen = new JWindow();
        splashScreen.setSize(800, 600);
        splashScreen.setLayout(new BorderLayout());

        ImageIcon backgroundIcon = new ImageIcon("C:\\BillingSystem\\images\\splash_background.jpg");
        Image backgroundImage = backgroundIcon.getImage();
        BackgroundPanel splashPanel = new BackgroundPanel(backgroundImage);
        splashPanel.setLayout(new GridBagLayout());
        splashScreen.add(splashPanel, BorderLayout.CENTER);

        JLabel splashLabel = new JLabel("", JLabel.CENTER);
        splashLabel.setFont(new Font("Serif", Font.BOLD, 50));
        splashLabel.setForeground(Color.WHITE);
        splashPanel.add(splashLabel);

        splashScreen.setVisible(true);

        Timer animationTimer = new Timer(100, new ActionListener() {
            private int charIndex = 0;
            private String title = "KM SUPERMARKET";

            @Override
            public void actionPerformed(ActionEvent e) {
                if (charIndex < title.length()) {
                    splashLabel.setText(splashLabel.getText() + title.charAt(charIndex));
                    charIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                    Timer hideTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            splashScreen.setVisible(false);
                            splashScreen.dispose();
                            showProductEntryPage();
                        }
                    });
                    hideTimer.setRepeats(false);
                    hideTimer.start();
                }
            }
        });
        animationTimer.start();
    }

    public static void showProductEntryPage() { // Changed to public
        JFrame frame = new JFrame("Billing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(800, 600));

        ImageIcon backgroundIcon = new ImageIcon("C:\\BillingSystem\\images\\product_background.jpg");
        Image backgroundImage = backgroundIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(new BorderLayout());

        BillingSystemGUI contentPane = new BillingSystemGUI(url, user, password, frame);
        contentPane.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("KM SUPERMARKET", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.YELLOW);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JLabel dateLabel = new JLabel();
        dateLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        dateLabel.setForeground(Color.CYAN);
        updateDateTime(dateLabel);
        topPanel.add(dateLabel, BorderLayout.EAST);

        Timer timer = new Timer(1000, e -> updateDateTime(dateLabel));
        timer.start();

        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPane, BorderLayout.CENTER);

        frame.add(backgroundPanel);
        frame.setVisible(true);
    }

    private static void updateDateTime(JLabel dateLabel) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateLabel.setText(formatter.format(date));
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            int width = getWidth();
            int height = getHeight();
            g.drawImage(backgroundImage, 0, 0, width, height, this);
        }
    }
}

class BillingSystemGUI extends JPanel implements ActionListener {
    private JButton updateButton, deleteButton, tallyButton, printButton, enterButton;
    private JTextField barcodeField, amountReceivedField;
    private JTextArea productDetails, billPreview;
    private String url, user, password;
    private JFrame frame;

    public BillingSystemGUI(String url, String user, String password, JFrame frame) {
        super();
        this.url = url;
        this.user = user;
        this.password = password;
        this.frame = frame;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        barcodeField = createNumericTextField(20);
        barcodeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                suggestProductName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                suggestProductName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                suggestProductName();
            }
        });

        amountReceivedField = createNumericTextField(20);

        updateButton = new JButton("Fetch Product");
        deleteButton = new JButton("Delete Product");
        tallyButton = new JButton("Tally Amount");
        printButton = new JButton("Print Bill");
        enterButton = new JButton("Enter");

        productDetails = new JTextArea(10, 30);
        productDetails.setEditable(false);
        productDetails.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productDetails.setFont(new Font("Serif", Font.PLAIN, 14));
        productDetails.setLineWrap(true);
        productDetails.setWrapStyleWord(true);

        billPreview = new JTextArea(10, 30);
        billPreview.setEditable(false);
        billPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        billPreview.setFont(new Font("Serif", Font.PLAIN, 14));
        billPreview.setLineWrap(true);
        billPreview.setWrapStyleWord(true);

        Dimension buttonSize = new Dimension(200, 40);
        updateButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        tallyButton.setPreferredSize(buttonSize);
        printButton.setPreferredSize(buttonSize);
        enterButton.setPreferredSize(buttonSize);

        updateButton.setFont(new Font("Serif", Font.BOLD, 18));
        deleteButton.setFont(new Font("Serif", Font.BOLD, 18));
        tallyButton.setFont(new Font("Serif", Font.BOLD, 18));
        printButton.setFont(new Font("Serif", Font.BOLD, 18));
        enterButton.setFont(new Font("Serif", Font.BOLD, 18));

        updateButton.setBackground(Color.BLUE);
        updateButton.setForeground(Color.WHITE);
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        tallyButton.setBackground(Color.GREEN);
        tallyButton.setForeground(Color.WHITE);
        printButton.setBackground(Color.ORANGE);
        printButton.setForeground(Color.WHITE);
        enterButton.setBackground(Color.MAGENTA);
        enterButton.setForeground(Color.WHITE);

        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        tallyButton.addActionListener(this);
        printButton.addActionListener(this);
        enterButton.addActionListener(e -> Billsmod.showProductEntryPage());

        JPanel dialogBox = new JPanel(new GridBagLayout());
        dialogBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        dialogBox.setBackground(new Color(255, 255, 255, 200));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        dialogBox.add(new JLabel("Barcode:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialogBox.add(barcodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        dialogBox.add(new JLabel("Amount Received:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialogBox.add(amountReceivedField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialogBox.add(updateButton, gbc);
        gbc.gridy = 3;
        dialogBox.add(deleteButton, gbc);
        gbc.gridy = 4;
        dialogBox.add(tallyButton, gbc);
        gbc.gridy = 5;
        dialogBox.add(enterButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        dialogBox.add(new JScrollPane(productDetails), gbc);

        add(dialogBox);
    }

    private JTextField createNumericTextField(int columns) {
        JTextField textField = new JTextField(columns);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("[0-9]+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                if (text.matches("[0-9]+")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        return textField;
    }

    private void suggestProductName() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String barcode = barcodeField.getText();
            if (barcode.isEmpty()) return;

            String query = "SELECT name FROM products WHERE barcode = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, barcode);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                productDetails.setText("Name: " + name);
            } else {
                productDetails.setText("");
            }

            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error suggesting product name: " + ex.getMessage());
        }
    }

    private void showBillSummaryPage() {
        frame.getContentPane().removeAll();
        JPanel billSummaryPanel = new JPanel(new BorderLayout());

        JTextArea billSummaryText = new JTextArea(20, 50);
        billSummaryText.setFont(new Font("Serif", Font.PLAIN, 18));
        billSummaryText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billSummaryText);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Serif", Font.BOLD, 18));
        backButton.setBackground(Color.BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> Billsmod.showProductEntryPage());

        JButton printButton = new JButton("Print");
        printButton.setFont(new Font("Serif", Font.BOLD, 18));
        printButton.setBackground(Color.ORANGE);
        printButton.setForeground(Color.WHITE);
        printButton.addActionListener(e -> printBill());

        billSummaryPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(printButton);
        billSummaryPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(billSummaryPanel);
        frame.revalidate();
        frame.repaint();

        fetchBillDetails(billSummaryText);
    }

    private void fetchBillDetails(JTextArea billSummaryText) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT name, price, tax, quantity FROM products";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            double totalAmount = 0;
            StringBuilder billDetails = new StringBuilder("KM SUPERMARKET\n\n");
            billDetails.append("Product\t\tPrice\t\tTax\t\tQuantity\tTotal\n");
            billDetails.append("------------------------------------------------------\n");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                double tax = resultSet.getDouble("tax");
                int quantity = resultSet.getInt("quantity");
                double total = (price + tax) * quantity;
                totalAmount += total;

                billDetails.append(name).append("\t\t")
                        .append(price).append("\t\t")
                        .append(tax).append("\t\t")
                        .append(quantity).append("\t\t")
                        .append(total).append("\n");
            }

            double amountReceived = Double.parseDouble(amountReceivedField.getText());
            double balance = amountReceived - totalAmount;

            billDetails.append("------------------------------------------------------\n");
            billDetails.append("Total Amount: ").append(totalAmount).append("\n");
            billDetails.append("Amount Received: ").append(amountReceived).append("\n");
            billDetails.append("Balance: ").append(balance).append("\n");

            billSummaryText.setText(billDetails.toString());

            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill details: " + ex.getMessage());
        }
    }

    private void printBill() {
        JOptionPane.showMessageDialog(this, "Printing...\n" + billPreview.getText());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            fetchProduct(barcodeField.getText());
        } else if (e.getSource() == deleteButton) {
            deleteProduct(barcodeField.getText());
        } else if (e.getSource() == tallyButton) {
            tallyAmount();
        }
    }

    private void fetchProduct(String barcode) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT name, price, quantity FROM products WHERE barcode = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, barcode);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                productDetails.setText("Name: " + name + "\nPrice: " + price + "\nQuantity: " + quantity);
            } else {
                JOptionPane.showMessageDialog(this, "Product not found.");
            }

            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching product: " + ex.getMessage());
        }
    }

    public void deleteProduct(String barcode) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String deleteSql = "DELETE FROM products WHERE barcode = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setString(1, barcode);

            int rowsDeleted = deleteStatement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product. Barcode not found.");
            }

            deleteStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage());
        }
    }

    public void tallyAmount() {
        double totalAmount = 100.00; // Replace with actual calculation logic
        double amountReceived = Double.parseDouble(amountReceivedField.getText());
        double change = amountReceived - totalAmount;

        billPreview.setText("KM SUPERMARKET\n\n");
        billPreview.append("Total Amount: " + totalAmount + "\n");
        billPreview.append("Amount Received: " + amountReceived + "\n");
        billPreview.append("Change: " + change + "\n");
    }
}
