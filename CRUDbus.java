package BusScheduling;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CRUDbus {
    
    private String plate;
    private int capacity;
    
    public CRUDbus() {
    }
    
    public CRUDbus(String _plate, int _capacity) {
        plate = _plate;
        capacity = _capacity;
    }
    
    public String getPlate() {
        return plate;
    }
    
    public void setPlate(String plate) {
        this.plate = plate;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public void writetoDB(Connection conn) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO BUS (plate, capacity) VALUES('" + getPlate() + "', " + getCapacity() + ")";
            stmt.executeUpdate(sql);
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (stmt != null) stmt.close(); 
                } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
    public void deleteBus(Connection conn, String _plate) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "DELETE FROM BUS WHERE plate = '" + _plate + "'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}

