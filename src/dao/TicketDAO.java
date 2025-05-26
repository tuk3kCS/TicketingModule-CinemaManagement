package dao;

import model.Ticket;
import java.sql.*;
import java.util.*;

public class TicketDAO extends DAO {
    public TicketDAO() { super(); }

    public Map<Integer, Integer> getBookedSeatsIds(Ticket ticket) {
        Map<Integer, Integer> seatStatusMap = new HashMap<>();
        String sql = "SELECT s.id AS seatId, t.invoiceId, t.price FROM tblSeat s LEFT JOIN tblTicket t ON s.id = t.seatId AND t.scheduleId = ? WHERE s.ScreeningRoomid = ?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ticket.getSchedule().getId());
            ps.setInt(2, ticket.getSchedule().getScreeningRoom().getId());
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int seatId = rs.getInt("seatId");
                Integer invoiceId = rs.getObject("invoiceId") != null ? rs.getInt("invoiceId") : null;
                int price = rs.getObject("price") != null ? rs.getInt("price") : 0;
                if (invoiceId != null) {
                    seatStatusMap.put(seatId, -1);
                } else {
                    seatStatusMap.put(seatId, price);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatStatusMap;
    }
} 