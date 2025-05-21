package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.UserDAO;
import model.User;

public class LoginFrm extends JFrame implements ActionListener {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrm() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Title
        JLabel lblTitle = new JLabel("Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;

        JLabel lblUsername = new JLabel("Username");
        txtUsername = new JTextField(15);
        JLabel lblPassword = new JLabel("Password");
        txtPassword = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Login button
        btnLogin = new JButton("Login");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(this);
        mainPanel.add(btnLogin);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            UserDAO userDAO = new UserDAO();
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            if (userDAO.checkLogin(user)) {
                {
                    if (user.getRole().equalsIgnoreCase("Ticket seller")) {
                        new TicketingFrm(user).setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Not allowed.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrm().setVisible(true);
        });
    }
} 