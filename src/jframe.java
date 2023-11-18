import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class jframe extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public jframe() {
        setTitle("Login Page");
        setSize(300, 150);

        setLayout(new FlowLayout());

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout());
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new FlowLayout());
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        JButton button = new JButton("Login");

        add(usernamePanel);
        add(passwordPanel);
        add(button);
        JButton button2 = new JButton("Sign Up");
        add(button2);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new SignUpPage();
                });
            }
        });

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                char[] enteredPasswordChars = passwordField.getPassword();
                String enteredPassword = new String(enteredPasswordChars);

                if (isValidLogin(enteredUsername, enteredPassword)) {

                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        new WelcomePage(enteredUsername);
                    });
                } else {
                    // Invalid login, show an error dialog
                    JOptionPane.showMessageDialog(jframe.this, "Username or password is incorrect.", "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);

        setVisible(true);
    }

    private boolean containsUppercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidLogin(String enteredUsername, String enteredPassword) {
        String url = "jdbc:mysql://localhost:3306/bus"; // Change the URL to your database
        String user = "root"; // Replace with your MySQL username
        String pass = "mypassword"; // Replace with your MySQL password

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String selectQuery = "SELECT name, password FROM users WHERE name = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, enteredUsername);
            preparedStatement.setString(2, enteredPassword);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // If a row is returned, the login is valid
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Login is invalid
    }

    public static void main(String s[]) {

        new jframe();

    }
}

class SignUpPage extends JFrame {

    private JTextField usernameField;

    private JPasswordField passwordField;

    public SignUpPage() {
        setTitle("Sign Up");
        setSize(300, 150);

        setLayout(new FlowLayout());

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout());
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new FlowLayout());
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        JButton signUpButton = new JButton("Sign Up");

        add(usernamePanel);
        add(passwordPanel);
        add(signUpButton);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (isValidInput(username, password)) {
                    // Save user details to a text file
                    saveUserDetails(username, password);

                    JOptionPane.showMessageDialog(SignUpPage.this, "Sign up successful!", "Sign Up Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        new WelcomePage(username);
                    });
                } else {
                    JOptionPane.showMessageDialog(SignUpPage.this,
                            "Password should contain 8 digits and 1 capital letter.",
                            "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    private boolean isValidInput(String username, String password) {
        return !username.isEmpty() && password.length() >= 8 && containsUppercase(password);
    }

    private boolean containsUppercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private void saveUserDetails(String username, String password) {
        // Instead of saving to a text file, we will use a SQL database
        String url = "jdbc:mysql://localhost:3306/bus"; // Change the URL to your database
        String user = "root"; // Replace with your MySQL username
        String pass = "mypassword"; // Replace with your MySQL password

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String insertQuery = "INSERT INTO users (name, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SignUpPage();
        });
    }
}

class HomePage extends JFrame {

    public HomePage(String username) {
        setTitle("Home Page");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a welcome label with the username
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        // Create buttons for various actions
        JButton button1 = new JButton("Book A Bus");
        JButton button2 = new JButton("Book Food for Bus");
        JButton button3 = new JButton("Book Cab After Bus");
        JButton logoutButton = new JButton("Logout");

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(button1);
        buttonPanel.add(button2);

        buttonPanel.add(logoutButton);

        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new jframe();
                });
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new BookingPage(username);
                });
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new BookingPage(username);
                });
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class WelcomePage extends JFrame {

    public WelcomePage(String username) {
        setTitle("Welcome Page");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton okButton = new JButton("OK");
        JButton bookingButton = new JButton("Book Bus"); // New button for booking

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(bookingButton); // Add the booking button

        add(welcomeLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new HomePage(username);
                });
            }
        });

        bookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new BookingPage(username);
                });
            }
        });

        setVisible(true);
    }
}

class BookingPage extends JFrame {

