import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Login implements ActionListener {

    // Variable.
    String[] loginAsName = {"Manager", "Cashier", "Data Entry Operator"};
    String managerUserName = "admin";
    String managerPassword = "1234";

    // Frame.
    JFrame loginFrame = new JFrame();

    // Panel.
    JPanel loginAsPanel = new JPanel();
    JPanel loginDetailsPanel = new JPanel();
    JPanel massagePanel = new JPanel();

    // Label.
    JLabel loginAsLabel = new JLabel();
    JLabel userNameLabel = new JLabel();
    JLabel passwordLabel = new JLabel();
    JLabel massageLabel = new JLabel();

    // ComboBox.
    JComboBox loginAsComboBox = new JComboBox(loginAsName);

    // TextField.
    JTextField userNameTextField = new JTextField();
    JTextField passwordTextField = new JTextField();

    // Button.
    JButton loginButton = new JButton();

    public Login() {

        // Frame.
        loginFrame.setVisible(true);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(500, 400);
        loginFrame.setTitle("Login");
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setLayout(null);
        loginFrame.add(loginAsPanel);
        loginFrame.add(loginDetailsPanel);
        loginFrame.add(massagePanel);

        // Panel.
        loginAsPanel.setLayout(null);
        loginAsPanel.setBounds(0, 0, 250, 150);
        loginAsPanel.add(loginAsLabel);
        loginAsPanel.add(loginAsComboBox);

        loginDetailsPanel.setLayout(null);
        loginDetailsPanel.setBounds(250, 0, 250, 150);
        loginDetailsPanel.add(userNameLabel);
        loginDetailsPanel.add(passwordLabel);
        loginDetailsPanel.add(userNameTextField);
        loginDetailsPanel.add(passwordTextField);
        loginDetailsPanel.add(loginButton);

        massagePanel.setLayout(null);
        massagePanel.setBounds(0, 150, 500, 250);
        massagePanel.add(massageLabel);

        // Label.
        loginAsLabel.setLayout(null);
        loginAsLabel.setBounds(30, 0, 250, 30);
        loginAsLabel.setText("Login As: ");

        userNameLabel.setLayout(null);
        userNameLabel.setBounds(0, 0, 100, 30);
        userNameLabel.setText("User Name: ");

        passwordLabel.setLayout(null);
        passwordLabel.setBounds(0, 30, 100, 30);
        passwordLabel.setText("Password: ");

        massageLabel.setLayout(null);
        massageLabel.setBounds(30, 0, 500, 30);

        // ComboBox.
        loginAsComboBox.setBounds(25, 0, 175, 100);
        loginAsComboBox.addActionListener(this);
        loginAsComboBox.setSelectedIndex(0);

        // TextField.
        userNameTextField.setLayout(null);
        userNameTextField.setBounds(80, 0, 150, 30);

        passwordTextField.setLayout(null);
        passwordTextField.setBounds(80, 30, 150, 30);

        // Button.
        loginButton.setLayout(null);
        loginButton.setBounds(80, 70, 100, 30);
        loginButton.setText("Login");
        loginButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {

            String loginAs = (String) loginAsComboBox.getSelectedItem();

            // Manager Login.
            if (Objects.equals(loginAs, "Manager") &&
                    Objects.equals(userNameTextField.getText(), managerUserName) &&
                    Objects.equals(passwordTextField.getText(), managerPassword))
            {
                massageLabel.setText("Login Successful.");

                loginFrame.dispose();
                new Manager();
            }

            // Cashier Login.
            else if (Objects.equals(loginAs, "Cashier") &&
                    login("cashierDB.txt")) {

                massageLabel.setText("Login Successful.");

                loginFrame.dispose();
                new Cashier();
            }

            // Data Entry Operator.
            else if (Objects.equals(loginAs, "Data Entry Operator") &&
                    login("dataEntryOperatorDB.txt")) {

                massageLabel.setText("Login Successful.");

                loginFrame.dispose();
                new DataEntryOperator();
            }

            else
                massageLabel.setText("User Name or Password Wrong!");

            userNameTextField.setText("");
            passwordTextField.setText("");

        }
    }


    // Login.
    public boolean login(String fileName) {
        boolean flag = false;

        try {

            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String userName = scanner.next();
                String password = scanner.next();
                scanner.nextLine();

                if (Objects.equals(userName, userNameTextField.getText()) &&
                        Objects.equals(password, passwordTextField.getText())) {

                    flag = true;
                    break;
                }
            }

            scanner.close();

        }
        catch (IOException e) {
            System.out.println(e);
        }

        return flag;
    }
}
