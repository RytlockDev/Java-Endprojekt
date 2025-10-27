package kosten.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBini {
	/**
	 * Erzeugt die Mitarbeiter Objekte aus der Datenbank<br>
	 * und speichert sie im Mitarbeiter Array
	 */
	public static void erzeugeMitarbeiter() {
		try {
			// Stellt die Verbindung zur DB her
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			// SQL-Statement erstellen un die Daten abzurufen 
			String sql = "SELECT * FROM mitarbeiter";
			Statement stmt = conn.createStatement();
			
			// Speichert die Angekommen Daten in einem ResultSet
			ResultSet result = stmt.executeQuery(sql);
			
			int i = 0;
			// Vergroesert das Mitarbeiter Array um 1 und Speichert dann einen
			// Mitarbeiter aus dem ResultSet in dem ArrayIndex
			// Speichert alle Mitarbeiter aus der DB
			while(result.next()) {
				Mitarbeiter.mitarbeiter =  HilfsMethoden.arrayVergroesern(Mitarbeiter.mitarbeiter);
				Mitarbeiter.mitarbeiter[i++] = new Mitarbeiter(result.getInt("pers_id"), 
															   result.getString("nachname"), 
															   result.getString("vorname"), 
															   result.getString("strasse"), 
															   result.getInt("plz"), 
															   result.getString("ort"));
			}  
		  	  
			// Statement Schliesen
			stmt.close();
			// DB Schliesen
			conn.close();  	  		  
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
	}

	/**
	 * Liest die Kostenarten aus der DB aus und Speicher sie im Array<br>
	 * fuer die Tabellendaten
	 * @param array Das Array fuer die Tabellendaten
	 */
	public static void holeKostenart(Object[][] array) {
		try {
			// Stellt die Verbindung zur DB her
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			// SQL-Statement erstellen un die Daten abzurufen
			String sql = "SELECT beschreibung, einzelverguetung FROM kostenart";
			Statement stmt = conn.createStatement();
			
			// Speichert die Angekommen Daten in einem ResultSet
			ResultSet result = stmt.executeQuery(sql);
			
			int i = 0;
			// Speichert die Kostenarten ins Tabellen Array
			while(result.next()) {
				array[i][0] = result.getString("beschreibung");
				// Der Double Wert wird auf zwei stellen nach dem Komma vorformatiert
				array[i][2] = String.format("%.2f", result.getDouble("einzelverguetung"));
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
	
	/**
	 * Speichert einen neuen Mitarbeiter wenn er noch nicht existiert<br>
	 * in der Datenbank ab und erweitert das mitarbeiter Array und fuegt<br>
	 * ihn gleich hinzu
	 * @param array String Array mit den Mitarbeiter Daten
	 * @return Gibt die erzeugte ID die die Personlanummer darstellt zum<br>
	 * 		   weiterverarbeiten zurueck
	 */
	public static int speichereMitarbeiter(String[] array) {
		int generatedKey = 0;
		try {
			// Stellt die Verbindung zur DB her
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			// erstellt ein Vorgefertigtes SQL-Statement mit Platzhaltern
			String sql = "INSERT INTO mitarbeiter (nachname, vorname, strasse, plz, ort) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			// Werte aus dem Array einsetzen
			stmt.setString(1, array[0]);
			stmt.setString(2, array[1]);
			stmt.setString(3, array[2]);
			int plz = Integer.parseInt(array[3]);
			stmt.setInt(4, plz);
			stmt.setString(5, array[4]);
			
			stmt.executeUpdate();
			
			// Holt die erstelle ID aus dem ResultSet und speichert ihn
			// in der am anfang erszeugten Variable
			ResultSet result = stmt.getGeneratedKeys();
			if(result.next()) {
				generatedKey = result.getInt(1);
			}
			
			// Vergroesert das Mitarbeiter Array um 1 und Speichert dann einen
			// Mitarbeiter der neu Angelegt wurde an die letzte stelle im Array
			Mitarbeiter.mitarbeiter =  HilfsMethoden.arrayVergroesern(Mitarbeiter.mitarbeiter);
			Mitarbeiter.mitarbeiter[Mitarbeiter.mitarbeiter.length-1] = new Mitarbeiter(generatedKey, 
																							   array[0], 
																							   array[1], 
																							   array[2], 
																							   Integer.parseInt(array[3]), 
																							   array[4]);
			// Statement Schliesen
			stmt.close();
			// DB Schliesen
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return generatedKey;
		
	}

	/**
	 * Speichert aus der Tabelle jeden Datensatz einzeln der<br>
	 * eine anzahl groesser als 0 hatt
	 * @param datum Datum der Abrechnung
	 * @param pers_id Personalnummer des MA
	 * @param kostenartAnzahl Array mit der Anzahl und der Kosatenart als <br>
	 * 		  in einem Zweidimensionalen Array
	 * @param gesamtVerguetung Die erechnete verguetung zu jeder Anzahl
	 */
	public static void schreibeKostenart(Date datum, int pers_id, int[][] kostenartAnzahl,
			double[] gesamtVerguetung) {
		try {
			// Stellt die Verbindung zur DB her
			Connection conn = DriverManager.getConnection(Config.KOSTEN_URI);
			
			// erstellt ein Vorgefertigtes SQL-Statement mit Platzhaltern
			String sql = "INSERT INTO reisekosten (datum, pers_id, ka_id, anzahl, gesamtverguetung) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			// Geht das gesamte Array der tabelle durch und erstellt fuer
			// jede Zeile einen Datensatz in der DB wenn die Anzahl groeser 0 ist
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
