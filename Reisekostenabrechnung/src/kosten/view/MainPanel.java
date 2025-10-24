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
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
	private static int[][] kostenartAnzahl = new int[4][2];
	private static double[] gesamtVerguetung = new double[4];
	private static String[] maDaten = new String[5];
	
	//========================
	// Eigenschaften
	//========================
	// JButtons
	private JButton btnSelect = new JButton("Auswehlen");
	private JButton btnRemove = new JButton("Abwaehlen");
	private JButton btnSave = new JButton("Speichern");
	private JButton btnCancel = new JButton("Abbrechen");
	
	//JComboBox
	@SuppressWarnings("rawtypes")
	private JComboBox cbMitarbeiter = new JComboBox();
	
	// Textfelder
	private JTextField tfDatum = new JTextField();
	private JTextField tfNachname = new JTextField();
	private JTextField tfVorname = new JTextField();
	private JTextField tfStrasse = new JTextField();
	private JTextField tfPlz = new JTextField();
	private JTextField tfOrt = new JTextField();
	
	// Beschriftungen
	private JLabel ueberschrift = new JLabel("Reisekostenabrechnung");
	private JLabel meldungen = new JLabel(" ");
	
	// Elementarrays
	private JTextField[] mitarbeierEingaben = {tfNachname, tfVorname, tfStrasse, tfPlz, tfOrt};
	private JComponent[] menueElemente = {new JLabel(""), new JLabel(""), new JLabel(""), new JLabel("Mitarbeiter: "), 
										  cbMitarbeiter, btnSelect, btnRemove, new JLabel(""), btnSave, btnCancel};
	private JComponent[] mitarbeiterElemente = {new JLabel("Nachname :"), tfNachname, new JLabel("Vorname :"), tfVorname,
											    new JLabel("Strasse :"), tfStrasse, new JLabel(""), new JLabel(""),
											    new JLabel("PLZ :"), tfPlz, new JLabel("Ort :"), tfOrt, 
											    new JLabel(""), new JLabel(""), new JLabel(""), new JLabel("")};
	private String[] SpaltenUeberschriften = {"Kostenart", "Anzahl", "Einzelverguetung", "Gesamtverguetung"};
	private Object[][] tabellenDaten = new String[4][4];
	
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
		DBini.erzeugeMitarbeiter();
		HilfsMethoden.fuelleCombobox(cbMitarbeiter, Mitarbeiter.mitarbeiter);
		DBini.holeKostenart(tabellenDaten);
		
		setLayout(new BorderLayout(15, 15));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
		model = new DefaultTableModel(tabellenDaten, SpaltenUeberschriften) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // nur die Zweite Spalte Editierbar machen
                return column == 1;
            }
        };
        
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Prüfe, ob die Änderung ein "UPDATE" war und in Spalte 2 (Index 1) stattfand
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
                    // Hole die betroffene Zeile
                    int row = e.getFirstRow();
                    
                    // Hole den neuen Wert aus Spalte 2
                   
                    String anzahl = model.getValueAt(row, 1).toString();
                    String multiplikator = model.getValueAt(row, 2).toString();
                    int zahl = Integer.parseInt(anzahl);
                    MainPanel.kostenartAnzahl[row][0] = row + 1;
                    MainPanel.kostenartAnzahl[row][1] = zahl;
                    double faktor = Double.parseDouble(multiplikator);                    
                    double ergebniss = zahl * faktor;
                    MainPanel.gesamtVerguetung[row] = ergebniss;
                    
                    // Setze den Wert in Spalte 4 (Index 3)
                    // WICHTIG: Wir müssen den Listener kurz entfernen,
                    // damit diese Änderung nicht wieder ein Event auslöst!
                    model.removeTableModelListener(this); // 'this' bezieht sich auf den aktuellen Listener
                    model.setValueAt(ergebniss, row, 3);
                    model.addTableModelListener(this); // Listener wieder hinzufügen
                }
            }
        });
		JTable table = new JTable(model);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setRowHeight(25);
		table.setFont(new Font("Arial", Font.PLAIN, 12));
		JScrollPane scrollPane = new JScrollPane(table);
		
		JPanel center = new JPanel(new GridLayout(3, 1, 5, 5));
		JPanel east = new JPanel(new GridLayout(12, 1, 5, 5));
		JPanel kopf = new JPanel(new GridLayout(4, 4, 5, 5));
		JPanel maDaten = new JPanel(new GridLayout(4, 4, 5, 5));
		JPanel fuss = new JPanel(new BorderLayout(15, 15));
		
		for(int i = 0; i < 16; i++) {
			if(i == 6) {
				kopf.add(new JLabel("Datum :"));
			} else if(i == 7) {
				kopf.add(tfDatum);
			} else {
				kopf.add(new JLabel(""));
			}
		}
		for(JComponent element: mitarbeiterElemente) {
			maDaten.add(element);
		}
		for(JComponent element: menueElemente) {
			if(element instanceof JButton) {
				((JButton) element).addActionListener(this);
			}
			east.add(element);
		}
		fuss.add(BorderLayout.NORTH, table.getTableHeader());
		fuss.add(BorderLayout.CENTER, scrollPane);
		
		ueberschrift.setHorizontalAlignment(JLabel.CENTER);
		add(BorderLayout.NORTH, ueberschrift);
		center.add(kopf);
		center.add(maDaten);
		center.add(fuss);
		add(BorderLayout.CENTER, center);
		add(BorderLayout.EAST, east);
		meldungen.setHorizontalAlignment(JLabel.CENTER);
		add(BorderLayout.SOUTH, meldungen);
		
		tabelleAusnullen();
	}

	//========================
	// Ueberschriebene Methode
	//========================
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnCancel) {
			formularLeeren();
			meldungen.setForeground(Color.BLACK);
			meldungen.setText("Daten zurueckgesetzt");
		} else if(e.getSource() == btnSelect) {
			if(cbMitarbeiter.getSelectedItem() == null && !tfNachname.getText().isBlank()) {
				maDatenLeeren();
				meldungen.setForeground(Color.BLACK);
				meldungen.setText("Mitarbeiter Abwgeweahlt");
			} else if(cbMitarbeiter.getSelectedItem() == null) {
				meldungen.setForeground(Color.RED);
				meldungen.setText("Kein Mitarbeiter ausgeweahlt");
			} else {
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
			cbMitarbeiter.setSelectedItem(null);
			maDatenLeeren();
			meldungen.setForeground(Color.BLACK);
			meldungen.setText("Daten zurueckgesetzt");
		} else if(e.getSource() == btnSave) {
			int i = 0;
			for(i = 0; i < mitarbeierEingaben.length; i++) {
				maDaten[i] = mitarbeierEingaben[i].getText();
			}
			if(!tfDatum.getText().matches(Config.DATE_VALIDATION)) {
				meldungen.setForeground(Color.RED);
				meldungen.setText("Gebe gueltiges Datum ein!! Format: 02.10.2025");
				return;
			}
			try {
				if(Integer.parseInt(tfPlz.getText()) < 1000 && Integer.parseInt(tfPlz.getText()) > 9999) {
					meldungen.setForeground(Color.RED);
					meldungen.setText("Postleitzahl muss zwischen 1000 und 9999 liegen");
					return;
				}
			} catch (NumberFormatException nfe) {
				meldungen.setForeground(Color.RED);
				meldungen.setText("Darf nur aus Zahlen bestehen");
				return;
			}
			for(String s : maDaten) {
				if(s.isBlank()) {
					meldungen.setForeground(Color.RED);
					meldungen.setText("Bitte Alle Mitarbeiter Daten Ausfuellen");
					return;			
				}
			}
			for(i = 0; i > kostenartAnzahl.length; i++) {
				if(kostenartAnzahl[i][1] < 0) {
					meldungen.setForeground(Color.RED);
					meldungen.setText("Die Anzahl der Kostenart darf nicht kleiner als 0 sein");
					return;
				}
			}
			
			Mitarbeiter ausgewahterMA = HilfsMethoden.exsistMitarbeiter(maDaten);
			int pers_id = 0;
			if(ausgewahterMA == null) {
				pers_id = DBini.speichereMitarbeiter(maDaten);
			} else {
				pers_id = ausgewahterMA.getPers_id();
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Config.DATUM_FORMAT);
			LocalDate localDate = LocalDate.parse(tfDatum.getText(), formatter);
			Date sqlDate = Date.valueOf(localDate);
			
			DBini.schreibeKostenart(sqlDate, pers_id, kostenartAnzahl, gesamtVerguetung);
			
			formularLeeren();
			HilfsMethoden.fuelleCombobox(cbMitarbeiter, Mitarbeiter.mitarbeiter);
			
			meldungen.setForeground(Color.BLACK);
			meldungen.setText("Daten Erfolgreich Gespeichert");
			
		}
		
	}
	//========================
	// Sonstige Methoden
	//========================
	private void formularLeeren() {
		tfDatum.setText(" ");
		maDatenLeeren();
		tabelleAusnullen();		
	}

	private void maDatenLeeren() {
		for(JTextField jt: mitarbeierEingaben) {
			jt.setText(" ");
		}
	}
	
	private void tabelleAusnullen() {
		for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(0, i, 1);
        }
	}
}
