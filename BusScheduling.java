package BusScheduling;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BusScheduling extends JFrame implements ActionListener {
	
	JButton btnReservation = new JButton("Reservations");
	JButton btnSchedules = new JButton("Manage Bus");
    JButton btnManageBuses = new JButton("Manage Schedules & Routes");
    JButton btnSeatStatus = new JButton("Seat Status");
    
	JPanel panel = new JPanel();
	JPanel picPanel = new JPanel();
	
	public BusScheduling() {
		this.setTitle("Bus Scheduling System");
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(2, 2, 10, 10));
		picPanel.setLayout(new FlowLayout());
		picPanel.setBackground(Color.white);
		ImageIcon image = new ImageIcon("bus.png"); 
		
		
		Image image1 = image.getImage(); 
		Image newImage = image1.getScaledInstance(450, 250, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newImageIcon = new ImageIcon(newImage);
		JLabel imageLabel = new JLabel(newImageIcon);
		
		
        imageLabel.setHorizontalTextPosition(JLabel.CENTER);
        imageLabel.setVerticalTextPosition(JLabel.BOTTOM);
		
        // Add action listeners for each button
        btnSchedules.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 new busSchedules();
            }
        });
        
        btnReservation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 new reservations();
            }
        });
        
        btnManageBuses.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RouteScheduleManagement();
            }
        });
        
        btnSeatStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new seats();
            }
        });
        
        picPanel.add(imageLabel);
        panel.add(btnReservation);
        panel.add(btnSchedules);
        panel.add(btnManageBuses);
        panel.add(btnSeatStatus);
        
        add(picPanel);
        add(panel);
		this.setResizable(false);
		this.setBounds(400,20, 600 ,500);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new BusScheduling();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		
	}

}
