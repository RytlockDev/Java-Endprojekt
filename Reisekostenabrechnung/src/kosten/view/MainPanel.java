package kosten.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class MainPanel extends JPanel implements ActionListener {
	//========================
	// Eigenschaften
	//========================
	// JButtons
	private JButton btnSelect = new JButton("Auswehlen");
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
	private JComponent[] menueBar = {new JLabel(""), new JLabel(""), new JLabel(""), new JLabel("Mitarbeiter: "), cbMitarbeiter, btnSelect, new JLabel(""), new JLabel(""),
								    btnSave, btnCancel};
	private JComponent[] mitarbeiterDaten = {new JLabel("Nachname :"), tfNachname, new JLabel("Vorname :"), tfVorname,
											 new JLabel("Strasse :"), tfStrasse, new JLabel(""), new JLabel(""),
											 new JLabel("PLZ :"), tfPlz, new JLabel("Ort :"), tfOrt, 
											 new JLabel(""), new JLabel(""), new JLabel(""), new JLabel("")};
	private String[] columNames = {"Kostenart", "Anzahl", "Einzelverguetung", "Gesamtverguetung"};
	private Object[][] tableData = new String[4][4];
	private static int[][] kostenartAnzahl = new int[4][2];
	private static double[] gesamtVerguetung = new double[4];
	
	
	public MainPanel() {
		// DB-Treiber registrieren
		try {
			Class.forName(Config.TREIBER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		DBini.erzeugeMitarbeiter();
		cbMitarbeiter = HilfsMethoden.fuelleCombobox(cbMitarbeiter, Mitarbeiter.mitarbeiter);
		DBini.holeKostenart(tableData);
		
		setLayout(new BorderLayout(15, 15));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
		DefaultTableModel model = new DefaultTableModel(tableData, columNames) {
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
		for(JComponent element: mitarbeiterDaten) {
			maDaten.add(element);
		}
		for(JComponent element: menueBar) {
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnCancel) {
			maDatenLeeren();
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
		} else if(e.getSource() == btnSave) {
			int pers_id;
			java.util.Date datum;
			String[] maDaten = {
					tfNachname.getText(),
					tfVorname.getText(),
					tfStrasse.getText(),
					tfPlz.getText(),
					tfOrt.getText(),
			};
			for(String a: maDaten) {
				if(a.isBlank()) {
					meldungen.setForeground(Color.RED);
					meldungen.setText("Bitte Mitarbeiter Daten Ausfuellen");
				}
			}
			
			
			
			pers_id = HilfsMethoden.holeID(maDaten);
			if(pers_id == -1) {
				 DBini.speichereMitarbeiter(maDaten);
				 pers_id = HilfsMethoden.holeID(maDaten);
				
			}
			
			SimpleDateFormat inputFormat =  new SimpleDateFormat(Config.DATUM_FORMAT);
			
			try {
				datum = inputFormat.parse(tfDatum.getText());
				java.sql.Date sqlDate = new java.sql.Date(datum.getTime());
				DBini.schreibeKostenart(pers_id, sqlDate, 
							    	MainPanel.kostenartAnzahl, 
							    	MainPanel.gesamtVerguetung);
			} catch (ParseException e1) {
				meldungen.setForeground(Color.RED);
				meldungen.setText("Bitte Gueltiges Datum eingeben, Format: " + Config.DATUM_FORMAT);
			}
				
			
		}
		
	}

	private void maDatenLeeren() {
		tfNachname.setText(" ");
		tfVorname.setText(" ");
		tfStrasse.setText(" ");
		tfPlz.setText(" ");
		tfOrt.setText(" ");		
	}
}
