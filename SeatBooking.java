package BusScheduling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeatBooking {
    private Connection connection;
    public SeatBooking(Connection connection) {
        this.connection = connection;
    }

    private Integer busId;
    private String departureTime;
    
    public Integer getBusId() {
        return busId;
    }
    public String getDepartureTime() {
        return departureTime;
    }
	public void setBusId(Integer busId) {
		this.busId = busId;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
    public void bookSeat(Integer busId, String departureTime) {
    
        JComboBox<String> seatComboBox = new JComboBox<>();
        JComboBox<String> originComboBox = new JComboBox<>();
        JComboBox<String> destinationComboBox = new JComboBox<>();
        JComboBox<String> timeComboBox = new JComboBox<>();
          
        try {
        	System.out.println("Bus ID: " + busId);
            String capacityQuery = "SELECT capacity FROM bus WHERE bus_id = ?";
            PreparedStatement capacityStatement = connection.prepareStatement(capacityQuery);
            capacityStatement.setInt(1, busId);
            ResultSet capacityResult = capacityStatement.executeQuery();
            
            int totalSeats = 0;
            if (capacityResult.next()) {
                totalSeats = capacityResult.getInt("capacity");
            }
            capacityResult.close();
            capacityStatement.close();
            System.out.println(totalSeats);
            
            String bookedSeatsQuery = 
            "SELECT COUNT(*) AS seat_number FROM reservations " +
            "INNER JOIN schedule ON reservations.schedule_id = schedule.schedule_id " +
            "WHERE schedule.bus_id = ? AND reservations.status = ?";
            PreparedStatement bookedSeatsStatement = connection.prepareStatement(bookedSeatsQuery);
            bookedSeatsStatement.setInt(1, busId);
            bookedSeatsStatement.setInt(2, 1); 
            ResultSet bookedSeatsResult = bookedSeatsStatement.executeQuery();

            int bookedSeats = 0;
            if (bookedSeatsResult.next()) {
                bookedSeats = bookedSeatsResult.getInt("seat_number");
            }
            bookedSeatsResult.close();
            bookedSeatsStatement.close();
            
            for (int i = 1; i <= totalSeats ; i++) {
                seatComboBox.addItem(String.valueOf(i));
            }
            
            String originDestQuery = "SELECT r.origin, r.destination FROM route r INNER JOIN schedule s "
            		+ "ON r.route_id = s.route_id WHERE s.bus_id = ?";
            PreparedStatement originDestStatement = connection.prepareStatement(originDestQuery);
            originDestStatement.setInt(1, busId);
            ResultSet originDestResult = originDestStatement.executeQuery();
            if (originDestResult.next()) {
                String origin = originDestResult.getString("origin");
                String destination = originDestResult.getString("destination");
                originComboBox.addItem(origin);
                destinationComboBox.addItem(destination);
            }
            originDestResult.close();
            originDestStatement.close();

            timeComboBox.addItem(departureTime);

        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        
        JFrame busInfoFrame = new JFrame("Bus Reservation");
        JPanel busInfoPanel = new JPanel(new GridLayout(5,2, 20, 20));
        JPanel InfoPanel = new JPanel(new GridLayout(1,2, 10, 10));
        busInfoPanel.setBackground(Color.WHITE);
        InfoPanel.setBackground(Color.WHITE); 
        busInfoFrame.getContentPane().setBackground(Color.WHITE);
        busInfoFrame.setLayout(new FlowLayout());
        
        JTextField customerNameField = new JTextField();
        JTextField customerNumberField = new JTextField();
        InfoPanel.add(new JLabel("Customer Name:"));
        InfoPanel.add(customerNameField);
        InfoPanel.add(new JLabel("Customer Number:"));
        InfoPanel.add(customerNumberField);

    
        busInfoPanel.add(new JLabel("Seat:"));
        busInfoPanel.add(seatComboBox);
        busInfoPanel.add(new JLabel("Origin:"));
        busInfoPanel.add(originComboBox);
        busInfoPanel.add(new JLabel("Destination:"));
        busInfoPanel.add(destinationComboBox);
        busInfoPanel.add(new JLabel("Time:"));
        busInfoPanel.add(timeComboBox);


        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement booking confirmation logic here
                String customerName = customerNameField.getText();
                String customerNumber = customerNumberField.getText();
                String seat = (String) seatComboBox.getSelectedItem();
                String origin = (String) originComboBox.getSelectedItem();
                String destination = (String) destinationComboBox.getSelectedItem();
                String time = (String) timeComboBox.getSelectedItem();

                try {
                    // Save customer info to the customer table
                    String customerInsertQuery = "INSERT INTO customer (full_name, phone_number) VALUES (?, ?)";
                    PreparedStatement customerStatement = connection.prepareStatement(customerInsertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                    customerStatement.setString(1, customerName);
                    customerStatement.setString(2, customerNumber);
                    customerStatement.executeUpdate();

                    ResultSet generatedKeys = customerStatement.getGeneratedKeys();
                    int customerId = 0;
                    if (generatedKeys.next()) {
                        customerId = generatedKeys.getInt(1);
                    }
                    generatedKeys.close();
                    customerStatement.close();

                    // Get schedule_id for the selected bus and departure time
                    String scheduleQuery =
                            "SELECT schedule_id FROM schedule " +
                                    "WHERE bus_id = ? AND departure_time = ?";
                    PreparedStatement scheduleStatement = connection.prepareStatement(scheduleQuery);
                    scheduleStatement.setInt(1, busId);
                    scheduleStatement.setString(2, departureTime);
                    ResultSet scheduleResult = scheduleStatement.executeQuery();

                    int scheduleId = 0;
                    if (scheduleResult.next()) {
                        scheduleId = scheduleResult.getInt("schedule_id");
                    } else {
                        JOptionPane.showMessageDialog(null, "No schedule found for Bus ID: " + busId + " at " + departureTime, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    scheduleResult.close();
                    scheduleStatement.close();

                    // Save the reservation to the reservations table
                    String reservationInsertQuery =
                            "INSERT INTO reservations (customer_id, schedule_id, seat_number, status) VALUES (?, ?, ?, ?)";
                    PreparedStatement reservationStatement = connection.prepareStatement(reservationInsertQuery);
                    reservationStatement.setInt(1, customerId);
                    reservationStatement.setInt(2, scheduleId);
                    reservationStatement.setInt(3, Integer.parseInt(seat));
                    reservationStatement.setInt(4, 1); // Assuming status 1 means booked
                    reservationStatement.executeUpdate();
                    reservationStatement.close();

                    // Show confirmation message
                    JOptionPane.showMessageDialog(null, "Seat booked for Bus ID: " + busId + " departing at " + departureTime, "Seat Booking", JOptionPane.INFORMATION_MESSAGE);
                    busInfoFrame.dispose(); // Close the frame after booking confirmation
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Booking failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        busInfoPanel.add(confirmButton);


        busInfoFrame.getContentPane().add(InfoPanel);
        busInfoFrame.getContentPane().add(busInfoPanel);
        busInfoFrame.setBounds(400,20, 600 ,500);
        
        busInfoFrame.setLocationRelativeTo(null); // Center the frame on the screen
        busInfoFrame.setVisible(true);
        
    }
}

