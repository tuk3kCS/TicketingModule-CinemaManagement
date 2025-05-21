package dao;

import java.sql.*;
import model.Schedule;
import model.Movie;
import model.Showtime;
import model.ScreeningRoom;

public class ScheduleDAO extends DAO {
    public ScheduleDAO() { super(); }

    public Schedule getScheduleById(Showtime showtime) {
        String sql = "SELECT * FROM tblSchedule WHERE showtimeId=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, showtime.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("movieId"));
                ScreeningRoom screeningRoom = new ScreeningRoom();
                screeningRoom.setId(rs.getInt("screeningRoomId"));
                return new Schedule(
                    rs.getInt("id"),
                    rs.getDate("date"),
                    movie,
                    showtime,
                    screeningRoom
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 