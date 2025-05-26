package dao;

import model.Seat;
import model.ScreeningRoom;
import java.sql.*;
import java.util.*;

public class SeatDAO extends DAO {
    public SeatDAO() { super(); }

    public List<Seat> getSeatsByScreeningRoom(ScreeningRoom screeningRoom) {
        List<Seat> list = new ArrayList<>();
        String sql = "SELECT * FROM tblSeat WHERE ScreeningRoomid=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, screeningRoom.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Seat(
                    rs.getInt("id"),
                    rs.getString("seatNumber"),
                    0,
                    rs.getInt("ScreeningRoomid")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
} 