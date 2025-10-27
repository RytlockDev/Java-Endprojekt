package kosten.util;

public class Mitarbeiter {
	//========================
	// Klasseneigenschaften
	//========================
	/** Array mit Mitarbeiter-Objekten die am anfang<br>
	 *  erzeugt und hier reingelegt werden */
	public static  Mitarbeiter[] mitarbeiter =  new Mitarbeiter[0];
	
	//========================
	// Eigenschaften
	//========================
	/** Personalnummer */
	private int pers_id;
	/** Nachname und Vorname des MA */
	private String nachname, vorname;
	/** Strasse des MA */
	private String strasse;
	/** PLZ im Bereich von 1000 - 9999 */
	private int plz;
	/** Ort der Strasse */
	private String ort;
	
	//========================
	// Konstruktor
	//========================
	/**
	 * @param pers_id
	 * @param nachname
	 * @param vorname
	 * @param strasse
	 * @param plz
	 * @param ort
	 */
	public Mitarbeiter(int pers_id, String nachname, String vorname, String strasse, int plz, String ort) {
		this.pers_id = pers_id;
		this.nachname = nachname;
		this.vorname = vorname;
		this.strasse = strasse;
		this.plz = plz;
		this.ort = ort;
	}	
	
	//========================
	// Getter
	//========================
	public int getPers_id() {
		return pers_id;
	}
	
	public String getNachname() {
		return nachname;
	}

	public String getVorname() {
		return vorname;
	}

	public String getStrasse() {
		return strasse;
	}

	public int getPlz() {
		return plz;
	}

	public String getOrt() {
		return ort;
	}
	
	//========================
	// Ueberschriebene Methoden
	//========================
	/**
	 * Ueberschriebene Methode die ein Mitarbeiter Objekt
	 * mit Personalnummer und Nachname angibt in der ComboBox
	 */
	@Override
	public String toString() {
		return pers_id + ": " + nachname;
	}
}
