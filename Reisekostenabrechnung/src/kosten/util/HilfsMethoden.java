package kosten.util;

import javax.swing.JComboBox;

public class HilfsMethoden {
	/**
	 * Vergroesert das Mitarbeiter Array um 1 Element
	 * @param array das Zu vergroesernde Array
	 * @return Gibt das Vergroeserte Array zurueck damit man es ggf.
	 * 		   in den benoetigen Objekt Typ casten kann
	 */
	public static Mitarbeiter[] arrayVergroesern(Mitarbeiter[] array) {
		Mitarbeiter[] temp = new Mitarbeiter[array.length+1];		
		for(int i = 0; i < array.length; i++) {
			temp[i] = array[i];
		} 		
		return temp;
	}

	/**
	 * - Leer am Anfang die Combobox komplett<br>
	 * - Fuellt die Combo box mit den Mitarbeitern<br>
	 * 	 zum auswaehlen wer man ist wenn der eintrag Existiert
	 * @param combobox Combobox aus dem JPanel
	 * @param array Mitarbeiter Array
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void fuelleCombobox(JComboBox combobox, Mitarbeiter[] array) {
		combobox.removeAllItems();
		combobox.addItem(null);
		for(Mitarbeiter ma: array) {
			combobox.addItem(ma);
		}
	}

	/**
	 * Sucht anhand der eingegebenen Daten im Mitarbeiter<br>
	 * Array ob der MA schon Existiert
	 * @param maDaten Eingetragene Daten im Formluar
	 * @return Mitarbeiter: wird zururckgegeben wenn er Existiert<br>
	 * 		   null: wenn er nicht Existiert
	 */
	public static Mitarbeiter exsistMitarbeiter(String[] maDaten) {
		for(int i = 0; i < Mitarbeiter.mitarbeiter.length; i++) {
			if(Mitarbeiter.mitarbeiter[i].getNachname().equals(maDaten[0])
			&& Mitarbeiter.mitarbeiter[i].getVorname().equals(maDaten[1])
			&& Mitarbeiter.mitarbeiter[i].getStrasse().equals(maDaten[2])
			&& Mitarbeiter.mitarbeiter[i].getPlz() == Integer.parseInt(maDaten[3])
			&& Mitarbeiter.mitarbeiter[i].getOrt().equals(maDaten[4])){
				return Mitarbeiter.mitarbeiter[i];
			}
		}
		return null;
	}
}
