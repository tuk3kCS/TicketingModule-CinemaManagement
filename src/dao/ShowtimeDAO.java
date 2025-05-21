package dao;

import model.Showtime;
import java.sql.*;
import java.util.*;
import model.Movie;

public class ShowtimeDAO extends DAO {
    public ShowtimeDAO() { super(); }

    public List<Showtime> searchAvailableShowtime(Movie movie) {
        List<Showtime> list = new ArrayList<>();
        String sql = "SELECT s.*, sh.time FROM tblSchedule s JOIN tblShowtime sh ON s.showtimeId = sh.id WHERE s.Movieid=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, movie.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Showtime(
                    rs.getInt("id"),
                    rs.getTimestamp("time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
} 