package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import dao.SeatDAO;
import dao.ScreeningRoomDAO;
import model.Seat;
import model.ScreeningRoom;
import dao.TicketDAO;
import model.Schedule;
import model.User;
import model.Ticket;
import model.Invoice;

public class SeatingFrm extends JFrame implements ActionListener {
    private JButton[][] seatButtons;
    private JLabel lblTotalPrice;
    private JLabel lblPriceValue;
    private JButton btnNext;
    private Map<String, Seat> seatMap;
    private Set<String> selectedSeats;
    private List<Seat> selectedSeatObjects;
    private User user;
    private Schedule schedule;
    private Ticket ticket;

    public SeatingFrm(User user, Schedule schedule) {
        this.user = user;
        this.schedule = schedule;

        setTitle("Seating");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        selectedSeats = new HashSet<>();
        seatMap = new HashMap<>();
        selectedSeatObjects = new ArrayList<>();

        ScreeningRoomDAO screeningRoomDAO = new ScreeningRoomDAO();
        ScreeningRoom screeningRoom = screeningRoomDAO.getScreeningRoomById(schedule);
        schedule.setScreeningRoom(screeningRoom);
        int numberOfSeats = screeningRoom.getNumberOfSeats();
        int rows = (int) Math.sqrt(numberOfSeats);
        int cols = (int) Math.ceil((double) numberOfSeats / rows);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 2, 2));
        seatButtons = new JButton[rows][cols];
        ticket = new Ticket();
        ticket.setSchedule(schedule);
        loadSeats(ticket, gridPanel, rows, cols, numberOfSeats);
        mainPanel.add(gridPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTotalPrice = new JLabel("Total price ");
        lblTotalPrice.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalPrice.setPreferredSize(new Dimension(100, 40));
        bottomPanel.add(lblTotalPrice);

        lblPriceValue = new JLabel("0$");
        lblPriceValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblPriceValue.setPreferredSize(new Dimension(100, 40));
        bottomPanel.add(lblPriceValue);

        btnNext = new JButton("Next");
        btnNext.setPreferredSize(new Dimension(100, 40));
        btnNext.addActionListener(this);
        bottomPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        bottomPanel.add(btnNext);

        mainPanel.add(bottomPanel);
        add(mainPanel);
    }

    private void loadSeats(Ticket ticket, JPanel gridPanel, int rows, int cols, int numberOfSeats) {
        SeatDAO seatDAO = new SeatDAO();
        ScreeningRoom screeningRoom = ticket.getSchedule().getScreeningRoom();
        List<Seat> seats = seatDAO.getSeatsByScreeningRoom(screeningRoom);
        Map<String, Seat> seatNumberMap = new HashMap<>();
        for (Seat seat : seats) {
            seatNumberMap.put(seat.getSeatNumber(), seat);
        }

        TicketDAO ticketDAO = new TicketDAO();
        Map<Integer, Integer> seatStatusMap = ticketDAO.getBookedSeatsIds(ticket);
        
        int seatIndex = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (seatIndex >= numberOfSeats) {
                    JButton btn = new JButton();
                    btn.setEnabled(false);
                    btn.setBackground(Color.RED);
                    seatButtons[i][j] = btn;
                    gridPanel.add(btn);
                    continue;
                }
                String seatLabel = seats.get(seatIndex).getSeatNumber();
                JButton btn = new JButton(seatLabel);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                btn.addActionListener(this);
                seatButtons[i][j] = btn;
                gridPanel.add(btn);
                Seat seat = seatNumberMap.get(seatLabel);
                if (seat != null) {
                    seatMap.put(seatLabel, seat);
                    Integer seatPrice = seatStatusMap.get(seat.getId());
                    
                    if (seatPrice == -1) {
                        btn.setEnabled(false);
                        btn.setBackground(Color.LIGHT_GRAY);
                    } else {
                        btn.setBackground(Color.WHITE);
                        seat.setPrice(seatPrice);
                    }
                } else {
                    btn.setEnabled(false);
                    btn.setBackground(Color.RED);
                }
                seatIndex++;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < seatButtons.length; i++) {
            for (int j = 0; j < seatButtons[i].length; j++) {
                if (e.getSource() == seatButtons[i][j]) {
                    String seatLabel = seatButtons[i][j].getText();
                    Seat seat = seatMap.get(seatLabel);
                    if (seat == null || seatButtons[i][j].getBackground().equals(Color.RED)) return;
                    if (selectedSeats.contains(seatLabel)) {
                        selectedSeats.remove(seatLabel);
                        seatButtons[i][j].setBackground(Color.WHITE);
                    } else {
                        selectedSeats.add(seatLabel);
                        seatButtons[i][j].setBackground(Color.GREEN);
                    }
                    updateTotalPrice();
                    return;
                }
            }
        }
        if (e.getSource() == btnNext) {
            selectedSeatObjects.clear();
            for (String seatLabel : selectedSeats) {
                Seat seat = seatMap.get(seatLabel);
                if (seat != null) selectedSeatObjects.add(seat);
            }
            if (selectedSeatObjects.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat.");
                return;
            }
            List<Ticket> ticketList = new ArrayList<>();
            for (Seat seat : selectedSeatObjects) {
                Ticket newTicket = new Ticket();
                newTicket.setSeat(seat);
                newTicket.setSchedule(schedule);
                newTicket.setPrice(seat.getPrice());
                ticketList.add(newTicket);
            }
            int totalPrice = getTotalPrice();
            Invoice invoice = new Invoice();
            invoice.setTotal(totalPrice);
            invoice.setUser(user);
            invoice.setTicketList(ticketList);
            this.setVisible(false);
            new view.ConfirmFrm(invoice).setVisible(true);
            return;
        }
    }

    private void updateTotalPrice() {
        int total = getTotalPrice();
        lblPriceValue.setText(String.format("%.1f$", (double) total));
    }

    private int getTotalPrice() {
        int total = 0;
        for (String seatLabel : selectedSeats) {
            Seat seat = seatMap.get(seatLabel);
            if (seat != null) total += seat.getPrice();
        }
        return total;
    }
} 