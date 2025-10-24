package kosten;

import javax.swing.JFrame;

import kosten.view.MainPanel;

@SuppressWarnings("serial")
public class Main extends JFrame {

	// Erstellt das Panel mit der GUI
	public Main(String titel) {
		super(titel);
		setBounds(200, 200, 700, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		add(new MainPanel());
		setVisible(true);		
	}

	public static void main(String[] args) {
		new Main("Reisekosten");
	}

}
