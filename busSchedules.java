package BusScheduling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class busSchedules extends JFrame implements ActionListener{
	
	private Connection connection;
    private Statement statement;
    private DefaultTableModel model;
    private JTable table;
	
	JTextField tfPlate = new JTextField(10);
	JTextField tfCapacity = new JTextField(10);
	
	JButton addToDB = new JButton("Add to DB");
	JButton deleteBus = new JButton("Delete BUS");
	JButton listBus = new JButton("List BUS");
	JPanel panel = new JPanel();
	JScrollPane scrollPane;
	
    public busSchedules() {
        setTitle("View Bus Schedules");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	setLayout( new FlowLayout());
    	this.getContentPane().setBackground(Color.WHITE);
    	panel.setLayout(new GridLayout(2, 4, 10, 10));
    	panel.setBackground(Color.WHITE);
    	
		panel.add(new JLabel("Plate")); 
		panel.add( tfPlate );
		panel.add(new JLabel("Capacity:"));
		panel.add( tfCapacity);
		add(panel, BorderLayout.NORTH);
		
		panel.add( addToDB );
		panel.add( deleteBus );
		panel.add( listBus );
		add(panel, BorderLayout.NORTH);

		model = new DefaultTableModel();
	    table = new JTable(model);
	    scrollPane = new JScrollPane(table);
	    add(scrollPane, BorderLayout.CENTER);
	    
		
	    addToDB.addActionListener(this);
		deleteBus.addActionListener(this);
		listBus.addActionListener(this);

        setVisible(true);
        
        connection = databaseConnection.getConnection();
    }
    
    public void listBuses() {
        Vector<String> columns = new Vector<>();
        Vector<Vector<Object>> data = new Vector<>();
        ResultSet rs = null;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM bus";
            rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                columns.add(rsmd.getColumnName(i));
            }

            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getObject(i));
                }
                data.add(vector);
            }

            model.setDataVector(data, columns);

        } catch (SQLException e) {
            e.printStackTrace();
       } 

    }
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if ( e.getSource() == addToDB) {
			CRUDbus b = new CRUDbus(tfPlate.getText(), Integer.parseInt( tfCapacity.getText() ) );
			b.writetoDB(connection);
		} 
		else if ( e.getSource() == deleteBus ) {
			CRUDbus b = new CRUDbus();
			b.deleteBus(connection, tfPlate.getText());
		}
		else if ( e.getSource() == listBus ) {
			listBuses();
		}
		
	}
}

