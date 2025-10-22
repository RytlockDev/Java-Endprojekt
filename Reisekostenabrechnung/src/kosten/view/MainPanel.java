package kosten.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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
	private JButton btnSave = new JButton("Speichern");
	private JButton btnCancel = new JButton("Abbrechen");
	
	@SuppressWarnings("rawtypes")
	private JComboBox cbMitarbeiter = new JComboBox();
	
	// Textfelder
	private JTextField tfDatum = new JTextField();
	private JTextField tfNachname = new JTextField();
	private JTextField tfVorname = new JTextField();
	private JTextField tfStrasse = new JTextField();
	private JTextField tfPlz = new JTextField();
	private JTextField tfOrt = new JTextField();
	private JTextField tfUser = new JTextField();
	
	// Beschriftungen
	private JLabel ueberschrift = new JLabel("Reisekostenabrechnung");
	private JLabel meldungen = new JLabel(" ");
	private JLabel lTagesgeld = new JLabel(" ");
	private JLabel lUebernachtung = new JLabel(" "); 
	private JLabel lFahrkosten = new JLabel(" "); 
	private JLabel lSonstiges = new JLabel(" "); 
	
	// Elementarrays
	private String[][] kostenart = new String[4][2];
	private JComponent[] eastBar = {new JLabel("Mitarbeiter: "), cbMitarbeiter, new JLabel(""), new JLabel(""),
								    btnSave, btnCancel};
	private JComponent[] mitarbeiterDaten = {new JLabel("Nachname :"), tfNachname, new JLabel("Vorname :"), tfVorname,
											 new JLabel("Strasse :"), tfStrasse, new JLabel(""), new JLabel(""),
											 new JLabel("PLZ :"), tfPlz, new JLabel("Ort :"), tfOrt, 
											 new JLabel(""), new JLabel(""), new JLabel(""), new JLabel("")};
	private String[] columNames = {"Kostenart", "Anzahl", "Einzelverguetung", "Gesamtverguetung"};
	private Object[][] data = {
			{new JLabel("1"), "", new JLabel("5"), lTagesgeld},
			{new JLabel("2"), "", new JLabel("6"), lUebernachtung},
			{new JLabel("3"), "", new JLabel("7"), lFahrkosten},
			{new JLabel("4"), "", new JLabel("8"), lSonstiges},
	};
	
	public MainPanel() {
		// DB-Treiber registrieren
		try {
			Class.forName(Config.TREIBER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		DBini.erzeugeMitarbeiter();
		cbMitarbeiter = HilfsMethoden.fuelleCombobox(cbMitarbeiter, Mitarbeiter.mitarbeiter);
		DBini.holeKostenart(kostenart);
		
		setLayout(new BorderLayout(15, 15));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JTable table = new JTable(data, columNames);
		DefaultCellEditor cellEditor = new DefaultCellEditor(tfUser);
		TableColumn column = table.getColumnModel().getColumn(1);
		column.setCellEditor(cellEditor);
		
		JPanel center = new JPanel(new GridLayout(3, 1, 5, 5));
		JPanel east = new JPanel(new GridLayout(16, 1, 5, 5));
		JPanel kopf = new JPanel(new GridLayout(4, 4, 5, 5));
		JPanel maDaten = new JPanel(new GridLayout(4, 4, 5, 5));
		JPanel fuss = new JPanel(new BorderLayout(15, 15));
		
		for(int i = 0; i < 16; i++) {
			if(i == 10) {
				kopf.add(new JLabel("Datum :"));
			} else if(i == 11) {
				kopf.add(tfDatum);
			} else {
				kopf.add(new JLabel(""));
			}
		}
		for(JComponent element: mitarbeiterDaten) {
			maDaten.add(element);
		}
		for(JComponent element: eastBar) {
			if(element instanceof JButton) {
				((JButton) element).addActionListener(this);
			}
			east.add(element);
		}
		for(JComponent element: mitarbeiterDaten) {
			maDaten.add(element);
		}
		fuss.add(BorderLayout.NORTH, table.getTableHeader());
		fuss.add(BorderLayout.CENTER, table);
		
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
		// TODO Auto-generated method stub
		
	}
}