    public BookingPage(String username) {
        setTitle("Booking Page");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Book a Bus");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Create a JComboBox for date selection
        String[] dates = new String[31];
        for (int i = 1; i <= 31; i++) {
            dates[i - 1] = String.valueOf(i);
        }
        JComboBox<String> dateComboBox = new JComboBox<>(dates);

        // Create a JComboBox for month selection
        String[] months = { "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December" };
        JComboBox<String> monthComboBox = new JComboBox<>(months);

        // Create a panel to hold the date and month selection
        JPanel dateMonthPanel = new JPanel();
        dateMonthPanel.setLayout(new FlowLayout());
        dateMonthPanel.add(new JLabel("Select Date:"));
        dateMonthPanel.add(dateComboBox);
        dateMonthPanel.add(new JLabel("Select Month:"));
        dateMonthPanel.add(monthComboBox);

        JButton button1 = new JButton("Chennai to Coimbatore");
        JButton button2 = new JButton("Hyderbad to Chennai");
        JButton button3 = new JButton("Hyderbad to Coimbatore");
        JButton button4 = new JButton("Chennai to Hyderbad");

        // Create a panel to hold the buttons
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel2.add(button1);
        buttonPanel2.add(button2);
        buttonPanel2.add(button3);
        buttonPanel2.add(button4);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                SwingUtilities.invokeLater(() -> {
                    new PaymentPage(username, 1500);
                });
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                SwingUtilities.invokeLater(() -> {
                    new PaymentPage(username, 2000);
                });
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                SwingUtilities.invokeLater(() -> {
                    new PaymentPage(username, 2500);
                });
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                SwingUtilities.invokeLater(() -> {
                    new PaymentPage(username, 2200);
                });
            }
        });

        setLayout(new BorderLayout());

        JButton backButton = new JButton("Back to Home");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(backButton);

        add(titleLabel, BorderLayout.CENTER);
        add(dateMonthPanel, BorderLayout.NORTH); // Add date and month selection above buttons
        add(buttonPanel2, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new HomePage(username);
                });
            }
        });

        setVisible(true);
    }

}

class PaymentPage extends JFrame {

    private double paymentAmount;
    private JLabel paymentLabel;
    private JTextField couponCodeField;
    private JTextField bargainField; // One-line field for user input
    private JButton bargainButton; // Button for bargain
    private boolean foodAdded = false;
    private boolean hasUsedBargain = false; // Track if the user has used the bargain

    public PaymentPage(String username, double initialPaymentAmount) {
        this.paymentAmount = initialPaymentAmount;

        setTitle("Payment Page");
        setSize(450, 300); // Increased height to accommodate components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        paymentLabel = new JLabel("Payment Amount: Rs." + paymentAmount);
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        paymentLabel.setHorizontalAlignment(JLabel.CENTER);
        JButton addFoodButton = new JButton("Add Food");
        JButton payButton = new JButton("Pay");

        JButton applyCouponButton = new JButton("Apply Coupon");
        bargainButton = new JButton("Bargain"); // New button for bargain
        JButton backButton = new JButton("Back to Home"); // Back to Home button

        couponCodeField = new JTextField(15);
        bargainField = new JTextField(15); // One-line field for user input

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Vertical layout

        mainPanel.add(paymentLabel);
        mainPanel.add(Box.createVerticalStrut(10)); // Vertical spacing
        mainPanel.add(centerAlignComponent(couponCodeField));
        mainPanel.add(centerAlignComponent(applyCouponButton));
        mainPanel.add(Box.createVerticalStrut(10)); // Vertical spacing
        mainPanel.add(centerAlignComponent(bargainField));
        mainPanel.add(centerAlignComponent(bargainButton));
        mainPanel.add(centerAlignComponent(addFoodButton));
        mainPanel.add(centerAlignComponent(payButton));

        mainPanel.add(centerAlignComponent(backButton));

        add(mainPanel);

        setLocationRelativeTo(null);

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Process payment (implement your payment processing logic here)

                JOptionPane.showMessageDialog(PaymentPage.this, "Payment successful!", "Payment Success",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
                SwingUtilities.invokeLater(() -> {
                    new WelcomePage(username);
                });
            }
        });

        addFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!foodAdded) {
                    // Increase the payment amount by 300 when food is added
                    paymentAmount += 300;
                    foodAdded = true;
                    updatePaymentLabel();
                }
            }
        });

        applyCouponButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String couponCode = couponCodeField.getText();

                if (couponCode.equalsIgnoreCase("DISCOUNT")) {
                    paymentAmount -= 50;
                    updatePaymentLabel();
                }
            }
        });

        bargainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!hasUsedBargain) {
                    String bargainInput = bargainField.getText();
                    try {
                        double bargainValue = Double.parseDouble(bargainInput);
                        // Check if the bargain is less than 20%
                        if (bargainValue < 20) {
                            // Reduce the payment amount by 20%
                            paymentAmount *= 0.8;
                            hasUsedBargain = true; // Set the flag to prevent further use
                            updatePaymentLabel();
                            bargainButton.setEnabled(false); // Disable the bargain button
                        }
                    } catch (NumberFormatException ex) {
                        // Handle invalid input (non-numeric or other errors)
                        JOptionPane.showMessageDialog(PaymentPage.this, "Invalid bargain value.", "Bargain Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(PaymentPage.this, "Bargain can only be used once.", "Bargain Limit",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new WelcomePage(username);
                });
            }
        });

        setVisible(true);
    }

    private void updatePaymentLabel() {
        paymentLabel.setText("Payment Amount: Rs." + paymentAmount);
    }

    private JPanel centerAlignComponent(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(component);
        return panel;
    }

}
