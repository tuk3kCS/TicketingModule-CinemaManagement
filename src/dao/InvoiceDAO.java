package dao;

import model.Invoice;
import java.sql.*;

public class InvoiceDAO extends DAO {
    public InvoiceDAO() { super(); }

    public boolean addInvoice(Invoice invoice) {
        String sql = "INSERT INTO tblInvoice(total, dateTime, userId) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, invoice.getTotal());
            ps.setTimestamp(2, new Timestamp(invoice.getDateTime().getTime()));
            ps.setInt(3, invoice.getUser().getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        invoice.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
} 