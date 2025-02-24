import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Manager implements ActionListener {

    // Variable.
    String[] account = {"Cashier", "Data Entry Operator"};

    // Frame.
    JFrame managerFrame = new JFrame();

    // Panel.
    JPanel createPanel = new JPanel();
    JPanel deletePanel = new JPanel();
    JPanel massagePanel = new JPanel();

    // Label.
    JLabel createAccountLabel = new JLabel();
    JLabel deleteAccountLabel = new JLabel();
    JLabel createUserNameLabel = new JLabel();
    JLabel createPasswordLabel = new JLabel();
    JLabel deleteUserNameLabel = new JLabel();
    JLabel deletePasswordLabel = new JLabel();
    JLabel massageLabel = new JLabel();

    // ComboBox.
    JComboBox createAccountComboBox = new JComboBox(account);
    JComboBox deleteAccountComboBox = new JComboBox(account);

    // TextField.
    JTextField createUserNameTextField = new JTextField();
    JTextField createPasswordTextField = new JTextField();
    JTextField deleteUserNameTextField = new JTextField();
    JTextField deletePasswordTextField = new JTextField();

    // Button.
    JButton createButton = new JButton();
    JButton deleteButton = new JButton();
    JButton logoutButton = new JButton();

    public Manager() {

        // Frame.
        managerFrame.setTitle("Manager");
        managerFrame.setSize(500, 400);
        managerFrame.setResizable(false);
        managerFrame.setLocationRelativeTo(null);
        managerFrame.setLayout(null);
        managerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerFrame.setVisible(true);
        managerFrame.add(createPanel);
        managerFrame.add(deletePanel);
        managerFrame.add(massagePanel);

        // Panel.
        createPanel.setLayout(null);
        createPanel.setBounds(0, 0, 250, 150);
        createPanel.add(createAccountLabel);
        createPanel.add(createAccountComboBox);
        createPanel.add(createUserNameLabel);
        createPanel.add(createPasswordLabel);
        createPanel.add(createUserNameTextField);
        createPanel.add(createPasswordTextField);
        createPanel.add(createButton);

        deletePanel.setLayout(null);
        deletePanel.setBounds(250, 0, 250, 150);
        deletePanel.add(deleteAccountLabel);
        deletePanel.add(deleteAccountComboBox);
        deletePanel.add(deleteUserNameLabel);
        deletePanel.add(deletePasswordLabel);
        deletePanel.add(deleteUserNameTextField);
        deletePanel.add(deletePasswordTextField);
        deletePanel.add(deleteButton);

        massagePanel.setLayout(null);
        massagePanel.setBounds(0, 150, 500, 250);
        massagePanel.add(massageLabel);
        massagePanel.add(logoutButton);

        // Label.
        createAccountLabel.setLayout(null);
        createAccountLabel.setBounds(30, 0, 250, 30);
        createAccountLabel.setText("Create Account: ");

        deleteAccountLabel.setLayout(null);
        deleteAccountLabel.setBounds(30, 0, 250, 30);
        deleteAccountLabel.setText("Delete Account: ");

        createUserNameLabel.setLayout(null);
        createUserNameLabel.setBounds(30, 60, 100, 30);
        createUserNameLabel.setText("User Name: ");

        createPasswordLabel.setLayout(null);
        createPasswordLabel.setBounds(30, 90, 100, 30);
        createPasswordLabel.setText("Password: ");

        deleteUserNameLabel.setLayout(null);
        deleteUserNameLabel.setBounds(30, 60, 100, 30);
        deleteUserNameLabel.setText("User Name: ");

        deletePasswordLabel.setLayout(null);
        deletePasswordLabel.setBounds(30, 90, 100, 30);
        deletePasswordLabel.setText("Password: ");

        massageLabel.setLayout(null);
        massageLabel.setBounds(30, 10, 500, 30);

        // ComboBox.
        createAccountComboBox.setBounds(25, 30, 175, 30);
        createAccountComboBox.addActionListener(this);
        createAccountComboBox.setSelectedIndex(0);

        deleteAccountComboBox.setBounds(25, 30, 175, 30);
        deleteAccountComboBox.addActionListener(this);
        deleteAccountComboBox.setSelectedIndex(0);

        // TextField.
        createUserNameTextField.setLayout(null);
        createUserNameTextField.setBounds(105, 60, 130, 30);

        createPasswordTextField.setLayout(null);
        createPasswordTextField.setBounds(105, 90, 130, 30);

        deleteUserNameTextField.setLayout(null);
        deleteUserNameTextField.setBounds(105, 60, 130, 30);

        deletePasswordTextField.setLayout(null);
        deletePasswordTextField.setBounds(105, 90, 130, 30);

        // Button.
        createButton.setLayout(null);
        createButton.setBounds(100, 120, 100, 30);
        createButton.setText("Create");
        createButton.addActionListener(this);

        deleteButton.setLayout(null);
        deleteButton.setBounds(100, 120, 100, 30);
        deleteButton.setText("Delete");
        deleteButton.addActionListener(this);

        logoutButton.setLayout(null);
        logoutButton.setBounds(200, 180, 100, 30);
        logoutButton.setText("Logout");
        logoutButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Create Panel.
        if (e.getSource() == createButton) {

            String createAccount = (String) createAccountComboBox.getSelectedItem();

            if (createAccount == "Cashier") {
                createAccount("cashierDB.txt");

                massageLabel.setText("\"Cashier\" Account Created Successfully.");
            }
            if (createAccount == "Data Entry Operator") {
                createAccount("dataEntryOperatorDB.txt");

                massageLabel.setText("\"Data Entry Operator\" Account Created Successfully.");
            }

            createUserNameTextField.setText("");
            createPasswordTextField.setText("");

        }

        // Delete Panel.
        if (e.getSource() == deleteButton) {

            String deleteAccount = (String) deleteAccountComboBox.getSelectedItem();

            if (deleteAccount == "Cashier") {
                if (deleteAccount("cashierDB.txt"))
                    massageLabel.setText("\"Cashier\" Account Deleted Successfully.");
                else
                    massageLabel.setText("User Name or Password Wrong!");
            }
            if (deleteAccount == "Data Entry Operator") {
                if (deleteAccount("dataEntryOperatorDB.txt"))
                    massageLabel.setText("\"Data Entry Operator\" Account Deleted Successfully.");
                else
                    massageLabel.setText("User Name or Password Wrong!");
            }

            deleteUserNameTextField.setText("");
            deletePasswordTextField.setText("");
        }

        // Logout.
        if (e.getSource() == logoutButton) {
            managerFrame.dispose();
            new Login();
        }

    }


    // Create Account.
    public void createAccount(String fileName) {

        try{

            FileWriter fileWriter = new FileWriter(fileName, true);

            fileWriter.write(createUserNameTextField.getText() + " " + createPasswordTextField.getText() + "\n");

            fileWriter.close();

        }
        catch (IOException e){
            System.out.println(e);
        }

    }


    // Delete Account.
    public boolean deleteAccount(String fileName) {

        boolean flag = false;

        try{

            File file1 = new File(fileName);
            File file2 = new File("tem.txt");

            FileWriter fileWriter = new FileWriter(file2);
            Scanner scanner = new Scanner(file1);

            while (scanner.hasNextLine()) {
                String userNameFile = scanner.next();
                String passwordFile = scanner.next();
                scanner.nextLine();

                String userNameTextField = deleteUserNameTextField.getText();
                String passwordTextField = deletePasswordTextField.getText();

                if (!Objects.equals(userNameFile, userNameTextField) ||
                        !Objects.equals(passwordFile, passwordTextField)) {

                    fileWriter.write(userNameFile + " " + passwordFile + "\n");
                }
                else
                    flag = true;
            }

            fileWriter.close();
            scanner.close();

            file1.delete();
            file2.renameTo(new File(fileName));


        }
        catch (IOException e){
            System.out.println(e);
        }

        return flag;

    }
}

