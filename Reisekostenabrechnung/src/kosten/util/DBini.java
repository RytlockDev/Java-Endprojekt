package kosten.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBini {
	public static void erzeugeMitarbeiter() {
		try {
			int i = 0;
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			String sql = "SELECT * FROM mitarbeiter";
			Statement stmt = conn.createStatement();
			
			ResultSet result = stmt.executeQuery(sql);
			
			while(result.next()) {
				Mitarbeiter.mitarbeiter = (Mitarbeiter[]) HilfsMethoden.arrayVergroesern(Mitarbeiter.mitarbeiter);
				Mitarbeiter.mitarbeiter[i++] = new Mitarbeiter(result.getInt("per_id"), 
															   result.getString("nachname"), 
															   result.getString("vorname"), 
															   result.getString("strasse"), 
															   result.getInt("plz"), 
															   result.getString("ort"));
			}  
		  	  
			stmt.close();  
			conn.close();  	  		  
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
	}

	public static void holeKostenart(String[][] kostenart) {
		try {
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			String sql = "SELECT * FROM mitarbeiter";
			Statement stmt = conn.createStatement();
			
			ResultSet result = stmt.executeQuery(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
