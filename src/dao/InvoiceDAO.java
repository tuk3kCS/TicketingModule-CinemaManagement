package dao;

import model.Invoice;
import model.Ticket;
import model.OrderedFood;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.List;

public class InvoiceDAO extends DAO {
    public InvoiceDAO() { super(); }

    public boolean addInvoice(Invoice invoice) {
        Connection localCon = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean success = false;
        
        try {
            localCon = this.con;
            localCon.setAutoCommit(false);
            
            String sql = "INSERT INTO tblInvoice(dateTime, userId) VALUES (?, ?)";
            ps = localCon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, new Timestamp(invoice.getDateTime().getTime()));
            ps.setInt(2, invoice.getUser().getId());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int invoiceId = rs.getInt(1);
                    invoice.setId(invoiceId);
                    
                    if (invoice.getTicketList() != null && !invoice.getTicketList().isEmpty()) {
                        boolean ticketsAdded = addTickets(invoice.getTicketList(), invoiceId);
                        if (!ticketsAdded) {
                            localCon.rollback();
                            return false;
                        }
                    }
                    
                    if (invoice.getFoodList() != null && !invoice.getFoodList().isEmpty()) {
                        boolean foodsInserted = addOrderedFoods(localCon, invoice);
                        if (!foodsInserted) {
                            localCon.rollback();
                            return false;
                        }
                    }
                    
                    localCon.commit();
                    success = true;
                }
            }
        } catch (SQLException e) {
            String errorMsg = "SQL Error: " + e.getMessage();
            JOptionPane.showMessageDialog(null, errorMsg, "Database Error", JOptionPane.ERROR_MESSAGE);
            
            try {
                if (localCon != null) {
                    localCon.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (localCon != null) {
                    localCon.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return success;
    }
    
    private boolean addTickets(List<Ticket> tickets, int invoiceId) {
        boolean allSuccess = true;
        
        for (Ticket ticket : tickets) {
            String updateSql = "UPDATE tblTicket SET invoiceId = ? WHERE scheduleId = ? AND seatId = ?";
            try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                updatePs.setInt(1, invoiceId);
                updatePs.setInt(2, ticket.getSchedule().getId());
                updatePs.setInt(3, ticket.getSeat().getId());
                
                int affected = updatePs.executeUpdate();
                if (affected > 0) {
                    // Successfully updated the ticket
                    ticket.setInvoiceId(invoiceId);
                    
                    // Get the ID of the updated ticket
                    String getIdSql = "SELECT id FROM tblTicket WHERE scheduleId = ? AND seatId = ?";
                    try (PreparedStatement idPs = con.prepareStatement(getIdSql)) {
                        idPs.setInt(1, ticket.getSchedule().getId());
                        idPs.setInt(2, ticket.getSeat().getId());
                        
                        ResultSet rs = idPs.executeQuery();
                        if (rs.next()) {
                            ticket.setId(rs.getInt("id"));
                        }
                    }
                } else {
                    System.out.println("Warning: Failed to update ticket for scheduleId=" + 
                                      ticket.getSchedule().getId() + " and seatId=" + ticket.getSeat().getId());
                    allSuccess = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                allSuccess = false;
            }
        }
        
        return allSuccess;
    }
    
    private boolean addOrderedFoods(Connection con, Invoice invoice) throws SQLException {
        if (invoice.getFoodList() == null || invoice.getFoodList().isEmpty()) {
            return true;
        }
        
        String sql = "INSERT INTO tblOrderedFood(quantity, unitPrice, invoiceId, foodItemId) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (OrderedFood food : invoice.getFoodList()) {
                ps.setInt(1, food.getQuantity());
                ps.setDouble(2, food.getFoodItem().getUnitPrice());
                ps.setInt(3, invoice.getId());
                ps.setInt(4, food.getFoodItem().getId());
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            for (int i = 0; i < results.length; i++) {
                if (results[i] != 1) {
                    return false;
                }
            }
        }
        return true;
    }
} 