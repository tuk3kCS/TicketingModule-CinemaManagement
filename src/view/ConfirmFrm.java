package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import model.Invoice;
import model.Ticket;
import model.OrderedFood;
import dao.TicketDAO;
import dao.InvoiceDAO;

public class ConfirmFrm extends JFrame implements ActionListener {
    private JPanel ticketListPanel;
    private JButton btnFoodService;
    private JButton btnConfirm;
    private JPanel foodListPanel;
    private JLabel lblTotalPrice;
    private JLabel lblPriceValue;
    private List<OrderedFood> foodList;
    private Invoice invoice;

    public ConfirmFrm(Invoice invoice) {
        this.invoice = invoice;
        this.foodList = invoice.getFoodList() != null ? invoice.getFoodList() : new ArrayList<>();
        setTitle("Confirm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitle = new JLabel("Confirm");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Scrollable ticket list
        ticketListPanel = new JPanel();
        ticketListPanel.setLayout(new BoxLayout(ticketListPanel, BoxLayout.Y_AXIS));
        for (Ticket ticket : invoice.getTicketList()) {
            JPanel ticketPanel = new JPanel();
            ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
            ticketPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            ticketPanel.add(new JLabel("Movie name: " + ticket.getSchedule().getMovie().getMovieTitle()));
            ticketPanel.add(new JLabel("Screening room: " + ticket.getSchedule().getScreeningRoom().getRoomName()));
            ticketPanel.add(new JLabel("Seat number: " + ticket.getSeat().getSeatNumber()));
            ticketPanel.add(new JLabel("Showtime: " + ticket.getSchedule().getShowtime().getTime()));
            ticketPanel.add(new JLabel("Price: " + String.format("%.1f$", (double)ticket.getSeat().getPrice())));
            ticketListPanel.add(ticketPanel);
            ticketListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        JScrollPane scrollPane = new JScrollPane(ticketListPanel);
        scrollPane.setPreferredSize(new Dimension(400, 180));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Food service list (if any)
        if (foodList != null && !foodList.isEmpty()) {
            foodListPanel = new JPanel();
            foodListPanel.setLayout(new BoxLayout(foodListPanel, BoxLayout.Y_AXIS));
            JLabel foodTitle = new JLabel("Food service");
            foodTitle.setFont(new Font("Arial", Font.BOLD, 16));
            foodListPanel.add(foodTitle);
            foodListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            for (OrderedFood food : foodList) {
                JPanel foodPanel = new JPanel();
                foodPanel.setLayout(new BoxLayout(foodPanel, BoxLayout.Y_AXIS));
                foodPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                foodPanel.add(new JLabel("Code: " + food.getFoodItem().getId()));
                foodPanel.add(new JLabel("Name: " + food.getFoodItem().getName()));
                foodPanel.add(new JLabel("Size: " + food.getFoodItem().getSize()));
                foodPanel.add(new JLabel("Unit price: " + String.format("%.1f$", food.getFoodItem().getUnitPrice())));
                foodPanel.add(new JLabel("Quantity: " + food.getQuantity()));
                foodPanel.add(new JLabel("Price: " + String.format("%.1f$", food.getFoodItem().getUnitPrice() * food.getQuantity())));
                foodListPanel.add(foodPanel);
                foodListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            JScrollPane foodScrollPane = new JScrollPane(foodListPanel);
            foodScrollPane.setPreferredSize(new Dimension(400, 180));
            mainPanel.add(foodScrollPane);
        }
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Bottom panel for total price and buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTotalPrice = new JLabel("Total price:");
        lblTotalPrice.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalPrice.setPreferredSize(new Dimension(100, 40));
        bottomPanel.add(lblTotalPrice);

        lblPriceValue = new JLabel(String.format("%.1f$", (double)invoice.getTotal()));
        lblPriceValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblPriceValue.setPreferredSize(new Dimension(100, 40));
        bottomPanel.add(lblPriceValue);

        bottomPanel.add(Box.createHorizontalGlue());

        btnFoodService = new JButton("Food service");
        btnFoodService.setPreferredSize(new Dimension(120, 40));
        btnFoodService.addActionListener(this);
        bottomPanel.add(btnFoodService);
        btnConfirm = new JButton("Confirm");
        btnConfirm.setPreferredSize(new Dimension(120, 40));
        btnConfirm.addActionListener(this);
        bottomPanel.add(btnConfirm);
        mainPanel.add(bottomPanel);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFoodService) {
            this.setVisible(false);
            new FoodServiceFrm(invoice).setVisible(true);
        } else if (e.getSource() == btnConfirm) {
            // Create and save invoice
            double total = Double.parseDouble(lblPriceValue.getText().replace("$", ""));
            invoice.setTotal(total);
            invoice.setDateTime(new java.util.Date());
            InvoiceDAO invoiceDAO = new InvoiceDAO();
            if (invoiceDAO.addInvoice(invoice)) {
                // Save tickets
                TicketDAO ticketDAO = new TicketDAO();
                for (Ticket ticket : invoice.getTicketList()) {
                    ticket.setInvoiceId(invoice.getId());
                    ticketDAO.addTicket(ticket);
                }
                JOptionPane.showMessageDialog(this, "Booking successful!");
                this.dispose();
                new TicketingFrm(invoice.getUser()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save invoice!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 