package kosten.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class DBini {
	public static void erzeugeMitarbeiter() {
		try {
			
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			String sql = "SELECT * FROM mitarbeiter";
			Statement stmt = conn.createStatement();
			
			ResultSet result = stmt.executeQuery(sql);
			
			int i = 0;
			while(result.next()) {
				Mitarbeiter.mitarbeiter =  HilfsMethoden.arrayVergroesern(Mitarbeiter.mitarbeiter);
				Mitarbeiter.mitarbeiter[i++] = new Mitarbeiter(result.getInt("pers_id"), 
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

	public static void holeKostenart(Object[][] array) {
		try {
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			String sql = "SELECT beschreibung, einzelverguetung FROM kostenart";
			Statement stmt = conn.createStatement();
			
			ResultSet result = stmt.executeQuery(sql);
			
			int i = 0;
			while(result.next()) {
				array[i][0] = result.getString("beschreibung");
				array[i][2] = result.getDouble("einzelverguetung") + "";
				i++;
			}
						
			// Statement Schliesen
			stmt.close();
			// DB Schliesen
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void speichereMitarbeiter(String[] maDaten) {
		int generatedKey;
		try {
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			String sql = "INSERT INTO mitarbeiter (nachname, vorname, strasse, plz, ort) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			// Werte aus dem Array einsetzen
			stmt.setString(1, maDaten[0]);
			stmt.setString(2, maDaten[1]);
			stmt.setString(3, maDaten[2]);
			int plz = Integer.parseInt(maDaten[3]);
			stmt.setInt(4, plz);
			stmt.setString(5, maDaten[4]);
			
			stmt.executeUpdate();
			
			ResultSet result = stmt.getGeneratedKeys();
			generatedKey = result.getInt(1);
			
			Mitarbeiter.mitarbeiter =  HilfsMethoden.arrayVergroesern(Mitarbeiter.mitarbeiter);
			Mitarbeiter.mitarbeiter[Mitarbeiter.mitarbeiter.length-1] = new Mitarbeiter(generatedKey, 
																						maDaten[0], 
																						maDaten[1], 
																						maDaten[2], 
																						Integer.parseInt(maDaten[3]), 
																						maDaten[4]);
			for(Object m: Mitarbeiter.mitarbeiter) {
				System.out.println(((Mitarbeiter) m).getPers_id());
			}
			// Statement Schliesen
			stmt.close();
			// DB Schliesen
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void schreibeKostenart(int pers_id, Date datum, int[][] kostenartAnzahl,
			double[] gesamtVerguetung) {
		try {
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			String sql = "INSERT INTO reisekosten (datum, pers_id, ka_id, anzahl, gesamtverguetung) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			for(int i = 0; i < kostenartAnzahl.length; i++) {
				if(kostenartAnzahl[i][1] > 0) {					
					stmt.setDate(1, datum);
					stmt.setInt(2, pers_id);
					stmt.setInt(3, kostenartAnzahl[i][0]);
					stmt.setInt(4, kostenartAnzahl[i][1]);
					stmt.setDouble(5, gesamtVerguetung[i]);
					
					stmt.executeUpdate();
				}
			
		    }
			
			// Statement Schliesen
			stmt.close();
			// DB Schliesen
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
