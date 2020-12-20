package interface_graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interface_graphic.Window.ButtonNumberPlayer;
import jeu.Partie;
import jeu.Territoire;

public class ButtonTerritoire extends JButton{
	
	private Territoire territoire;
	private Partie partie;
	
	public ButtonTerritoire(Territoire territoire,Partie partie) {
		super();
		this.partie=partie;
		this.territoire=territoire;
		this.setText(this.toString());
		this.setEnabled(false);
		territoire.setJouer(false);
	}
	
	
	public Territoire getTerritoire() {
		return territoire;
	}



	public String toString() {
		if(territoire.getExist()) // si le territoire existe 
			return "<html>"+partie.getJoueurID(territoire.getJoueurID()).getPseudo()+"<br> Dé :"+territoire.getNbDe()+"</html>";
		// si le territoire existe pas
		this.setBackground(Color.black);
		this.setEnabled(false);
		return "";
		

	}


	public void majText() {
		this.setText(this.toString());
		
	}
	
	

}
