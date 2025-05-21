package dao;

import model.Ticket;
import java.sql.*;
import java.util.*;

public class TicketDAO extends DAO {
    public TicketDAO() { super(); }

    public boolean addTicket(Ticket ticket) {
        String sql = "INSERT INTO tblTicket(price, seatId, scheduleId, invoiceId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ticket.getPrice());
            ps.setInt(2, ticket.getSeat().getId());
            ps.setInt(3, ticket.getSchedule().getId());
            ps.setInt(4, ticket.getInvoiceId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        ticket.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> getBookedSeatIds(Ticket ticket) {
        List<Integer> seatIds = new ArrayList<>();
        String sql = "SELECT seatId FROM tblTicket WHERE scheduleId=? AND seatId IN (SELECT id FROM tblSeat WHERE ScreeningRoomid=?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ticket.getSchedule().getId());
            ps.setInt(2, ticket.getSchedule().getScreeningRoom().getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                seatIds.add(rs.getInt("seatId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatIds;
    }
} 