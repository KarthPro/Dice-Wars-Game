package interface_graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import jeu.Jeu;
import jeu.Joueur;
import jeu.NotFoundException;
import jeu.Partie;
import jeu.Territoire;
import jeu.TypeJoueur;


public class Window extends JFrame implements Serializable{
	
	private Partie partie;
	
	
	public Window(){
		super();
		this.setTitle("Dice Wars");  
		this.setSize(1400,900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		menuDemarage();//On initialise notre fenêtre 
		}
	  

		public void menuDemarage(){  // menu de démarage
			

			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().setBackground(Color.black);
			JLabel labelDiceWars=new JLabel("DICE WARS");
			Font fontDiceWars = new Font("Impact",Font.BOLD,240);
			labelDiceWars.setFont(fontDiceWars);
			labelDiceWars.setHorizontalAlignment(JLabel.CENTER);
			labelDiceWars.setForeground(Color.darkGray);
		    this.getContentPane().add(labelDiceWars, BorderLayout.NORTH);
		    
			Font fontButton = new Font("Impact",Font.BOLD,50);
			
		    JButton buttonNewGame = new JButton("Lancer une nouvelle partie");
		    buttonNewGame.setFont(fontButton);
		    buttonNewGame.setHorizontalAlignment(JLabel.CENTER);
		    buttonNewGame.setBackground(Color.DARK_GRAY);
		    buttonNewGame.setForeground(Color.LIGHT_GRAY);
		    buttonNewGame.addActionListener(new ButtonNewGameListener());

		    this.getContentPane().add(buttonNewGame, BorderLayout.WEST);
		    
		    JButton buttonContinueGame = new JButton("       Continuer la partie       ");
		    buttonContinueGame.setFont(fontButton);
		    buttonContinueGame.setHorizontalAlignment(JLabel.CENTER);
		    buttonContinueGame.setBackground(Color.DARK_GRAY);
		    buttonContinueGame.setForeground(Color.LIGHT_GRAY);
		    buttonContinueGame.addActionListener(new ButtonContinueGame(buttonContinueGame));
		    this.getContentPane().add(buttonContinueGame, BorderLayout.EAST);
		    
		    repaint();
		    revalidate();

		}
		
		public void initialiserCarte() throws NotFoundException{ // initalisation de carte, appelé lors de la création de la partie

			getContentPane().removeAll();
			
		    this.getContentPane().setLayout(new FlowLayout());
		    this.getContentPane().setBackground(Color.black);
			

			JPanel panelCarte = new JPanel();
			panelCarte.setBackground(Color.DARK_GRAY);
			panelCarte.setPreferredSize(new Dimension(950, 850));
			GridLayout gl = new GridLayout(partie.getCarte().getLongueur(), partie.getCarte().getLargeur());
			gl.setHgap(10);
			gl.setVgap(10);
			panelCarte.setLayout(gl);
			for(int i=0;i<(partie.getCarte().getLargeur()*partie.getCarte().getLongueur());i++) {
				panelCarte.add(new ButtonTerritoire(partie.getCarte().rechercherTerritoire(i),partie));
			}
		    
		
			
			Font FontPanelAffichage= new Font("Impact",Font.BOLD,30);
			
			JPanel panelAffichage = new JPanel();
			panelAffichage.setLayout(new FlowLayout());
			panelAffichage.setBackground(Color.DARK_GRAY);
			panelAffichage.setPreferredSize(new Dimension(400, 850));
			JLabel labelMessageInfoJoueur = new JLabel("<html><br>Tour du joueur : "+partie.getJeu().getJoueur().getPseudo()+"<br><br>Sélectionner le territoire attaquant</html>");
			labelMessageInfoJoueur.setPreferredSize(new Dimension(350, 400));
			labelMessageInfoJoueur.setOpaque(false);
			labelMessageInfoJoueur.setFont(FontPanelAffichage);
			labelMessageInfoJoueur.setForeground(partie.getJeu().getJoueur().getColor());
			panelAffichage.add(labelMessageInfoJoueur);
			
			JButton buttonFinTour=new JButton("Fin de tour");
			buttonFinTour.setFont(FontPanelAffichage);
			buttonFinTour.setPreferredSize(new Dimension(390, 150));
			buttonFinTour.addActionListener(new ButtonFinTour());
			buttonFinTour.setBackground(partie.getJeu().getJoueur().getColor());
			panelAffichage.add(buttonFinTour);
			
			JButton buttonSaveGame=new JButton("Enregistrer la partie");
			buttonSaveGame.setFont(FontPanelAffichage);
			buttonSaveGame.setPreferredSize(new Dimension(390, 150));
			buttonSaveGame.addActionListener(new ButtonSaveGame());
			//buttonFinTour.set
			panelAffichage.add(buttonSaveGame);
			
			JButton buttonLeaveGame=new JButton("Quitter la partie");
			buttonLeaveGame.setFont(FontPanelAffichage);
			buttonLeaveGame.setPreferredSize(new Dimension(390, 150));
			buttonLeaveGame.addActionListener(new ButtonLeaveGame());
			//buttonFinTour.set
			panelAffichage.add(buttonLeaveGame);
			
			this.getContentPane().add(panelCarte);
		    this.getContentPane().add(panelAffichage);
		    
		    activerTerritoireJoueurCourrant();
		    majAffichageButtonTerritoire();

		    
		    this.getContentPane().repaint();
		    this.getContentPane().revalidate();
		    
		    if(!partie.getJeu().getJoueur().getTypeJoueur().equals(TypeJoueur.HUMAIN)) {
		    	tourIA();
		    }

		    
		}
		





		
		public void tourIA() { // appelé lorsque le l'IA joue
	    	  ((JLabel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).setForeground(partie.getJeu().getJoueur().getColor());
	    	  ((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(1)).setBackground(partie.getJeu().getJoueur().getColor());
			if(partie.getJeu().getJoueur().getTypeJoueur().equals(TypeJoueur.IA_Facile)) {	   
				partie.getJeu().getJoueur().selectionIA(partie.getJeu());
			}
			if(partie.getJeu().getJoueur().getTypeJoueur().equals(TypeJoueur.IA_Moyen)) {
				int count=0;
				while(count<3&&partie.getJeu().getJoueur().selectionIA(partie.getJeu())) {
					count++;
				}
			}
			if(partie.getJeu().getJoueur().getTypeJoueur().equals(TypeJoueur.IA_Difficile)) {
				int count=0;
				while(count<6&&partie.getJeu().getJoueur().selectionIA(partie.getJeu())) {
					count++;
					}

			}
			
			
		      boolean victory=partie.getJeu().getJoueur().finTour(partie.getJeu());
		      
		      if(victory){ // Si l'IA a gagner

			    	((JLabel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).setText("<html><br>L'IA "+partie.getJeu().getJoueur().getPseudo()+" <br><br>a gagner la partie</html>");
				    majAffichageButtonTerritoire();
				    desactiverAllTerritoire();
				    ((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(1)).setEnabled(false);
			   	   	((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(2)).setEnabled(false);
					  repaint();
					  revalidate();
			    	

		      }
		      else {
		    	  partie.nextPlayer();
		  		  resetBooleanJouerTerritoire();
		  	    if(!partie.getJeu().getJoueur().getTypeJoueur().equals(TypeJoueur.HUMAIN)) {
			    	tourIA();

			    }
		  	    else {
			    	  ((JLabel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).setText("<html><br>Tour du joueur : "+partie.getJeu().getJoueur().getPseudo()+"<br><br>Sélectionner le territoire attaquant</html>");
			    	  ((JLabel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).setForeground(partie.getJeu().getJoueur().getColor());
			    	  ((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(1)).setBackground(partie.getJeu().getJoueur().getColor());
				      ((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(1)).setEnabled(true);
			   	   	  ((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(2)).setEnabled(true);
			    	  desactiverAllTerritoire();
					  majAffichageButtonTerritoire();
					  activerTerritoireJoueurCourrant();
					  repaint();
					  revalidate();
		  	          }
		  	    }
		      

			}
		
		public void activerTerritoireJoueurCourrant() { // afficher les territoire qui peuvent attaquer du joueur courant humain

			ArrayList<Territoire> territoireJoueur=partie.getJeu().getJoueur().getTerritoires();
			for(int i=0;i<territoireJoueur.size();i++) {
				//if(!territoireJoueur.get(i).isJouer()) { // si le territoire a deja jouer
					if(territoireJoueur.get(i).getNbDe()>1) { // seul les territoire avec plus de 1 dé sont activé
						if(partie.getCarte().existeEnnemiTerritoireVoisin(territoireJoueur.get(i).getId())) { // si le territoire possède au moin un voisin ennemi
							((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(territoireJoueur.get(i).getId())).setEnabled(true);
							((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(territoireJoueur.get(i).getId())).addActionListener(new ButtonChoixTerritoireAttaquant(((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(territoireJoueur.get(i).getId()))));
						}
					}
				//}

				
			}
		}
		
		public void activerTerritoireJoueurCourrantIA() { // afficher les territoire qui peuvent attaquer du joueur courant si c'est une IA

			ArrayList<Territoire> territoireJoueur=partie.getJeu().getJoueur().getTerritoires();
			for(int i=0;i<territoireJoueur.size();i++) {
				//if(!territoireJoueur.get(i).isJouer()) { // si le territoire a deja jouer alors le bouton n'est pas activer
					if(territoireJoueur.get(i).getNbDe()>1) { // seul les territoire avec plus de 1 dé sont activé
						if(partie.getCarte().existeEnnemiTerritoireVoisin(territoireJoueur.get(i).getId())) { // si le territoire possède au moin un voisin ennemi
							((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(territoireJoueur.get(i).getId())).setEnabled(true);
						}
					}
				//}

				
			}
		}
		

		
		public void desactiverAllTerritoire() { // désactive les bouton de la carte
			for(int i=0;i<(partie.getCarte().getLargeur()*partie.getCarte().getLongueur());i++) {
				((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(i)).setEnabled(false);
				removeActionListenerJButton(((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(i)));
			}
		}
		
		public void majAffichageButtonTerritoire() { // met a jour l'affichge des territoires sur la carte
			for(int i=0;i<(partie.getCarte().getLargeur()*partie.getCarte().getLongueur());i++) {
				((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(i)).majText();
				if(((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(i)).getTerritoire().getExist())
					((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(i)).setForeground(partie.getJoueurID(((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(i)).getTerritoire().getJoueurID()).getColor());
			}
		}
		
		public void removeActionListenerJButton(JButton button) { // retire les action listener d'un territoire
			for(int i=0;i<button.getActionListeners().length;i++) {
				button.removeActionListener(button.getActionListeners()[i]);
			}
		}
		
		public void resetBooleanJouerTerritoire() { // non implémenter
			for(int i=0;i<(partie.getCarte().getLargeur()*partie.getCarte().getLongueur());i++) {
				((ButtonTerritoire)((JPanel) this.getContentPane().getComponent(0)).getComponent(i)).getTerritoire().setJouer(false);
			}
		}



		public Partie getPartie() {
			return partie;
		}


		public void setPartie(Partie partie) {
			this.partie = partie;
		}
		
		
		public void configueJoueur(int nbJoueur) {  // configuration d'un joueur
			this.getContentPane().removeAll();
			
			Font fontTitre= new Font("Impact",Font.BOLD,60);
			
		    this.getContentPane().setLayout(new FlowLayout());
		    this.getContentPane().setBackground(Color.black);
			
			JPanel panelTitre = new JPanel();
			JPanel panelForm = new JPanel();
			this.getContentPane().add(panelTitre);
			this.getContentPane().add(panelForm);
			
			panelTitre.setLayout(new BorderLayout());
			panelTitre.setBackground(Color.DARK_GRAY);
			panelTitre.setPreferredSize(new Dimension(1360,350));
			JLabel labelRemplir= new JLabel("Remplisser les informations du joueur : "+(nbJoueur+1)+"/"+partie.getJoueurs().size());
			labelRemplir.setFont(fontTitre);
			labelRemplir.setForeground(Color.LIGHT_GRAY);
			labelRemplir.setHorizontalAlignment(JLabel.CENTER);
			panelTitre.add(labelRemplir,BorderLayout.CENTER);
			
			Font fontForm= new Font("Impact",Font.PLAIN,15);

			
			

			panelForm.setBackground(Color.LIGHT_GRAY);
			panelForm.setPreferredSize(new Dimension(1360, 500));
			GridLayout gl = new GridLayout(4,9);
			gl.setHgap(5);
			gl.setVgap(5);
			panelForm.setLayout(gl);
			for(int i=0;i<(4*9);i++) {
				panelForm.add(new JPanel());
				((JPanel)this.getContentPane().getComponent(1)).getComponent(i).setBackground(Color.LIGHT_GRAY);
			}
			
			JLabel labelPseudo= new JLabel("Pseudo du joueur :");
			labelPseudo.setFont(fontForm);
			labelPseudo.setForeground(Color.DARK_GRAY);

			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(1)).add(labelPseudo);
			
			JLabel labelColor= new JLabel("Couleur du joueur :");
			labelColor.setFont(fontForm);
			labelColor.setForeground(Color.DARK_GRAY);
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(10)).add(labelColor);
			
			JLabel labelTypeJoueur= new JLabel("Type de joueur :");
			labelTypeJoueur.setFont(fontForm);
			labelTypeJoueur.setForeground(Color.DARK_GRAY);
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(19)).add(labelTypeJoueur);
			
			JTextField textFieldPseudo = new JTextField("");
			textFieldPseudo.setEditable(true);
			textFieldPseudo.setColumns(10);
			textFieldPseudo.setFont(fontForm);
			textFieldPseudo.setBackground(Color.white);
			textFieldPseudo.setForeground(Color.DARK_GRAY);
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(2)).add(textFieldPseudo);
			
			JRadioButton rouge = new JRadioButton("Rouge");
			rouge.setForeground(Color.DARK_GRAY);
			rouge.setBackground(Color.LIGHT_GRAY);
			rouge.setFont(fontForm);
			rouge.setSelected(true);
			
	        JRadioButton bleu = new JRadioButton("Bleu");
	        bleu.setForeground(Color.DARK_GRAY);
	        bleu.setBackground(Color.LIGHT_GRAY);
	        bleu.setFont(fontForm);
	        
	        JRadioButton vert = new JRadioButton("Vert");
	        vert.setForeground(Color.DARK_GRAY);
	        vert.setBackground(Color.LIGHT_GRAY);
	        vert.setFont(fontForm);
	        
			JRadioButton magenta = new JRadioButton("Magenta");
			magenta.setForeground(Color.DARK_GRAY);
			magenta.setBackground(Color.LIGHT_GRAY);
			magenta.setFont(fontForm);
			
	        JRadioButton jaune = new JRadioButton("Jaune");
	        jaune.setForeground(Color.DARK_GRAY);
	        jaune.setBackground(Color.LIGHT_GRAY);
	        jaune.setFont(fontForm);
	        
	        JRadioButton cyan = new JRadioButton("Cyan");
	        cyan.setForeground(Color.DARK_GRAY);
	        cyan.setBackground(Color.LIGHT_GRAY);
	        cyan.setFont(fontForm);
	        
	        ButtonGroup groupColor = new ButtonGroup();
	        groupColor.add(rouge);
	        groupColor.add(bleu);
	        groupColor.add(vert);
	        groupColor.add(magenta);
	        groupColor.add(jaune);
	        groupColor.add(cyan);

	        
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(11)).add(rouge);
			 
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(12)).add(bleu);
			 
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(13)).add(vert);
			
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(14)).add(magenta);
			 
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(15)).add(jaune);
			 
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(16)).add(cyan);
			
			
			
			JRadioButton humain = new JRadioButton("Humain");
			humain.setForeground(Color.DARK_GRAY);
			humain.setBackground(Color.LIGHT_GRAY);
			humain.setFont(fontForm);
			humain.setSelected(true);
			
	        JRadioButton ordi_Facile = new JRadioButton("IA - facile");
	        ordi_Facile.setForeground(Color.DARK_GRAY);
	        ordi_Facile.setBackground(Color.LIGHT_GRAY);
	        ordi_Facile.setFont(fontForm);
	        
	        JRadioButton ordi_moyen = new JRadioButton("IA - moyen");
	        ordi_moyen.setForeground(Color.DARK_GRAY);
	        ordi_moyen.setBackground(Color.LIGHT_GRAY);
	        ordi_moyen.setFont(fontForm);
	        
			JRadioButton ordi_difficile = new JRadioButton("IA - difficile");
			ordi_difficile.setForeground(Color.DARK_GRAY);
			ordi_difficile.setBackground(Color.LIGHT_GRAY);
			ordi_difficile.setFont(fontForm);
			
	        
	        ButtonGroup groupTypeJoueur = new ButtonGroup();
	        groupTypeJoueur.add(humain);
	        groupTypeJoueur.add(ordi_Facile);
	        groupTypeJoueur.add(ordi_moyen);
	        groupTypeJoueur.add(ordi_difficile);
	        
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(20)).add(humain);
			 
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(21)).add(ordi_Facile);
			 
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(22)).add(ordi_moyen);
			
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(23)).add(ordi_difficile);
			
			Font fontSuivant= new Font("Impact",Font.BOLD,30);

			JButton buttonSuivant = new JButton(" Suivant ");
			buttonSuivant.setFont(fontSuivant);
			buttonSuivant.setHorizontalAlignment(JLabel.CENTER);
			buttonSuivant.setBackground(Color.DARK_GRAY);
			buttonSuivant.setForeground(Color.white);
			buttonSuivant.addActionListener(new ButtonConfigPlayer(nbJoueur,textFieldPseudo,groupColor,groupTypeJoueur));
		    //buttonSuivant.addActionListener(new ButtonNumberPlayer(textFieldNumberPlayer));
			((JPanel)((JPanel)this.getContentPane().getComponent(1)).getComponent(34)).add(buttonSuivant);
			
			
			

		   
		    this.getContentPane().repaint();
		    this.getContentPane().revalidate();
			
		}
		
        ///////////////////////// LISTENER  /////////////////////////////////
	

		
	  class ButtonNewGameListener implements ActionListener{ 
		  
		    public void actionPerformed(ActionEvent arg0) {
		    	getContentPane().removeAll();
				getContentPane().setLayout(new BorderLayout());
				getContentPane().setBackground(Color.DARK_GRAY);

				JLabel labelNumberPlayer=new JLabel("<html>Combien de joueur ?<br> &nbsp &nbsp(entre 2 et 6) </html>");
				Font fontNumberPlayer = new Font("Impact",Font.ITALIC,100);
				labelNumberPlayer.setFont(fontNumberPlayer);
				labelNumberPlayer.setHorizontalAlignment(JLabel.CENTER);
				labelNumberPlayer.setForeground(Color.LIGHT_GRAY);
			    getContentPane().add(labelNumberPlayer, BorderLayout.CENTER);
			    
				JPanel panelInputNumberPlayer= new JPanel();
				panelInputNumberPlayer.setLayout(new BorderLayout());
				panelInputNumberPlayer.setBackground(Color.DARK_GRAY);
				Font FontInputNumberPlayer= new Font("Impact",Font.BOLD,40);

			    
				JTextField textFieldNumberPlayer = new JTextField("");
				textFieldNumberPlayer.setEditable(true);
				textFieldNumberPlayer.setColumns(10);
				textFieldNumberPlayer.setFont(FontInputNumberPlayer);
				textFieldNumberPlayer.setBackground(Color.LIGHT_GRAY);
				textFieldNumberPlayer.setForeground(Color.DARK_GRAY);
				panelInputNumberPlayer.add(textFieldNumberPlayer, BorderLayout.CENTER);
				
				JButton buttonSuivant = new JButton(" Suivant ");
				buttonSuivant.setFont(FontInputNumberPlayer);
				buttonSuivant.setHorizontalAlignment(JLabel.CENTER);
				buttonSuivant.setBackground(Color.DARK_GRAY);
				buttonSuivant.setForeground(Color.LIGHT_GRAY);
			    buttonSuivant.addActionListener(new ButtonNumberPlayer(textFieldNumberPlayer));
			    panelInputNumberPlayer.add(buttonSuivant, BorderLayout.EAST);
			    
			    getContentPane().add(panelInputNumberPlayer, BorderLayout.SOUTH);

			    
			    repaint();
			    revalidate();
		    }
		  }
	  
	  
	  class ButtonNumberPlayer implements ActionListener{
		  
		  private JTextField textFieldNumberPlayer;
		  
	     public ButtonNumberPlayer(JTextField textFieldNumberPlayer) {
	    	 this.textFieldNumberPlayer=textFieldNumberPlayer;
		}

			public void actionPerformed(ActionEvent e) {
				try {
					partie=new Partie();
					int numberPlayer=Integer.parseInt(textFieldNumberPlayer.getText());
					if(numberPlayer<2||numberPlayer>6) {
						throw new Exception();
					}
  		            partie.creationJoueurs(numberPlayer);
					partie.initPartie(numberPlayer);
  		            configueJoueur(0);
			    	
				}
				catch(Exception e1){
					textFieldNumberPlayer.setBackground(Color.RED);
					repaint();
				    revalidate();
				}

				
		    	 
		    }
		  }
	  
	  class ButtonConfigPlayer implements ActionListener{
		  
		  private JTextField textFieldPseudo;
		  private int indiceJoueur;
		  private ButtonGroup groupColor;
		  private ButtonGroup groupTypeJoueur;
		  
	     public ButtonConfigPlayer(int indiceJoueur,JTextField textFieldPseudo, ButtonGroup groupColor, ButtonGroup groupTypeJoueur) {
	    	 this.textFieldPseudo=textFieldPseudo;
	    	 this.indiceJoueur=indiceJoueur;
	    	 this.groupColor=groupColor;
	    	 this.groupTypeJoueur=groupTypeJoueur;
		}

			public void actionPerformed(ActionEvent e) {
				try {
					Joueur joueurUpdate=partie.getJoueurs().get(indiceJoueur);
					joueurUpdate.setPseudo(textFieldPseudo.getText());
					JRadioButton radio=null;
					Enumeration<AbstractButton> listRadioColor=groupColor.getElements();
					boolean colorNotFound=true;
					while(listRadioColor.hasMoreElements()&&colorNotFound) {
						radio=(JRadioButton) listRadioColor.nextElement();
						if(radio.isSelected())
							colorNotFound=false;
					}
					joueurUpdate.setColor(radio.getText());
					Enumeration<AbstractButton> listRadioTypeJoueur=groupTypeJoueur.getElements();
					boolean TypeJoueurNotFound=true;
					while(listRadioTypeJoueur.hasMoreElements()&&TypeJoueurNotFound) {
						radio=(JRadioButton) listRadioTypeJoueur.nextElement();
						if(radio.isSelected())
							TypeJoueurNotFound=false;
					}
					joueurUpdate.setTypeJoueur(radio.getText());
					
					
					indiceJoueur++;
					if(indiceJoueur<partie.getJoueurs().size()) {
	  		            configueJoueur(indiceJoueur);
					}
					else {
	  		            partie.ordoJoueurs();
	  		            partie.setJeu(new Jeu(partie));
	  		            partie.getJeu().setJoueur(partie.getJoueurs().get(0));
	  		            initialiserCarte();
					}
				}
				catch(Exception e1){
					e1.printStackTrace();
				}

				
		    	 
		    }
		  }

	  class ButtonContinueGame implements ActionListener{
		 private JButton buttonContinueGame;
	     public ButtonContinueGame(JButton buttonContinueGame) {
			this.buttonContinueGame=buttonContinueGame;
		}

		public void actionPerformed(ActionEvent e) {
	    	 try {
	    		partie=new Partie();
				partie.chargerPartie("src/jeu/sauvegarde_partie");
				initialiserCarte();

				
			} catch(Exception e1) {
				buttonContinueGame.setText("   Données inaccessible  !");
				buttonContinueGame.setEnabled(false);
				buttonContinueGame.setBorderPainted(false);
				repaint();
			    revalidate();
			} 


	    }
	  }
	  
	  public class ButtonChoixTerritoireAttaquant implements ActionListener{
		  
		    ButtonTerritoire buttonTerritoire;
		  
		    public ButtonChoixTerritoireAttaquant(ButtonTerritoire buttonTerritoire) {
		    	this.buttonTerritoire=buttonTerritoire;
		}

			public void actionPerformed(ActionEvent arg0) {
				desactiverAllTerritoire();
		    	ArrayList<Territoire> listTerritoireVoisin=partie.getCarte().recupererVoisin(buttonTerritoire.getTerritoire().getId());		    	
		    	((JLabel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).setText("<html><br>Tour de "+partie.getJeu().getJoueur().getPseudo()+"<br><br>Sélectionner le territoire à attaquer</html>");
		    	((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(1)).setEnabled(false);
		    	((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(2)).setEnabled(false);

		    	
				for(int i=0;i<listTerritoireVoisin.size();i++) {
					if(listTerritoireVoisin.get(i).getJoueurID()!=partie.getJeu().getJoueur().getID()) { // territoire voisin n'appartenant pas au même joueur
							((ButtonTerritoire)((JPanel) getContentPane().getComponent(0)).getComponent(listTerritoireVoisin.get(i).getId())).setEnabled(true);
							((ButtonTerritoire)((JPanel) getContentPane().getComponent(0)).getComponent(listTerritoireVoisin.get(i).getId())).addActionListener(new ButtonChoixTerritoireAttaquer(buttonTerritoire,((ButtonTerritoire)((JPanel)getContentPane().getComponent(0)).getComponent(listTerritoireVoisin.get(i).getId()))));
						
						
					}
					
				}
				
				repaint();
			    revalidate();
				
		    	
		    }
		  }
	  
	  public class ButtonChoixTerritoireAttaquer implements ActionListener{
		  
		    ButtonTerritoire buttonTerritoireAttaque;
		    ButtonTerritoire buttonTerritoireDefense;

			  
		    public ButtonChoixTerritoireAttaquer(ButtonTerritoire buttonTerritoireAttaque, ButtonTerritoire buttonTerritoireDefense) {
		    	this.buttonTerritoireAttaque=buttonTerritoireAttaque;
		    	this.buttonTerritoireDefense=buttonTerritoireDefense;
		}

			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttonTerritoireAttaque.getTerritoire().setJouer(true);
				buttonTerritoireDefense.getTerritoire().setJouer(true);

				partie.getJeu().getJoueur().jouer(partie.getJeu(), buttonTerritoireAttaque.getTerritoire(), buttonTerritoireDefense.getTerritoire());
				desactiverAllTerritoire();
				majAffichageButtonTerritoire();
				activerTerritoireJoueurCourrant();
				
		    	((JLabel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).setText("<html><br>Tour de "+partie.getJeu().getJoueur().getPseudo()+"<br><br>Sélectionner le territoire attaquant</html>");
		    	((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(1)).setEnabled(true);
		    	((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(2)).setEnabled(true);
				
				repaint();
			    revalidate();
				
				
				
				
			}
	  }
	  
	  class ButtonFinTour implements ActionListener{


			public void actionPerformed(ActionEvent e) {
		      boolean victory=partie.getJeu().getJoueur().finTour(partie.getJeu());
		      
		      if(victory){
			    	((JLabel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).setText("<html><br>Fellicitaion "+partie.getJeu().getJoueur().getPseudo()+"<br><br>Vous avez gagner</html>");
			    	((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(1)).setEnabled(false);
			    	((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(2)).setEnabled(false);
		      }
		      else {
		    	  partie.nextPlayer();
		  		  resetBooleanJouerTerritoire();
		  	    if(!partie.getJeu().getJoueur().getTypeJoueur().equals(TypeJoueur.HUMAIN)) {
			    	tourIA();
			    }
		  	    else {
			    	  ((JLabel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).setText("<html><br>Tour du joueur : "+partie.getJeu().getJoueur().getPseudo()+"<br><br>Sélectionner le territoire attaquant</html>");
			    	  ((JLabel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).setForeground(partie.getJeu().getJoueur().getColor());
			    	  ((JButton)((JPanel)getContentPane().getComponent(1)).getComponent(1)).setBackground(partie.getJeu().getJoueur().getColor());
			    	  desactiverAllTerritoire();
					  majAffichageButtonTerritoire();
					  activerTerritoireJoueurCourrant();
					  repaint();
					  revalidate();
		  	    }

		    	  
		      }

			} 
		   }
	  
	  class ButtonSaveGame implements ActionListener{


		public void actionPerformed(ActionEvent e) {
	      try {
	          partie.sauvegardePartie("src/jeu/sauvegarde_partie");
	      } catch (IOException e1) {
	          e1.printStackTrace();
	      }

		} 
	   }
	  
	  class ButtonLeaveGame implements ActionListener{


		public void actionPerformed(ActionEvent e) {
	    	getContentPane().removeAll();
	    	menuDemarage();
		} 
	   }
	  
	  
 
		

}
