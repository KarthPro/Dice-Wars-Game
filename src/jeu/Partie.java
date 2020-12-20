package jeu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import javax.swing.SwingUtilities;



import interface_graphic.Window;

public class Partie implements Serializable {
	private Carte carte;
	private ArrayList<Joueur> joueurs;
	private Jeu jeu;
	private int indexJoueur;

	public Partie() {
	}

	public static void main(String[] args) {
        
        
		 SwingUtilities.invokeLater(new Runnable(){
			 public void run(){    
				 Window window = new Window();
				 window.setVisible(true);  
			 }
		 });
		 
    }
	
	public void nextPlayer() {
		indexJoueur++;
		if(indexJoueur >= this.joueurs.size()) {
			indexJoueur = 0;
		}
		this.jeu.setJoueur(joueurs.get(indexJoueur));
	}

	public void creationJoueurs(int nbrJoueurs) {
		joueurs=new ArrayList<Joueur>();
		for (int i = 0; i < nbrJoueurs; i++) {
			Joueur j = new Joueur();
			joueurs.add(j);
		}
	}
	
	public void majIndexJoueur(int nbrJoueurs) {
		indexJoueur += 1;
	      if(indexJoueur >=joueurs.size()) {
	    	  indexJoueur = 0;
	      }
	}
	
	public void initPartie(int nbrJoueur) {

		try {
			boolean carteConnexe=false;
			while(!carteConnexe) {
				indexJoueur=0;			
				jeu=null;
				Joueur.resetStaticID();
				Territoire.resetStatidId();
				Random r = new Random();
				int largeur =r.nextInt(7);
				int longueur =r.nextInt(10);
				
				Carte.generationCarte(longueur+1, largeur+1, nbrJoueur);
				carte = new Carte();
				carteConnexe=carte.connexe();
				System.out.println("azertyuik;, cdftyujik,nb vfgtyuik,");
			}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		

	}
	
	//ordonne aléatoirement les joueurs
	public void ordoJoueurs() {	
		Collections.shuffle(joueurs);
	}

	//permet d'afficher la carte en console
	/*public void affichageCarte() {
		
		for (int i = 0; i < this.getCarte().getLongueur(); i++) {
			for (int j = 0; j < this.getCarte().getLargeur(); j++) {
				System.out.print(this.getCarte().getCarte()[i][j]);
				System.out.print(" || ");
			}
			System.out.println(" ");
		}
	}*/
	
	//récupère un joueur à partir de son id
	public Joueur getJoueurID(int id) {
		Iterator<Joueur> iterator = this.joueurs.iterator();
		while (iterator.hasNext()) {
			Joueur j = iterator.next();
			if (j.getID() == id) {
				System.out.println("trouvé");
				return j;
			}
		}
		System.out.println("rien trouvé");

		return null;
	}

	//permet de sauvegarder la partie en cours
	public void sauvegardePartie(String nomFichier) throws IOException {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(nomFichier);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
		} finally {
			oos.close();
			fos.close();

		}
	}

	//permet de charger une partie sauvegardé
	@SuppressWarnings("unchecked")
	public void chargerPartie(String nomFichier) throws IOException, ClassNotFoundException {

		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try {
			fis = new FileInputStream(nomFichier);
			ois = new ObjectInputStream(fis);
			Partie partie = (Partie) ois.readObject();
			this.carte = partie.carte;
			this.jeu = partie.jeu;
			this.joueurs = partie.joueurs;
		} finally {
			ois.close();
			fis.close();
		}
	}

	public Carte getCarte() {
		return carte;
	}

	public ArrayList<Joueur> getJoueurs() {
		return joueurs;
	}

	public Jeu getJeu() {
		return jeu;
	}

	public void setCarte(Carte carte) {
		this.carte = carte;
	}

	public void setJoueurs(ArrayList<Joueur> joueurs) {
		this.joueurs = joueurs;
	}

	public void setJeu(Jeu jeu) {
		this.jeu = jeu;
	}

	@Override
	public String toString() {
		return "Partie [carte=" + carte + ", joueurs=" + joueurs + ", jeu=" + jeu + "]";
	}

}
