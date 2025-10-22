package kosten.util;

import javax.swing.JComboBox;

public class HilfsMethoden {
	/**
	 * Vergroesert ein Uebergebendes Array um 1 Element
	 * @param array das Zu vergroesernde Array
	 * @return Gibt das Vergroeserte Array zurueck damit man es ggf.
	 * 		   in den benoetigen Objekt Typ casten kann
	 */
	public static Object[] arrayVergroesern(Object[] array) {
		Object[] temp = new Object[array.length+1];		
		for(int i = 0; i < array.length; i++) {
			temp[i] = array[i];
		} 		
		return temp;
	}

	public static JComboBox fuelleCombobox(JComboBox combobox, Object[] array) {
		combobox.removeAllItems();
		combobox.addItem(null);
		for(Object o: array) {
			combobox.addItem(o);
		}
		return combobox;
	}

}
