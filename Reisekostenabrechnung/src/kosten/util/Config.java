package kosten.util;

public class Config {
	/** Erzeugt einen MySQL Treiber um<br> 
	 *  um sich mit einer DB zu verbinden*/ 
	public static final String TREIBER = "com.mysql.cj.jdbc.Driver";
	
	/** Verbindungs Details fur die Datenbank "kosten"*/
	public static final String KOSTEN_URI = "jdbc:mysql://127.0.0.1/kosten?user=javauser&password=profil";
	
	public static final String DATUM_FORMAT = "dd.MM.yyyy";
}
