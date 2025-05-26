package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import model.Invoice;
import model.Ticket;
import model.OrderedFood;
import dao.InvoiceDAO;

public class ConfirmFrm extends JFrame implements ActionListener {
    private JTable ticketTable;
    private JTable foodTable;
    private JButton btnFoodService;
    private JButton btnConfirm;
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

        JLabel ticketTitle = new JLabel("Ticket List");
        ticketTitle.setFont(new Font("Arial", Font.BOLD, 16));
        ticketTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(ticketTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        String[] ticketColumns = {"No.", "Movie", "Time", "Room", "Seat", "Price"};
        DefaultTableModel ticketModel = new DefaultTableModel(ticketColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        int ticketRowNum = 1;
        for (Ticket ticket : invoice.getTicketList()) {
            Object[] row = {
                ticketRowNum++,
                ticket.getSchedule().getMovie().getMovieTitle(),
                ticket.getSchedule().getShowtime().getTime(),
                ticket.getSchedule().getScreeningRoom().getRoomName(),
                ticket.getSeat().getSeatNumber(),
                String.format("%.1f$", (double)ticket.getSeat().getPrice())
            };
            ticketModel.addRow(row);
        }
        
        ticketTable = new JTable(ticketModel);
        ticketTable.getTableHeader().setReorderingAllowed(false);
        
        ticketTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        ticketTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        ticketTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        ticketTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        ticketTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        ticketTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        
        JScrollPane ticketScrollPane = new JScrollPane(ticketTable);
        ticketScrollPane.setPreferredSize(new Dimension(400, 150));
        mainPanel.add(ticketScrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        if (foodList != null && !foodList.isEmpty()) {
            JLabel foodTitle = new JLabel("Food Service List");
            foodTitle.setFont(new Font("Arial", Font.BOLD, 16));
            foodTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(foodTitle);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            
            String[] foodColumns = {"Code", "Name", "Size", "Quantity", "Price"};
            DefaultTableModel foodModel = new DefaultTableModel(foodColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            for (OrderedFood food : foodList) {
                Object[] row = {
                    food.getFoodItem().getId(),
                    food.getFoodItem().getName(),
                    food.getFoodItem().getSize(),
                    food.getQuantity(),
                    String.format("%.1f$", food.getFoodItem().getUnitPrice())
                };
                foodModel.addRow(row);
            }
            
            foodTable = new JTable(foodModel);
            foodTable.getTableHeader().setReorderingAllowed(false);
            
            foodTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            foodTable.getColumnModel().getColumn(1).setPreferredWidth(120);
            foodTable.getColumnModel().getColumn(2).setPreferredWidth(80);
            foodTable.getColumnModel().getColumn(3).setPreferredWidth(80);
            foodTable.getColumnModel().getColumn(4).setPreferredWidth(80);
            
            JScrollPane foodScrollPane = new JScrollPane(foodTable);
            foodScrollPane.setPreferredSize(new Dimension(400, 150));
            mainPanel.add(foodScrollPane);
        }
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

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
            try {
                // Set the date/time for the invoice
                invoice.setDateTime(new java.util.Date());
                InvoiceDAO invoiceDAO = new InvoiceDAO();
                if (invoiceDAO.addInvoice(invoice)) {
                    JOptionPane.showMessageDialog(this, "Booking successful!");
                    this.dispose();
                    new TicketingFrm(invoice.getUser()).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save invoice!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 