

package jeu;

import java.util.Comparator;

public interface ComparatorCombinaison {
	static final Comparator<Combinaison> Benefique_Order = new Comparator<Combinaison>() {
		public int compare(Combinaison comb1, Combinaison comb2) {
			double comb1Proba = comb1.getProbaVictoire();
			double comb2Proba = comb2.getProbaVictoire();
			//on compare avec les taux de victoire
			if (comb1Proba > comb2Proba) {
				return -1;
			}
			if (comb1Proba < comb2Proba) {
				return 1;
			}
			//si encore égal
			int comb1Gain = comb1.getNbrDeAGagner();
			int comb2Gain = comb2.getNbrDeAGagner();
			//comparaison du gain de dé
			if(comb1Gain > comb2Gain) {
				return -1;
			}
			if(comb1Gain < comb2Gain) {
				return 1;
			}
			//si encore égal
			int comb1Perte = comb1.getNbrDeAPerdre();
			int comb2Perte = comb2.getNbrDeAPerdre();
			//comparaison de la perte de dé
			if(comb1Perte > comb2Perte) {
				return 1;
			}
			if(comb1Perte < comb2Perte) {
				return -1;
			}
			//si encore égal
			int comb1Voisins = comb1.getNbrVoisinAppartenant();
			int comb2Voisins = comb2.getNbrVoisinAppartenant();
			//comparaison du nombre de voisins
			if(comb1Voisins > comb2Voisins) {
				return -1;
			}
			if(comb1Voisins < comb2Voisins) {
				return 1;
			}
			//si encore égal
			int comb1VoisinsIndirect = comb1.getNbrVoisinAppartenantVoisin();
			int comb2VoisinsIndirect = comb2.getNbrVoisinAppartenantVoisin();
			//comparaison du nombre de voisin indirect
			if(comb1VoisinsIndirect > comb2VoisinsIndirect) {
				return -1;
			}
			if(comb1VoisinsIndirect < comb2VoisinsIndirect) {
				return 1;
			}
			return 0;
			
		}
	};
}
