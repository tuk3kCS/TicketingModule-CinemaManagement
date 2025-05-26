package dao;

import model.Invoice;
import model.Ticket;
import model.OrderedFood;
import java.sql.*;
import java.util.List;

public class InvoiceDAO extends DAO {
    public InvoiceDAO() { super(); }

    public boolean addInvoice(Invoice invoice) {
        String sql = "INSERT INTO tblInvoice(dateTime, userId) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(invoice.getDateTime().getTime()));
            ps.setInt(2, invoice.getUser().getId());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int invoiceId = rs.getInt(1);
                        invoice.setId(invoiceId);
                        
                        if (invoice.getTicketList() != null && !invoice.getTicketList().isEmpty()) {
                            addTickets(invoice.getTicketList(), invoiceId);
                        }
                        
                        if (invoice.getFoodList() != null && !invoice.getFoodList().isEmpty()) {
                            addOrderedFoods(invoice);
                        }
                        
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean addTickets(List<Ticket> tickets, int invoiceId) {
        boolean allSuccess = true;
        String updateSql = "UPDATE tblTicket SET invoiceId = ? WHERE scheduleId = ? AND seatId = ?";
        
        for (Ticket ticket : tickets) {
            try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                updatePs.setInt(1, invoiceId);
                updatePs.setInt(2, ticket.getSchedule().getId());
                updatePs.setInt(3, ticket.getSeat().getId());
                
                int affected = updatePs.executeUpdate();
                if (affected > 0) {
                    ticket.setInvoiceId(invoiceId);
                    
                    String getIdSql = "SELECT id FROM tblTicket WHERE scheduleId = ? AND seatId = ?";
                    try (PreparedStatement idPs = con.prepareStatement(getIdSql);) {
                        idPs.setInt(1, ticket.getSchedule().getId());
                        idPs.setInt(2, ticket.getSeat().getId());
                        
                        try (ResultSet rs = idPs.executeQuery()) {
                            if (rs.next()) {
                                ticket.setId(rs.getInt("id"));
                            }
                        }
                    }
                } else {
                    allSuccess = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                allSuccess = false;
            }
        }
        
        return allSuccess;
    }
    
    private boolean addOrderedFoods(Invoice invoice) {
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
            for (int result : results) {
                if (result != 1) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 