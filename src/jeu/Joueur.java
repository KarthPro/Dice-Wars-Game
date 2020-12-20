package jeu;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Joueur implements Serializable{
	static int staticID = 1;
	private int ID;
	private ArrayList<Territoire> territoires = new ArrayList<Territoire>();
	private Color color;
	private String pseudo;
	private TypeJoueur typeJoueur;


	public Joueur() {
		this.ID = staticID;
		staticID += 1;
	}
	

	public void jouer(Jeu jeu,Territoire attacking,Territoire attacked) {
		try {
			this.attaquer(attacking, attacked,jeu);
		} catch (NotExistException e) {
			System.out.println(e.getMessage());
		} catch (NotOwnerException e) {
			System.out.println(e.getMessage());
		} catch(OnlyOneDiceException e) {
			System.out.println(e.getMessage());
		}catch (OwnerException e ) {
			System.out.println(e.getMessage());
		} catch (NotNeibourghException e) {
			System.out.println(e.getMessage());
		}
		

	}
	public boolean selectionIA(Jeu jeu) {
		ArrayList<Combinaison> combinaisons = new ArrayList<Combinaison>();
		Combinaison.lister(this, combinaisons, jeu.partie.getCarte());
		Combinaison[] combinaisonArray = new Combinaison[combinaisons.size()];
		combinaisons.toArray(combinaisonArray);
		Arrays.sort(combinaisonArray, ComparatorCombinaison.Benefique_Order);
		
		if(combinaisons.isEmpty()) {
			return false;
		}
		this.jouer(jeu,combinaisonArray[0].getTerritoireJoueur(), combinaisonArray[0].getTerritoireVoisin());
		return true;
	}
	
	
	
	
	public Color getColor() {
		return color;
	}

	public void setColor(String color) {
		if(color.equals("Rouge")) {
			this.color=Color.red;
		}
		if(color.equals("Bleu")) {
			this.color=Color.blue;
		}
		if(color.equals("Vert")) {
			this.color=Color.green;
		}
		if(color.equals("Magenta")) {
			this.color=Color.magenta;
		}
		if(color.equals("Jaune")) {
			this.color=Color.yellow;
		}
		if(color.equals("Cyan")) {
			this.color=Color.cyan;
		}

	}
	
	

	public boolean finTour(Jeu jeu) {
		jeu.misAJourDe(this);
		System.out.println("Fin du tour");
		//checker si victoire
		return jeu.victoire();
	}

	public void attaquer(Territoire attacking, Territoire attacked, Jeu jeu) throws NotExistException,NotNeibourghException,NotOwnerException, OwnerException, OnlyOneDiceException {
		if(attacking.getExist() == false || attacked.getExist() == false) {
			throw new NotExistException();
		}
		if(this.getID() != attacking.getJoueurID()) {
			throw new NotOwnerException();
		}
		if(attacking.getNbDe() <= 1) {
			throw new OnlyOneDiceException();
		}
		if(!jeu.partie.getCarte().recupererVoisin(attacking.getId()).contains(attacked)) {
			throw new NotNeibourghException();
		}
		if(this.getID() == attacked.getJoueurID()) {
			throw new OwnerException();
		}
		int scoreDefense = 0;
		int scoreAttaque = 0;
		boolean victoire = true;
		Random r = new Random();
		for (int i = 0; i < attacking.getNbDe(); i++) {
			scoreAttaque += r.nextInt(5) + 1;
		}
		for (int i = 0; i < attacked.getNbDe(); i++) {
			scoreDefense += r.nextInt(5) + 1;
		}
		if(scoreDefense >= scoreAttaque) {
			victoire = false;
		}
		System.out.println("score Defense");
		System.out.println(scoreDefense);
		System.out.println("score Attaque");
		System.out.println(scoreAttaque);
		System.out.println(victoire);
		jeu.misAJourAttaque(victoire, attacking, attacked);
	}
	
	public void remove(Territoire t) {
		System.out.println("remove "+t.toString());
        this.territoires.remove(t);
    }
	
	public void add(Territoire t) {
		this.territoires.add(t);
	}
	
	public ArrayList<Territoire> getTerritoires() {
		return territoires;
	}
	
	
	public void setTerritoires(ArrayList<Territoire> territoires) {
		this.territoires = territoires;
	}

	public int getID() {
		return ID;
	}

	@Override
	public String toString() {
		return "Joueur [ID=" + ID + ", territoires=" + territoires + "]";
	}

	public static void resetStaticID() {
		Joueur.staticID = 1;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	
	public TypeJoueur getTypeJoueur() {
		return typeJoueur;
	}

	public void setTypeJoueur(String typeJoueur) {
		if(typeJoueur.equals("Humain")) {
			this.typeJoueur=TypeJoueur.HUMAIN;
		}
		if(typeJoueur.equals("IA - facile")) {
			this.typeJoueur=TypeJoueur.IA_Facile;
		}
		if(typeJoueur.equals("IA - moyen")) {
			this.typeJoueur=TypeJoueur.IA_Moyen;
		}
		if(typeJoueur.equals("IA - difficile")) {
			this.typeJoueur=TypeJoueur.IA_Difficile;
		}
	}
	
	

}
