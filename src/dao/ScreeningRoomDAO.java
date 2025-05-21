package dao;

import model.ScreeningRoom;
import model.Schedule;
import java.sql.*;

public class ScreeningRoomDAO extends DAO {
    public ScreeningRoomDAO() { super(); }

    public ScreeningRoom getScreeningRoomById(Schedule schedule) {
        String sql = "SELECT * FROM tblScreeningRoom WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, schedule.getScreeningRoom().getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ScreeningRoom(
                    rs.getInt("id"),
                    rs.getString("roomName"),
                    rs.getInt("numberOfSeats"),
                    rs.getString("roomType")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 