package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import model.Showtime;
import dao.ShowtimeDAO;
import java.sql.*;
import model.User;
import model.Movie;
import model.Schedule;
import model.ScreeningRoom;
import dao.ScheduleDAO;
import dao.ScreeningRoomDAO;
import dao.DAO;

public class TicketingFrm extends JFrame implements ActionListener {
    private JComboBox<MovieItem> cbMovie;
    private JComboBox<ShowtimeItem> cbShowtime;
    private JButton btnNext;
    private User user;
    private ScheduleDAO scheduleDAO;
    private ScreeningRoomDAO screeningRoomDAO;

    public TicketingFrm(User user) {
        this.user = user;

        setTitle("Ticketing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel lblTitle = new JLabel("Ticketing");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel moviePanel = new JPanel(new BorderLayout(10, 0));
        cbMovie = new JComboBox<>();
        cbMovie.setPreferredSize(new Dimension(250, 35));
        cbMovie.addActionListener(_ -> loadShowtimes());
        moviePanel.add(new JLabel("Movie"), BorderLayout.WEST);
        moviePanel.add(cbMovie, BorderLayout.CENTER);
        mainPanel.add(moviePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel showtimePanel = new JPanel(new BorderLayout(10, 0));
        cbShowtime = new JComboBox<>();
        cbShowtime.setPreferredSize(new Dimension(250, 35));
        showtimePanel.add(new JLabel("Showtime"), BorderLayout.WEST);
        showtimePanel.add(cbShowtime, BorderLayout.CENTER);
        mainPanel.add(showtimePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        btnNext = new JButton("Next");
        btnNext.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNext.setPreferredSize(new Dimension(100, 40));
        btnNext.addActionListener(this);
        mainPanel.add(btnNext);

        add(mainPanel);
        loadMovies();
    }

    private void loadMovies() {
        cbMovie.removeAllItems();
        try {
            Statement stmt = DAO.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, movieTitle FROM tblMovie");
            while (rs.next()) {
                MovieItem item = new MovieItem(rs.getInt("id"), rs.getString("movieTitle"));
                cbMovie.addItem(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (cbMovie.getItemCount() > 0) {
            cbMovie.setSelectedIndex(0);
            loadShowtimes();
        }
    }

    private void loadShowtimes() {
        cbShowtime.removeAllItems();
        MovieItem selectedMovie = (MovieItem) cbMovie.getSelectedItem();
        if (selectedMovie == null) return;
        ShowtimeDAO showtimeDAO = new ShowtimeDAO();
        Movie movie = new Movie();
        movie.setId(selectedMovie.getId());
        List<Showtime> showtimes = showtimeDAO.searchAvailableShowtime(movie);
        for (Showtime st : showtimes) {
            scheduleDAO = new ScheduleDAO();
            screeningRoomDAO = new ScreeningRoomDAO();
            Schedule schedule = scheduleDAO.getScheduleById(st);
            ScreeningRoom screeningRoom = screeningRoomDAO.getScreeningRoomById(schedule);
            ShowtimeItem item = new ShowtimeItem(st.getId(), st.getTime(), screeningRoom.getRoomName(), screeningRoom.getId(), schedule.getId());
            cbShowtime.addItem(item);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnNext) {
            MovieItem selectedMovie = (MovieItem) cbMovie.getSelectedItem();
            ShowtimeItem selectedShowtime = (ShowtimeItem) cbShowtime.getSelectedItem();
            if (selectedMovie == null || selectedShowtime == null) {
                JOptionPane.showMessageDialog(this, "Please select both a movie and a showtime.");
                return;
            }

            Movie movie = new Movie();
            movie.setId(selectedMovie.getId());
            movie.setMovieTitle(selectedMovie.getMovieTitle());

            Showtime showtime = new Showtime();
            showtime.setId(selectedShowtime.getId());
            showtime.setTime(selectedShowtime.getTime());

            ScreeningRoom screeningRoom = new ScreeningRoom();
            screeningRoom.setId(selectedShowtime.getScreeningRoomId());

            Schedule schedule = new Schedule();
            schedule.setId(selectedShowtime.getScheduleId());
            schedule.setMovie(movie);
            schedule.setShowtime(showtime);
            schedule.setScreeningRoom(screeningRoom);
            
            this.setVisible(false);
            new SeatingFrm(user, schedule).setVisible(true);
        }
    }

    private static class MovieItem {
        private int id;
        private String title;
        public MovieItem(int id, String title) { 
            this.id = id; 
            this.title = title; 
        }
        public int getId() { return id; }
        public String getMovieTitle() { return title; }
        @Override public String toString() { return title; }
    }

    private static class ShowtimeItem {
        private int id;
        private java.util.Date time;
        private String roomName;
        private int screeningRoomId;
        private int scheduleId;
        public ShowtimeItem(int id, java.util.Date time, String roomName, int screeningRoomId, int scheduleId) {
            this.id = id;
            this.time = time;
            this.roomName = roomName;
            this.screeningRoomId = screeningRoomId;
            this.scheduleId = scheduleId;
        }
        public int getId() { return id; }
        public java.util.Date getTime() { return time; }
        public int getScreeningRoomId() { return screeningRoomId; }
        public int getScheduleId() { return scheduleId; }
        @Override public String toString() { return time.toString() + " - " + roomName; }
    }
} 