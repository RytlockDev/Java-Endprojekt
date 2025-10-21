package kosten;

import javax.swing.JFrame;

import kosten.util.DBini;
import kosten.view.MainPanel;

@SuppressWarnings("serial")
public class Main extends JFrame {

	public Main(String titel) {
		super(titel);
		DBini.erzeugeMitarbeiter();
		setBounds(200, 200, 400, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		add(new MainPanel());
		setVisible(true);		
	}

	public static void main(String[] args) {
		new Main("Reisekosten");
	}

}
