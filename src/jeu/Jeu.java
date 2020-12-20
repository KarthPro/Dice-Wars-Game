package jeu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Jeu implements Serializable {
	Partie partie;
	Joueur joueur;

	public Jeu(Partie partie) {
		this.partie = partie;
		//lors de la creation du jeu, on initialise la carte et les territoires dans la carte
		this.initialisationTerritoire();
		
	}

	public void initialisationTerritoire() {
		//surface de la carte
		int nbrTerritoire = this.partie.getCarte().getLargeur() * this.partie.getCarte().getLongueur();
		//on recupere le nombre de joueur de la partie
		int nbrJoueurs = this.partie.getJoueurs().size();
		//on determine le nombre de territoire par joueur (si la creation de la carte est faite correctement, tous les territoires auront 1 joueur)
		int nbrTerritoireParJoueur = this.partie.getCarte().nbrTerritoire() / nbrJoueurs;
		Random r = new Random();
		int t;
		int longueur = this.partie.getCarte().getLongueur();
		int largeur = this.partie.getCarte().getLargeur();
		//On parcours la liste des joueurs
		for (Joueur j : this.partie.getJoueurs()) {
			int i = 0;
			//on cree un boolean qui permet de savoir si on a attribu� le bon nombre de territoire au joueur
			boolean attribution = false;
			while (!attribution) {
				//on tire au sort un num�ro de territoire
				t = r.nextInt(nbrTerritoire);
				//on determine ses coordonn�es sur la carte
				int L = t / largeur;
				int l = t % largeur;
				//tant que le territoire que l'on parcour appartient d�j� � qqn, on augmente les indices l et L
				while (this.partie.getCarte().getCarte()[L][l].getJoueurID() != 0) {
					if (this.partie.getCarte().getCarte()[L][l].getJoueurID() != 0) {
						l += 1;
						if (l >= largeur) {
							l = 0;
							L += 1;
						}
						if (L >= longueur) {
							L = 0;
						}
						if (l * L == nbrTerritoire) {
							l = 0;
							L = 0;
						}
					} else {
						this.partie.getCarte().getCarte()[L][l].setJoueurID(j.getID());
					}
				}
				if (this.partie.getCarte().getCarte()[L][l].getExist()) {
					//d�s qu'on a trouv� un territoire sans joueur et qu'il existe bien
					//on ajoute le territoire � la liste des territoires du joueur courant
					j.getTerritoires().add(this.partie.getCarte().getCarte()[L][l]);
					//on ajoute l'id du joueur au territoire
					this.partie.getCarte().getCarte()[L][l].setJoueurID(j.getID());
					//on increment le nombre de territoire que poss�de actuellement le joueur
					i += 1;
				}
				if (i == nbrTerritoireParJoueur) {
					//on a bien attribu� le bon nombre de territoire au joueur
					attribution = true;
				}
			}
		}
		//On r�cup�re le nombre de territoire de la partie
		nbrTerritoire = this.partie.getCarte().nbrTerritoire();
		//On fixe le nombre de d� par territoire � 4 (1 + 3)
		int nbrDeParTerritoire = 3;
		//on fait un tableau du nbr de d� que chaque joueur doit placer
		int nbrDeParJoueur[] = new int[nbrJoueurs];
		for (int i = 0; i < nbrJoueurs; i++) {
			nbrDeParJoueur[i] = nbrDeParTerritoire * nbrTerritoireParJoueur;
		}
		//Parcours de tous les territoires
		for (int i = 0; i < this.partie.getCarte().getLongueur(); i++) {
			for (int j = 0; j < this.partie.getCarte().getLargeur(); j++) {
				Territoire ter = this.partie.getCarte().getCarte()[i][j];
				//recuperation de l'id du joueur
				int idJoueur = ter.getJoueurID();
				if (idJoueur != 0 && nbrDeParJoueur[idJoueur - 1] >= 1) {
					//on determine aleatoirement le nombre de d�
					int a = r.nextInt(nbrDeParJoueur[idJoueur - 1]);
					int nbrDe = Math.min(7, a);
					//ajout des d� sur le territoire
					ter.setNbDe(nbrDe + 1);
					nbrDeParJoueur[idJoueur - 1] -= nbrDe;
				}
			}
		}
		//si il reste des d� � distribuer, on les distribue mais pas al�atoirement, territoire par territoire, en faisant attention qu'il n'y ait pas plus de 8 d�s par territoire
		for (int i = 1; i <= nbrDeParJoueur.length; i++) {
			Iterator<Territoire> iterator = this.partie.getJoueurID(i).getTerritoires().iterator();
			while (iterator.hasNext() && (nbrDeParJoueur[i - 1] != 0)) {
				Territoire ter = iterator.next();
				if (ter.getNbDe() + nbrDeParJoueur[i - 1] <= 8) {
					ter.setNbDe(ter.getNbDe() + nbrDeParJoueur[i - 1]);
					nbrDeParJoueur[i - 1] = 0;
				} else {
					nbrDeParJoueur[i - 1] = nbrDeParJoueur[i - 1] - (8 - ter.getNbDe());
					ter.setNbDe(8);
				}
			}
		}
	}
	
	//permet de generer un entier entre 1 et 6 (d�)
	public int aleatoire() {
		Random r = new Random();
		return r.nextInt(6) + 1;
	}

	//permet de mettre � jour la carte apr�s une attaque
	public void misAJourAttaque(boolean victoire, Territoire attacking, Territoire attacked) {
		boolean over = false;
		if (victoire) {
			// Le territoire attaquant a gagn�
			if (attacked.getNbDe() + attacking.getNbDe() - 1 <= 8) {
				// tous les d�s sauf un se d�placent du territoire attaquant au territoire
				// attaqu�
				attacked.setNbDe(attacked.getNbDe() + attacking.getNbDe() - 1);
				attacking.setNbDe(1);
			} else {
				attacking.setNbDe(attacked.getNbDe() + attacking.getNbDe() - 1 - 8);
				attacked.setNbDe(8);
			}
			// le joueur attaqu� perd son territoire
			try {
				Joueur j = this.rechercheJoueurID(attacked.getJoueurID());
				j.remove(attacked);
			} catch (NotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// le territoire attaqu� a pour joueur le joueur attaquant
			attacked.setJoueurID(attacking.getJoueurID());
			try {
				// le joueur attaquant gagne le territoire
				Joueur joueur = this.rechercheJoueurID(attacking.getJoueurID());
				joueur.add(attacked);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Le territoire attaquant a perdu
			attacking.setNbDe(1);
		}
	}



	
	//permet de mettre � jour le nombre de d�, et de redistribu� celui ci aux territoires du joueur
	public void misAJourDe(Joueur joueur) {
		int compteur = 0;
		int dispo = 0;
		for (Territoire t : joueur.getTerritoires()) {
			if (t.getNbDe() == 8) {
				compteur += 1;
			} else {
				dispo += 8 - t.getNbDe();
			}
		}
		if (compteur == joueur.getTerritoires().size()) {
			return;
		}
		// le nombre de d� rapport� par un joueur est le nombre maximum de territoire
		// contigent d'un joueur
		int nbrDeGagne = this.partie.getCarte().maxTerritoireContigue(joueur);
		if (nbrDeGagne > dispo) {
			nbrDeGagne = dispo;
		}
		System.out.println(nbrDeGagne);
		// repartition al�atoire des d�s
		Random r = new Random();
		while (nbrDeGagne != 0) {
			for (int i = 0; i < this.partie.getCarte().getLongueur(); i++) {
				for (int j = 0; j < this.partie.getCarte().getLargeur(); j++) {
					Territoire t = this.partie.getCarte().getCarte()[i][j];
					if (t.getJoueurID() == joueur.getID()) {
						int nbr = Math.min(8, r.nextInt(nbrDeGagne + 1));
						if (t.getNbDe() + nbr <= 8) {
							t.setNbDe(t.getNbDe() + nbr);
						} else {
							nbr = 8 - t.getNbDe();
							t.setNbDe(8);
						}
						nbrDeGagne -= nbr;
					}
				}
			}
			System.out.println(nbrDeGagne);
		}
	}

	//recherche d'un joueur par rapport � son id
	public Joueur rechercheJoueurID(int id) throws NotFoundException {
		int size = this.partie.getJoueurs().size();
		if(id-1 >= size) {
			throw new NotFoundException();
		}
		for(Joueur j : this.partie.getJoueurs()) {
			if(id == j.getID()) {
				return j;
			}
		}
		return null;
	}
	//m�thode permettant de verifier si un joueur est en position de victoire
	public boolean victoire() {
		ArrayList<Joueur> joueur = this.partie.getJoueurs();
		for (Joueur j : joueur) {
			if (this.partie.getCarte().maxTerritoireContigue(j) == this.partie.getCarte().nbrTerritoire()) {
				return true;
			}
		}
		return false;
	}
	

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}


}
