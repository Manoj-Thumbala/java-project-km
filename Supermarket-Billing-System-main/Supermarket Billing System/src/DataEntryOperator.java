import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class DataEntryOperator implements ActionListener {

    // Frame.
    JFrame dataEntryOperatorFrame = new JFrame();

    // Panel.
    JPanel addProductPanel = new JPanel();
    JPanel removeProductPanel = new JPanel();
    JPanel massagePanel = new JPanel();

    // Label.
    JLabel addProductLabel = new JLabel();
    JLabel idLabel = new JLabel();
    JLabel nameLabel = new JLabel();
    JLabel quantityLabel = new JLabel();
    JLabel priceLabel = new JLabel();
    JLabel massageLabel = new JLabel();
    JLabel removeProductLabel = new JLabel();
    JLabel removeIDLabel = new JLabel();

    // TextField.
    JTextField idTextField = new JTextField();
    JTextField nameTextField = new JTextField();
    JTextField quantityTextField = new JTextField();
    JTextField priceTextField = new JTextField();
    JTextField removeIDTextField = new JTextField();

    // Button.
    JButton addButton = new JButton();
    JButton removeButton = new JButton();
    JButton productListButton = new JButton();
    JButton logoutButton = new JButton();

    public DataEntryOperator() {

        // Frame.
        dataEntryOperatorFrame.setTitle("Data Entry Operator");
        dataEntryOperatorFrame.setSize(500, 400);
        dataEntryOperatorFrame.setResizable(false);
        dataEntryOperatorFrame.setLocationRelativeTo(null);
        dataEntryOperatorFrame.setLayout(null);
        dataEntryOperatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dataEntryOperatorFrame.setVisible(true);
        dataEntryOperatorFrame.add(addProductPanel);
        dataEntryOperatorFrame.add(removeProductPanel);
        dataEntryOperatorFrame.add(massagePanel);

        // Panel.
        addProductPanel.setLayout(null);
        addProductPanel.setBounds(0, 0, 250, 300);
        addProductPanel.add(addProductLabel);
        addProductPanel.add(idLabel);
        addProductPanel.add(nameLabel);
        addProductPanel.add(quantityLabel);
        addProductPanel.add(priceLabel);
        addProductPanel.add(idTextField);
        addProductPanel.add(nameTextField);
        addProductPanel.add(quantityTextField);
        addProductPanel.add(priceTextField);
        addProductPanel.add(addButton);

        removeProductPanel.setLayout(null);
        removeProductPanel.setBounds(250, 0, 250, 300);
        removeProductPanel.add(removeProductLabel);
        removeProductPanel.add(removeIDLabel);
        removeProductPanel.add(removeIDTextField);
        removeProductPanel.add(removeButton);
        removeProductPanel.add(productListButton);

        massagePanel.setLayout(null);
        massagePanel.setBounds(0, 300, 500, 100);
        massagePanel.add(massageLabel);
        massagePanel.add(logoutButton);

        // Label.
        addProductLabel.setLayout(null);
        addProductLabel.setBounds(30, 0, 250, 30);
        addProductLabel.setText("Add Product: ");

        idLabel.setLayout(null);
        idLabel.setBounds(30, 30, 250, 30);
        idLabel.setText("Id: ");

        nameLabel.setLayout(null);
        nameLabel.setBounds(30, 60, 250, 30);
        nameLabel.setText("Name: ");

        quantityLabel.setLayout(null);
        quantityLabel.setBounds(30, 90, 250, 30);
        quantityLabel.setText("Quantity: ");

        priceLabel.setLayout(null);
        priceLabel.setBounds(30, 120, 250, 30);
        priceLabel.setText("Price: ");

        massageLabel.setLayout(null);
        massageLabel.setBounds(30, 0, 500, 30);

        removeProductLabel.setLayout(null);
        removeProductLabel.setBounds(30, 0, 250, 30);
        removeProductLabel.setText("Remove Product: ");

        removeIDLabel.setLayout(null);
        removeIDLabel.setBounds(30, 30, 250, 30);
        removeIDLabel.setText("Id: ");

        // TextField.
        idTextField.setLayout(null);
        idTextField.setBounds(90, 30, 150, 30);

        nameTextField.setLayout(null);
        nameTextField.setBounds(90, 60, 150, 30);

        quantityTextField.setLayout(null);
        quantityTextField.setBounds(90, 90, 150, 30);

        priceTextField.setLayout(null);
        priceTextField.setBounds(90, 120, 150, 30);

        removeIDTextField.setLayout(null);
        removeIDTextField.setBounds(90, 30, 150, 30);

        // Button.
        addButton.setLayout(null);
        addButton.setBounds(90, 160, 100, 30);
        addButton.setText("Add");
        addButton.addActionListener(this);

        removeButton.setLayout(null);
        removeButton.setBounds(90, 70, 100, 30);
        removeButton.setText("Remove");
        removeButton.addActionListener(this);

        productListButton.setLayout(null);
        productListButton.setBounds(90, 160, 100, 30);
        productListButton.setText("Product List");
        productListButton.addActionListener(this);

        logoutButton.setLayout(null);
        logoutButton.setBounds(200, 30, 100, 30);
        logoutButton.setText("Logout");
        logoutButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addButton) {
            addProduct();

            idTextField.setText("");
            nameTextField.setText("");
            quantityTextField.setText("");
            priceTextField.setText("");

            massageLabel.setText("Product Added Successfully");
        }

        if (e.getSource() == removeButton) {
            if (removeProduct())
                massageLabel.setText("Product Removed Successfully");
            else
                massageLabel.setText("Product Not Found");

            removeIDTextField.setText("");
        }

        if (e.getSource() == productListButton) {
            String line = productList();

            JOptionPane.showMessageDialog(null, line, "Product List", JOptionPane.PLAIN_MESSAGE);
        }

        if (e.getSource() == logoutButton) {
            dataEntryOperatorFrame.dispose();
            new Login();
        }

    }


    // Add Product function.
    public void addProduct() {

        try {

            File file = new File("productList.txt");
            FileWriter fileWriter = new FileWriter(file, true);

            fileWriter.write(idTextField.getText() + " " +
                    nameTextField.getText() + " " +
                    quantityTextField.getText() + " " +
                    priceTextField.getText() + "\n");

            fileWriter.close();

        }
        catch (IOException e) {
            System.out.println(e);
        }

    }


    // Remove Product Function.
    public boolean removeProduct() {

        boolean flag = false;

        try {

            File file1 = new File("productList.txt");
            File file2 = new File("tem.txt");

            FileWriter fileWriter = new FileWriter(file2);
            Scanner scanner = new Scanner(file1);

            while (scanner.hasNextLine()) {
                String idFile = scanner.next();
                String lineFile = scanner.nextLine();

                String idTextField = removeIDTextField.getText();

                if (!Objects.equals(idFile, idTextField))
                    fileWriter.write(idFile + lineFile + "\n");
                else
                    flag = true;
            }

            fileWriter.close();
            scanner.close();

            file1.delete();
            file2.renameTo(new File("productList.txt"));

        }
        catch (IOException e) {
            System.out.println(e);
        }

        return flag;

    }


    // Product List Function.
    public String productList() {

        String line = "";

        try {

            File file = new File("productList.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                line += scanner.nextLine() + "\n";
            }

        }
        catch (IOException e) {
            System.out.println(e);
        }

        return line;

    }
}
