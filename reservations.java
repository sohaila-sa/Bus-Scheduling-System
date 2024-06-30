package BusScheduling;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class reservations extends JFrame {
	private Connection connection ;
	DefaultTableModel model;
	
	  JLabel lblOrigin = new JLabel("Origin:");
      JLabel lblDestination = new JLabel("Destination:");
      JLabel lblDate = new JLabel("Date:");
      
      JTextField txtOrigin = new JTextField(10); 
      JTextField txtDestination = new JTextField(10);
      JTextField txtDate = new JTextField(10);
      
      JButton btnSearch = new JButton("Search Buses");
      JButton btnBook = new JButton("Book Seat");
      
      JPanel searchPanel = new JPanel();
      JScrollPane scrollPane;
      JTable table;
    public reservations() {
    	
        setTitle("Make Reservation");
        setBounds(400,20, 600 ,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        this.getContentPane().setBackground(Color.WHITE);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setLayout(new GridLayout(4,2,10,10));
       
    
       
        model = new DefaultTableModel();
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAvailableBuses(txtOrigin.getText(), txtDestination.getText(), txtDate.getText());
            }
        });
        
        btnBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
               
                    Integer busId = (Integer) model.getValueAt(selectedRow, model.findColumn("bus_id"));
                    Object departureTimeObj = model.getValueAt(selectedRow, model.findColumn("departure_time"));

                   
                    String departureTime;
                    if (departureTimeObj instanceof Timestamp) {
                        departureTime = ((Timestamp) departureTimeObj).toString();
                    } else {
                        departureTime = departureTimeObj.toString();
                    }
                    SeatBooking booking = new SeatBooking(connection);
                    booking.bookSeat(busId, departureTime);
                   // bookSeat(busId, departureTime);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a bus to book a seat.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        searchPanel.add(lblOrigin);
        searchPanel.add(txtOrigin);
        searchPanel.add(lblDestination);
        searchPanel.add(txtDestination);
        searchPanel.add(lblDate);
        searchPanel.add(txtDate);
        searchPanel.add(btnSearch);
        searchPanel.add(btnBook);
        
        
        setVisible(true);
        
        connection = databaseConnection.getConnection();
    }
    
    private void displayAvailableBuses(String origin, String destination, String date) {
    	Vector<String> columns = new Vector<>();
        Vector<Vector<Object>> data = new Vector<>();
        ResultSet rs = null;
        Statement statement = null;

        try {
           
            String query = "SELECT route.*, schedule.bus_id, schedule.departure_time FROM route INNER JOIN schedule ON "
            		+ "route.route_id = schedule.route_id WHERE route.origin = ? AND route.destination = ? AND schedule.departure_time = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, date);
            
            
            rs = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                columns.add(rsmd.getColumnName(i));
            }

            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    if (value instanceof Timestamp) {
                        value = value.toString();
                    }
                    vector.add(value);
                }
                data.add(vector);
            }
            model.setDataVector(data, columns);
            
        } catch (SQLException e) {
            e.printStackTrace();
         } 
        JOptionPane.showMessageDialog(this, "Buses from " + origin + " to " + destination + " on " + date);
    }



}

