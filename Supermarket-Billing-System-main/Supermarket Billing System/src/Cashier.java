import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Cashier implements ActionListener {

    // Variable.
    int count = 0;
    int total = 0;

    // Frame.
    JFrame cashierFrame = new JFrame();

    // Panel.
    JPanel cardPanel = new JPanel();
    JPanel customerDetailsPanel = new JPanel();
    JPanel massagePanel = new JPanel();

    // Label.
    JLabel productIdLabel = new JLabel();
    JLabel productQuantityLabel = new JLabel();
    JLabel customerNameLabel = new JLabel();
    JLabel customerNumberLabel = new JLabel();
    JLabel massageLabel = new JLabel();
    JLabel totalLabel = new JLabel();
    JLabel countLabel = new JLabel();

    // TextField.
    JTextField productIdTextField = new JTextField();
    JTextField productQuantityTextField = new JTextField();
    JTextField customerNameTextField = new JTextField();
    JTextField customerNumberTextField = new JTextField();

    // Button.
    JButton addButton = new JButton();
    JButton billPrintButton = new JButton();
    JButton logoutButton = new JButton();

    public Cashier() {

        // Frame.
        cashierFrame.setTitle("Cashier");
        cashierFrame.setSize(500, 400);
        cashierFrame.setResizable(false);
        cashierFrame.setLocationRelativeTo(null);
        cashierFrame.setLayout(null);
        cashierFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cashierFrame.setVisible(true);
        cashierFrame.add(cardPanel);
        cashierFrame.add(customerDetailsPanel);
        cashierFrame.add(massagePanel);

        // Panel.
        cardPanel.setLayout(null);
        cardPanel.setBounds(0, 0, 250, 150);
        cardPanel.add(productIdLabel);
        cardPanel.add(productQuantityLabel);
        cardPanel.add(productIdTextField);
        cardPanel.add(productQuantityTextField);
        cardPanel.add(addButton);

        customerDetailsPanel.setLayout(null);
        customerDetailsPanel.setBounds(250, 0, 250, 150);
        customerDetailsPanel.add(customerNameLabel);
        customerDetailsPanel.add(customerNumberLabel);
        customerDetailsPanel.add(customerNameTextField);
        customerDetailsPanel.add(customerNumberTextField);
        customerDetailsPanel.add(billPrintButton);

        massagePanel.setLayout(null);
        massagePanel.setBounds(0, 150, 500, 250);
        massagePanel.add(massageLabel);
        massagePanel.add(totalLabel);
        massagePanel.add(countLabel);
        massagePanel.add(logoutButton);

        // Label.
        productIdLabel.setLayout(null);
        productIdLabel.setBounds(30, 0, 250, 30);
        productIdLabel.setText("ID: ");

        productQuantityLabel.setLayout(null);
        productQuantityLabel.setBounds(30, 30, 250, 30);
        productQuantityLabel.setText("Quantity: ");

        customerNameLabel.setLayout(null);
        customerNameLabel.setBounds(30, 0, 250, 30);
        customerNameLabel.setText("Name: ");

        customerNumberLabel.setLayout(null);
        customerNumberLabel.setBounds(30, 30, 250, 30);
        customerNumberLabel.setText("Phone No: ");

        countLabel.setLayout(null);
        countLabel.setBounds(30, 0, 500, 30);
        countLabel.setText(count + " Product Added");

        totalLabel.setLayout(null);
        totalLabel.setBounds(30, 30, 500, 30);
        totalLabel.setText("Total: " + total);

        massageLabel.setLayout(null);
        massageLabel.setBounds(30, 60, 500, 30);

        // TextField.
        productIdTextField.setLayout(null);
        productIdTextField.setBounds(90, 0, 150, 30);

        productQuantityTextField.setLayout(null);
        productQuantityTextField.setBounds(90, 30, 150, 30);

        customerNameTextField.setLayout(null);
        customerNameTextField.setBounds(100, 0, 140, 30);

        customerNumberTextField.setLayout(null);
        customerNumberTextField.setBounds(100, 30, 140, 30);

        // Button.
        addButton.setLayout(null);
        addButton.setBounds(90, 65, 100, 30);
        addButton.setText("Add");
        addButton.addActionListener(this);

        billPrintButton.setLayout(null);
        billPrintButton.setBounds(100, 65, 100, 30);
        billPrintButton.setText("Bill Print");
        billPrintButton.addActionListener(this);

        logoutButton.setLayout(null);
        logoutButton.setBounds(200, 180, 100, 30);
        logoutButton.setText("Logout");
        logoutButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addButton) {
            if (add()) {
                countLabel.setText(count + " Product Added");
                totalLabel.setText("Total: " + total);
                massageLabel.setText("Product Added Successfully.");
            }
            else
                massageLabel.setText("Product Not found!");

            productIdTextField.setText("");
            productQuantityTextField.setText("");
        }

        if (e.getSource() == billPrintButton) {
            billPrint();

            count = 0;
            total = 0;

            countLabel.setText(count + " Product Added");
            totalLabel.setText("Total: " + total);
            massageLabel.setText("Bill Print Successfully");

            customerNameTextField.setText("");
            customerNumberTextField.setText("");
        }

        if (e.getSource() == logoutButton) {
            cashierFrame.dispose();
            new Login();
        }

    }


    // Add Function.
    public boolean add() {

        boolean flag = false;
        int id = Integer.parseInt(productIdTextField.getText());
        int quantity = Integer.parseInt(productQuantityTextField.getText());

        try {

            Scanner scanner = new Scanner(new File("productList.txt"));

            while (scanner.hasNextLine()) {
                int idF = scanner.nextInt();
                scanner.next();
                scanner.next();
                int price = scanner.nextInt();
                scanner.nextLine();

                if (id == idF) {
                    total += price * quantity;
                    count++;

                    flag = true;
                    break;
                }
            }

        }
        catch (IOException e) {
            System.out.println(e);
        }

        return flag;

    }


    // Bill Print Button.
    public void billPrint() {

        String customerName = customerNameTextField.getText();
        String customerNumber = customerNumberTextField.getText();

        try {

            FileWriter fileWriter = new FileWriter(customerName + ".txt");

            fileWriter.write("Name: " + customerName + "\n");
            fileWriter.write("Phone No: " + customerNumber + "\n");
            fileWriter.write("\n\n\n");
            fileWriter.write("Total Bill: " + total + "\n");
            fileWriter.write("\n\n\n");
            fileWriter.write("Thank You for Shopping.\n");

            fileWriter.close();

        }
        catch (IOException e) {
            System.out.println(e);
        }

    }
}
