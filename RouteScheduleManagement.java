package BusScheduling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector; 

public class RouteScheduleManagement extends JFrame {

    private Connection connection;
    private JTextField routeIdField, originField, destinationField, durationField;
    private JTextField scheduleIdField, busIdField, departureTimeField, arrivalTimeField;

    public RouteScheduleManagement() {
       
        setTitle("Route and Schedule Management");
        setBounds(400,20, 600 ,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setBackground(Color.WHITE);
        

        JLabel routeIdLabel = new JLabel("Route ID:");
        routeIdField = new JTextField(10);
        JLabel originLabel = new JLabel("Origin:");
        originField = new JTextField(10);
        JLabel destinationLabel = new JLabel("Destination:");
        destinationField = new JTextField(10);
        JLabel durationLabel = new JLabel("Duration:");
        durationField = new JTextField(10);
        JButton addRouteButton = new JButton("Add Route");
        JButton updateRouteButton = new JButton("Update Route");
        JButton showRoutesButton = new JButton("Show Routes");
        
       
        JLabel scheduleIdLabel = new JLabel("Schedule ID:");
        scheduleIdField = new JTextField(10);
        JLabel routeIdLabel2 = new JLabel("Route ID:");
        JTextField routeIdField2 = new JTextField(10);
        JLabel busIdLabel = new JLabel("Bus ID:");
        busIdField = new JTextField(10);
        JLabel departureTimeLabel = new JLabel("Departure Time:");
        departureTimeField = new JTextField(10);
        JLabel arrivalTimeLabel = new JLabel("Arrival Time:");
        arrivalTimeField = new JTextField(10);
        JButton addScheduleButton = new JButton("Add Schedule");
        JButton updateScheduleButton = new JButton("Update Schedule");
        JButton showSchedulesButton = new JButton("Show Schedules");
        
        JPanel routePanel = new JPanel(new GridLayout(7, 2));
       // routePanel.setBackground(Color.WHITE);
        routePanel.add(routeIdLabel);
        routePanel.add(routeIdField);
        routePanel.add(originLabel);
        routePanel.add(originField);
        routePanel.add(destinationLabel);
        routePanel.add(destinationField);
        routePanel.add(durationLabel);
        routePanel.add(durationField);
        routePanel.add(addRouteButton);
        routePanel.add(updateRouteButton);
        routePanel.add(showRoutesButton);

        JPanel schedulePanel = new JPanel(new GridLayout(8, 2));
       // schedulePanel.setBackground(Color.WHITE);
        schedulePanel.add(scheduleIdLabel);
        schedulePanel.add(scheduleIdField);
        schedulePanel.add(routeIdLabel2);
        schedulePanel.add(routeIdField2);
        schedulePanel.add(busIdLabel);
        schedulePanel.add(busIdField);
        schedulePanel.add(departureTimeLabel);
        schedulePanel.add(departureTimeField);
        schedulePanel.add(arrivalTimeLabel);
        schedulePanel.add(arrivalTimeField);
        schedulePanel.add(addScheduleButton);
        schedulePanel.add(updateScheduleButton);
        schedulePanel.add(showSchedulesButton);

        JPanel mainPanel = new JPanel(new GridLayout(2,2));
        mainPanel.setBackground(Color.WHITE);
        JPanel title1 = new JPanel();
        JPanel title2 = new JPanel();
        
        mainPanel.add(routePanel);
        mainPanel.add(schedulePanel);

        add(mainPanel);
        setVisible(true);
        
        connection = databaseConnection.getConnection();
        
        addRouteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoute();
            }
        });

        updateRouteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoute();
            }
        });

        addScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSchedule();
            }
        });

        updateScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSchedule();
            }
        });
        showRoutesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllRoutes();
            }
        });

        showSchedulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllSchedules();
            }
        });

    }

    private void addRoute() {
        try {
           
            String origin = originField.getText();
            String destination = destinationField.getText();
            String duration = durationField.getText();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO route (origin, destination, duration) VALUES ( ?, ?, ?)");
//            preparedStatement.setInt(1, routeId);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, duration);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            JOptionPane.showMessageDialog(this, "Route added successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding route: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSchedule() {
        try {
            int scheduleId = Integer.parseInt(scheduleIdField.getText());
            int routeId = Integer.parseInt(routeIdField.getText());
            int busId = Integer.parseInt(busIdField.getText());
            String departureTime = departureTimeField.getText();
            String arrivalTime = arrivalTimeField.getText();

            // Perform insertion into the schedule table
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO schedule ( route_id, bus_id, departure_time, arrival_time) VALUES ( ?, ?, ?, ?)");
            preparedStatement.setInt(1, routeId);
            preparedStatement.setInt(2, busId);
            preparedStatement.setString(3, departureTime);
            preparedStatement.setString(4, arrivalTime);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            JOptionPane.showMessageDialog(this, "Schedule added successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding schedule: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSchedule() {
        try {
            int scheduleId = Integer.parseInt(scheduleIdField.getText());
            int routeId = Integer.parseInt(routeIdField.getText());
            int busId = Integer.parseInt(busIdField.getText());
            String departureTime = departureTimeField.getText();
            String arrivalTime = arrivalTimeField.getText();

            // Perform update in the schedule table
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE schedule SET route_id = ?, bus_id = ?, departure_time = ?, arrival_time = ? WHERE schedule_id = ?");
            preparedStatement.setInt(1, routeId);
            preparedStatement.setInt(2, busId);
            preparedStatement.setString(3, departureTime);
            preparedStatement.setString(4, arrivalTime);
            preparedStatement.setInt(5, scheduleId);
            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Schedule updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No schedule found with ID: " + scheduleId, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating schedule: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateRoute() {
        try {
            int routeId = Integer.parseInt(routeIdField.getText());
            String origin = originField.getText();
            String destination = destinationField.getText();
            String duration = durationField.getText();

            // Perform update in the route table
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE route SET origin = ?, destination = ?, duration = ? WHERE route_id = ?");
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, duration);
            preparedStatement.setInt(4, routeId);
            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Route updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No route found with ID: " + routeId, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating route: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void showAllRoutes() {
        try {
            // Query to retrieve all routes
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM route");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create a table to display routes
            JTable table = new JTable(buildTableModel(resultSet));
            JOptionPane.showMessageDialog(this, new JScrollPane(table), "All Routes", JOptionPane.PLAIN_MESSAGE);

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving routes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAllSchedules() {
        try {
            // Query to retrieve all schedules
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM schedule");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create a table to display schedules
            JTable table = new JTable(buildTableModel(resultSet));
            JOptionPane.showMessageDialog(this, new JScrollPane(table), "All Schedules", JOptionPane.PLAIN_MESSAGE);

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving schedules: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Get column names
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Get row data
        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(resultSet.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }


}

