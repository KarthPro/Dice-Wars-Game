package jeu;

import java.io.File;
import java.util.ArrayList;

public class Combinaison {
	private String nom;
	private Territoire territoireJoueur;
	private Territoire territoireVoisin;
	private double probaVictoire;
	private int nbrDeAGagner;
	private int nbrDeAPerdre;
	private int nbrVoisinAppartenant;
	private int nbrVoisinAppartenantVoisin;
	private final double[][] res = Util.proba(new File("src/jeu/probaDe.csv"));
	
	//creation d'une combinaison avec un territoire du joueur, un territoire voisin, le nombre de territoire nous appartenant dans les voisins du territoire, et le nbr de territoire nous appartenant autour du territoire voisin
	Combinaison(Territoire territoireJoueur, Territoire territoireVoisin, int nbrVoisinAppartenant,
			int nbrVoisinAppartenantVoisin) {
		this.territoireJoueur = territoireJoueur;
		this.territoireVoisin = territoireVoisin;
		this.setNom(territoireJoueur.getId(), territoireVoisin.getId());
		//on calcul le nombre de dé à gagner et à perdre
		this.nbrDeAGagner();
		this.nbrDePerdre();
		//calcul de la probabilité de victoire
		this.calculProbaVictoire();
	}

	//permet de calculer le nombre de dé potentiel à gagner
	private void nbrDeAGagner() {
		int res = 0;
		res = this.territoireVoisin.getNbDe();
		this.setNbrDeAGagner(res);
	}

	//permet de calculer le nombre de dé potentiel à perdre
	private void nbrDePerdre() {
		int res = 0;
		res = this.territoireJoueur.getNbDe() - 1;
		this.setNbrDeAPerdre(res);
	}

	//on va chercher dans le tableau res les probabilités de victoires potentiel de la combinaison
	private void calculProbaVictoire() {
		double[] tableauProbaJoueur = new double[this.territoireJoueur.getNbDe() * 6];
		double[] tableauAdversaire = new double[this.territoireVoisin.getNbDe() * 6];

		for (int i = 0; i < tableauProbaJoueur.length; i++) {
			tableauProbaJoueur[i] = 0;
		}
		for (int i = 0; i < tableauAdversaire.length; i++) {
			tableauAdversaire[i] = 0;
		}
		for (int x = 0; x < tableauProbaJoueur.length; x++) {
			tableauProbaJoueur[x] = res[x + 1][this.territoireJoueur.getNbDe()];
		}
		for (int x = 0; x < tableauAdversaire.length; x++) {
			tableauAdversaire[x] = res[x + 1][this.territoireVoisin.getNbDe()];
		}
		this.setProbaVictoire(0);
		for (int i = 0; i < tableauProbaJoueur.length; i++) {
			for (int j = 0; j < tableauAdversaire.length; j++) {
				if (i > j) {
					this.probaVictoire += tableauProbaJoueur[i] * tableauAdversaire[j];
				}
			}
		}
	}

	// on liste toutes les combinaisons d'attaques possible pour le joueur
	public static void lister(Joueur j, ArrayList<Combinaison> combinaisons, Carte carte) {
		ArrayList<Territoire> voisins = new ArrayList<Territoire>();
		ArrayList<Territoire> territoire = new ArrayList<Territoire>();
		voisins.removeAll(voisins);
		territoire.removeAll(territoire);
		for (Territoire t : j.getTerritoires()) {
			if (t.getNbDe() != 1 || t.getJoueurID() == j.getID()) {
				int lenght = carte.recupererVoisin(t.getId()).size();
				ArrayList<Territoire> AllVoisins = carte.recupererVoisin(t.getId());
				for (int i = 0; i < lenght; i++) {
					if (AllVoisins.get(i).getJoueurID() == j.getID() || !AllVoisins.get(i).getExist()) {
						continue;
					} else {
						voisins.add(AllVoisins.get(i));
						territoire.add(t);
					}
				}
			}
		}
		int i = 0;
		for (Territoire ter : voisins) {
			Territoire t = territoire.get(i);
			int nbrVoisinDirect = carte.nbrTerritoireVoisinGagne(t);
			int nbrVoisinIndirect = carte.nbrTerritoireGagneduVoisinGagne(ter, j);
			Combinaison comb = new Combinaison(t, ter, nbrVoisinDirect, nbrVoisinIndirect);
			if (t.getNbDe() != 1) {
				combinaisons.add(comb);
			}
			i++;
		}
	}

	public int getNbrDeAPerdre() {
		return nbrDeAPerdre;
	}

	public void setNbrDeAPerdre(int nbrDeAPerdre) {
		this.nbrDeAPerdre = nbrDeAPerdre;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(int territoireJoueur, int territoireVoisin) {
		this.nom = "" + territoireJoueur + "" + territoireVoisin;
	}

	public double getProbaVictoire() {
		return probaVictoire;
	}

	public void setProbaVictoire(double probaVictoire) {
		this.probaVictoire = probaVictoire;
	}

	public int getNbrDeAGagner() {
		return nbrDeAGagner;
	}

	public void setNbrDeAGagner(int nbrDeAGagner) {
		this.nbrDeAGagner = nbrDeAGagner;
	}

	public Territoire getTerritoireJoueur() {
		return territoireJoueur;
	}

	public void setTerritoireJoueur(Territoire territoireJoueur) {
		this.territoireJoueur = territoireJoueur;
	}

	public Territoire getTerritoireVoisin() {
		return territoireVoisin;
	}

	public void setTerritoireVoisin(Territoire territoireVoisin) {
		this.territoireVoisin = territoireVoisin;
	}

	public int getNbrVoisinAppartenant() {
		return nbrVoisinAppartenant;
	}

	public void setNbrVoisinAppartenant(int nbrVoisinAppartenant) {
		this.nbrVoisinAppartenant = nbrVoisinAppartenant;
	}

	public int getNbrVoisinAppartenantVoisin() {
		return nbrVoisinAppartenantVoisin;
	}

	public void setNbrVoisinAppartenantVoisin(int nbrVoisinAppartenantVoisin) {
		this.nbrVoisinAppartenantVoisin = nbrVoisinAppartenantVoisin;
	}

	@Override
	public String toString() {
		return "Combinaison [nom=" + nom + ", territoireJoueur=" + territoireJoueur + ", territoireVoisin="
				+ territoireVoisin + ", probaVictoire=" + probaVictoire + ", nbrDeAGagner=" + nbrDeAGagner
				+ ", nbrDeAPerdre=" + nbrDeAPerdre + "]";
	}

}