package kosten.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import kosten.util.Config;
import kosten.util.DBini;
import kosten.util.HilfsMethoden;
import kosten.util.Mitarbeiter;

@SuppressWarnings("serial")
public class MainPanel extends JPanel 
					   implements ActionListener {
	//========================
	// Klasseneigenschaften
	//========================
	// Arrays um die Eingabe zu Speichern
	/** Array was die Kostenarten und die jeweilige<br>
	 * Anahl Speichert */
	private static int[][] kostenartAnzahl = new int[4][2];
	/** Speichert die Erechnete Gesamtverguetung als Ddouble */
	private static double[] gesamtVerguetung = new double[4];
	/** Speichert die eingegebenen Mitarbeiter Daten */
	private static String[] maDaten = new String[5];
	
	//========================
	// Eigenschaften
	//========================
	// JButtons
	private JButton btnSelect = new JButton("Auswehlen");
	private JButton btnRemove = new JButton("Abwaehlen");
	private JButton btnSave = new JButton("Speichern");
	private JButton btnCancel = new JButton("Abbrechen");
	
	// JComboBox zum Anzeigen der Mitarbeiter
	@SuppressWarnings("rawtypes")
	private JComboBox cbMitarbeiter = new JComboBox();
	
	// Textfelder zum Einlesen der Daten
	private JTextField tfDatum = new JTextField();
	private JTextField tfNachname = new JTextField();
	private JTextField tfVorname = new JTextField();
	private JTextField tfStrasse = new JTextField();
	private JTextField tfPlz = new JTextField();
	private JTextField tfOrt = new JTextField();
	
	// Beschriftungen
	private JLabel ueberschrift = new JLabel("Reisekostenabrechnung");
	/** Label um Statusmeldungen anzuzeigen */
	private JLabel meldungen = new JLabel(" ");
	
	// Elementarrays
	/** Array um die Texftfelder Auslesen zu koennen */
	private JTextField[] mitarbeierEingaben = {tfNachname, tfVorname, tfStrasse, tfPlz, tfOrt};
	/** Array um die Rechte Menueliste zu erzeugen */
	private JComponent[] menueElemente = {new JLabel(""), new JLabel(""), new JLabel(""), new JLabel("Mitarbeiter: "), 
										  cbMitarbeiter, btnSelect, btnRemove, new JLabel(""), btnSave, btnCancel};
	/** Erzeugt die Formular Elemente fuer die Mitarbeiter */
	private JComponent[] mitarbeiterElemente = {new JLabel("Nachname :"), tfNachname, new JLabel("Vorname :"), tfVorname,
											    new JLabel("Strasse :"), tfStrasse, new JLabel(""), new JLabel(""),
											    new JLabel("PLZ :"), tfPlz, new JLabel("Ort :"), tfOrt, 
											    new JLabel(""), new JLabel(""), new JLabel(""), new JLabel("")};
	/** Beinhaltet die TabellenUeberschriften */
	private String[] SpaltenUeberschriften = {"Kostenart", "Anzahl", "Einzelverguetung", "Gesamtverguetung"};
	/** Beinhaltet die Tabellen Zellen mit denen die Tabelle am anfang erzeugt wird */
	private String[][] tabellenDaten = new String[4][4];
	
	// Initialiesierung des Tabellen Models
	// um von ausden draufzugreifen zu koennen
	private DefaultTableModel model;
	
	
	//========================
	// Konstruktor
	//========================
	public MainPanel() {
		// DB-Treiber registrieren
		try {
			Class.forName(Config.TREIBER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Erzeugt am Anfang alle MA aus der DB und fuellt die
		// Combobox
		DBini.erzeugeMitarbeiter();
		HilfsMethoden.fuelleCombobox(cbMitarbeiter, Mitarbeiter.mitarbeiter);
		// Holt die Kostenart und die Einzelverguetung aus der DB um
		// damit die Tabelle damit gefuellt werden kann
		DBini.holeKostenart(tabellenDaten);
		
		// Setzt das Haupt-Lauout mit Rahmen
		setLayout(new BorderLayout(15, 15));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		/**
		 * Erzeugt das tabellen Model und setzt nur die 2te
		 * Spalte auf Editierbar mit einer Annonymen Methode
		 */
		model = new DefaultTableModel(tabellenDaten, SpaltenUeberschriften) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // da die Tabellen Indexe nullbasiert sind muss man 1 einsetzen
                return column == 1;
            }
        };
        
        /**
         * Fuegt der Tabelle einen Tabellen Listener Hinzu damit auf 
         * Benutzer eingaben Reagiert werden kann ebenfals als Anonyme Methode
         */
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Prüfe, ob die Änderung ein "UPDATE" war und in Spalte 2 (Index 1) stattfand
                if (e.getType() == TableModelEvent.UPDATE) {  
                	int row = 0;
					try {
						// Hole die betroffene Zeile in der
						// die Zelle bearbeitet wurde
	                    row = e.getFirstRow();
	                    
	                    // Hole die eingegebene Zahl und Einzelverguetung
	                    // aus der Tabelle
	                    String anzahl = model.getValueAt(row, 1).toString().trim();
	                    String multiplikator = model.getValueAt(row, 2).toString();
	                    int zahl;
	                    
	                    // wenn die eingabe erfolgreiche Geparsed wurde wurd Uberpruft
	                    // ob es eine Positive zahl Groesser als 0 ist
	                    // ansonsten Kommt eine fehlermeldung
						zahl = Integer.parseInt(anzahl);
						if(zahl < 0) {
							model.removeTableModelListener(this);
							model.setValueAt(0, row, 1);
							model.addTableModelListener(this);
							meldungen.setForeground(Color.RED);
							meldungen.setText("Anzahl muss Groeser oder gleich 0 sein");
							return;
	                    }
						
						// Speichert die Konstenart und die Anzahl im Array
						// fuer die weiterverarbeitung
						MainPanel.kostenartAnzahl[row][0] = row + 1;
	                    MainPanel.kostenartAnzahl[row][1] = zahl;
	                    
	                    // Parsed die Einzelverguetung in einen Double Wert
	                    double faktor = Double.parseDouble(multiplikator); 
	                    // Berechnet die Gesamtverguetung als der Anzahl und 
	                    // der gesamtverguetung
	                    double ergebniss = zahl * faktor;
	                    // Speichert den Wert im Array zur weiterverarbeitung
	                    MainPanel.gesamtVerguetung[row] = ergebniss;
	                    
	                    // Setzt den Erechneten Wer in die 4te Spalte, Nach dem
	                    // der Listener abgeschlatet wurde da ansonsten dies als Update
	                    // Registriert wird
	                    model.removeTableModelListener(this); // 'this' bezieht sich auf den aktuellen Listener
	                    // Wert wird mit 2 Stellen nach dem Komma Formatiert
	                    model.setValueAt(String.format("%.2f",ergebniss), row, 3);
	                    // Nach dem Einsetzen des wertes wird der Listener wieder Aktiviert
	                    model.addTableModelListener(this);
					} catch (NumberFormatException e1) {
						model.removeTableModelListener(this);
						model.setValueAt(0, row, 1);
						model.addTableModelListener(this);
						meldungen.setForeground(Color.RED);
						meldungen.setText("Gebe eine ganzzahl ein");
					}
                }
            }
        });
        
        // Erzeugt eine neue Tabelle aus dem Model und setzt die Entsprechenden
        // einstellungen fuers Aussehen
		JTable table = new JTable(model);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		table.getColumnModel().getColumn(1).setMaxWidth(70);
		table.setRowHeight(25);
		table.setFont(new Font("Arial", Font.PLAIN, 12));
		// Erzeugt einen neuen Zellen Renderer um dieWerte der 2 - 4 Spalte zu Zentrieren
		DefaultTableCellRenderer zentriert = new DefaultTableCellRenderer();
		zentriert.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(1).setCellRenderer(zentriert);
		table.getColumnModel().getColumn(2).setCellRenderer(zentriert);
		table.getColumnModel().getColumn(3).setCellRenderer(zentriert);
		// Erzeugt einen 'Container' in dem der Tabellen-Koerper
		// hinnein gelegt wird
		JScrollPane scrollPane = new JScrollPane(table);
		
		// erzeugt weitere Panel in denen die einzelnen Elemente des
		// Formulars hineingelegt werden
		JPanel center = new JPanel(new GridLayout(3, 1, 5, 5));
		JPanel east = new JPanel(new GridLayout(12, 1, 5, 5));
		JPanel kopf = new JPanel(new GridLayout(4, 4, 5, 5));
		JPanel maDaten = new JPanel(new GridLayout(4, 4, 5, 5));
		JPanel fuss = new JPanel(new BorderLayout(15, 15));
		
		// fuegt das Datumsfeld und das Label dem kopf Panel hinzu
		// der Rest des Grid-Layouts wird mit Leer-Labels aufgefuellt
		for(int i = 0; i < 16; i++) {
			
			if(i == 6) {
				JLabel datum = new JLabel("Datum :");
				datum.setHorizontalAlignment(JLabel.RIGHT);
				kopf.add(datum);
			} else if(i == 7) {				
				kopf.add(tfDatum);
			} else {				
				kopf.add(new JLabel(""));
			}
		}
		
		// Fuegt die Mitarbeiter Formular Elemente dem Panel hinzu
		for(JComponent element: mitarbeiterElemente) {
			if(element instanceof JLabel) {
				((JLabel) element).setHorizontalAlignment(JLabel.RIGHT);
			}
			maDaten.add(element);
		}
		
		// Fuegt die Menue Elemente dem Rechten Panel hinzu
		for(JComponent element: menueElemente) {
			if(element instanceof JButton) {
				((JButton) element).addActionListener(this);
			}
			east.add(element);
		}
		
		// Fuegt die Tabelle in das Verschachtelte Border-Layout hinzu
		fuss.add(BorderLayout.NORTH, table.getTableHeader());
		fuss.add(BorderLayout.CENTER, scrollPane);
		
		// Setzt die Textfelder und setzt die Pannel in die Jeweiligen Zonen
		// des Haupt Border-Layouts Hinzu
		ueberschrift.setHorizontalAlignment(JLabel.CENTER);
		add(BorderLayout.NORTH, ueberschrift);
		center.add(kopf);
		center.add(maDaten);
		center.add(fuss);
		add(BorderLayout.CENTER, center);
		add(BorderLayout.EAST, east);
		meldungen.setHorizontalAlignment(JLabel.CENTER);
		add(BorderLayout.SOUTH, meldungen);
		
		// Nullt einmal zu anfang die Tabelle aus
		tabelleAusnullen();
	}

	//========================
	// Ueberschriebene Methode
	//========================
	@Override
	public void actionPerformed(ActionEvent e) {
		// Wenn man auf 'Abbrechen' klickt wird das Formular zurueckgesetzt
		if(e.getSource() == btnCancel) {
			formularLeeren();
			meldungen.setForeground(Color.BLACK);
			meldungen.setText("Daten zurueckgesetzt");
		} else if(e.getSource() == btnSelect) {
			// Gibt eine Fehlermeldung wenn kein MA ausgewahlt ist
			if(cbMitarbeiter.getSelectedItem() == null) {
				meldungen.setForeground(Color.RED);
				meldungen.setText("Kein Mitarbeiter ausgeweahlt");
			} else {
				// Setzt die Daten des Mitarbeiter anhand des Ausgewaehlten MA's
				Mitarbeiter ausgewaehlt = (Mitarbeiter) cbMitarbeiter.getSelectedItem();
				tfNachname.setText(ausgewaehlt.getNachname());
				tfVorname.setText(ausgewaehlt.getVorname());
				tfStrasse.setText(ausgewaehlt.getStrasse());
				tfPlz.setText(ausgewaehlt.getPlz() + "");
				tfOrt.setText(ausgewaehlt.getOrt());
				meldungen.setForeground(Color.BLACK);
				meldungen.setText("Mitarbeiter Auswgeweahlt");
			}
		} else if(e.getSource() == btnRemove) {
			// Setzt den Ausgeweahlten Mitarbeiter zurueck
			// und leer die Formularfelder des Mitarbeiters
			cbMitarbeiter.setSelectedItem(null);
			maDatenLeeren();
			meldungen.setForeground(Color.BLACK);
			meldungen.setText("Daten zurueckgesetzt");
		} else if(e.getSource() == btnSave) {
			// Holt alle eingegeben Mitarbeiter Daten und Speiert sie im Array
			for(int i = 0; i < mitarbeierEingaben.length; i++) {
				maDaten[i] = mitarbeierEingaben[i].getText().trim();
			}
			
			// Urbeerprueft an eines Regulaeren ausdrucks ob das eingegebene
			// Datum Gueltig ist
			if(!tfDatum.getText().trim().matches(Config.DATE_VALIDATION)) {
				meldungen.setForeground(Color.RED);
				meldungen.setText("Gebe gueltiges Datum ein!! Format: 02.10.2025");
				return;
			}
			
			// Parsed die plz und Ueberprueft ob sie im entsprechenden Bereich ist
			try {
				if(Integer.parseInt(tfPlz.getText().trim()) < 1000 || Integer.parseInt(tfPlz.getText().trim()) > 99999) {
					meldungen.setForeground(Color.RED);
					meldungen.setText("Postleitzahl muss zwischen 1000 und 99999 liegen");
					return;
				}
			} catch (NumberFormatException nfe) {
				meldungen.setForeground(Color.RED);
				meldungen.setText("PLZ Darf nur aus Zahlen bestehen");
				return;
			}
			
			// Prueft on eine Feld der MA Daten leer ist und gibt dann eine 
			// Fehlermeldung aus
			for(String s : maDaten) {
				if(s.isBlank()) {
					meldungen.setForeground(Color.RED);
					meldungen.setText("Bitte Alle Mitarbeiter Daten Ausfuellen");
					return;			
				}
			}
			
			// Ueberprueft ob der mitarbeiter schon existiert in der DB
			// anhand des Mitarbeiter Arrays
			Mitarbeiter ausgewahterMA = HilfsMethoden.exsistMitarbeiter(maDaten);
			int pers_id = 0;
			// Wenn er nicht Existiert wird er neu angelegt und die erzeugt ID 
			// zur verarbietung Gespeichert
			if(ausgewahterMA == null) {
				pers_id = DBini.speichereMitarbeiter(maDaten);
			} else {
				// wenn er beites Existiert wird die ID geholt
				pers_id = ausgewahterMA.getPers_id();
			}
			
			// DAS eingegebene Datum wird in ein SQL Datum umgewandelt
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Config.DATUM_FORMAT);
			LocalDate localDate = LocalDate.parse(tfDatum.getText().trim(), formatter);
			Date sqlDate = Date.valueOf(localDate);
			
			// es werden das SQL-Datum, die ID das Array mit der Kostenart und der Anzahl und 
			// die erechntete Gesamt verguetung uebergeben und als Datensaetze in die DB geschriben
			DBini.schreibeKostenart(sqlDate, pers_id, kostenartAnzahl, gesamtVerguetung);
			
			// zum Schluss wird das Formular zurueckgesetzt und eine Erfolgsmeldung ausgegeben
			formularLeeren();
			HilfsMethoden.fuelleCombobox(cbMitarbeiter, Mitarbeiter.mitarbeiter);			
			meldungen.setForeground(Color.BLACK);
			meldungen.setText("Daten Erfolgreich Gespeichert");
			
		}
		
	}
	//========================
	// Sonstige Methoden
	//========================
	/**
	 * Leer Alle Formular Elemente und<br>
	 * nullt die Tabelle aus
	 */
	private void formularLeeren() {
		tfDatum.setText(" ");
		maDatenLeeren();
		tabelleAusnullen();		
	}

	/**
	 * Loescht alle Mitarbeiter  Daten aus den Textfeldern
	 */
	private void maDatenLeeren() {
		for(JTextField jt: mitarbeierEingaben) {
			jt.setText(" ");
		}
	}
	
	/**
	 * nullt die Tabelle aus
	 */
	private void tabelleAusnullen() {
		for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(0, i, 1);
        }
	}
}