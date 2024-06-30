package BusScheduling;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.concurrent.*;


//    private JLabel[][] seatLabels;
//
//    public seats() {
//        setTitle("View Seat Status");
//        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setBounds(400,20, 600 ,500);
//        JPanel panel = new JPanel(new GridLayout(10, 4));
//        seatLabels = new JLabel[10][4];
//        
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 4; j++) {
//                seatLabels[i][j] = new JLabel("Seat " + (i*4 + j+1), SwingConstants.CENTER);
//                seatLabels[i][j].setOpaque(true);
//                seatLabels[i][j].setBackground(Color.GREEN);
//                panel.add(seatLabels[i][j]);
//            }
//        }
//        
//        add(panel, BorderLayout.CENTER);
//        
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(new Runnable() {
//            public void run() {
//                updateSeatStatus();
//            }
//        }, 0, 1, TimeUnit.SECONDS);
//        
//        setVisible(true);
//    }
//    
//    private void updateSeatStatus() {
//        try {
//          
//
//            String query = "SELECT seat_number, status FROM reservations";
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            
//            ResultSet resultSet = statement.executeQuery();
//
//
//            while (resultSet.next()) {
//                int seatNumber = resultSet.getInt("seat_number");
//                boolean status = resultSet.getBoolean("status");
//                int row = (seatNumber - 1) / 4;
//                int col = (seatNumber - 1) % 4;
//
//              
//                if (status) {
//                    seatLabels[row][col].setBackground(Color.RED); // Occupied seat
//                } else {
//                    seatLabels[row][col].setBackground(Color.GREEN); // Available seat
//                }
//            }
//
//            
//            resultSet.close();
//            statement.close();
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            
//        }
//    }


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class seats extends JFrame {
    private JLabel statusLabel;
    private String selectedBusId;
    Connection connection;
    public seats() {
        setTitle("Seat Status");
        setBounds(400,20, 400 ,300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(Color.white);
        initComponents();
        setVisible(true);
    }
    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        statusLabel = new JLabel("Select the bus ID to see the status!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel, BorderLayout.CENTER);

        JButton selectBusButton = new JButton("Select Bus");
        selectBusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	String busId = JOptionPane.showInputDialog("Enter bus ID:"); 
                selectBus(busId);
            }
        });
        panel.add(selectBusButton, BorderLayout.SOUTH);
        panel.setBackground(Color.white);
        add(panel);

       
        SeatStatusUpdater updater = new SeatStatusUpdater();
        Thread updaterThread = new Thread(updater);
        updaterThread.start();
    }

    private void selectBus(String busId) {
        selectedBusId = busId;
      
        updateSeatStatus();
    }

    private void updateSeatStatus() {
        if (selectedBusId == null) {
            return;
        }
        try { 
        	connection = databaseConnection.getConnection();
            
            String sql = "SELECT COUNT(*) AS seat_number FROM reservations " +
                    "INNER JOIN schedule ON reservations.schedule_id = schedule.schedule_id " +
                    "WHERE schedule.bus_id = ? AND reservations.status = 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, selectedBusId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int reservedSeats = resultSet.getInt("seat_number");
                int totalSeats = 50;
                int emptySeats = totalSeats - reservedSeats;
                statusLabel.setText("Seat Status: " + emptySeats + " empty out of " + totalSeats);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching seat status from database: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private class SeatStatusUpdater implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    updateSeatStatus();
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}


