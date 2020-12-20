package jeu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Carte implements Serializable{

	private int longueur;
	private int largeur;
	private Territoire[][] carte;

	//constructeur de la classe carte, renvoie l'erreur FileNotFound si le fichier src/jeu/csv_type n'est pas trouvé
	public Carte()throws FileNotFoundException{
		Scanner Struct_carte;
		Struct_carte = new Scanner(new File("src/jeu/csv_type"));
		Scanner inter = new Scanner(Struct_carte.nextLine());
		//On utilise les espaces comme séparation
		Scanner carte_el = inter.useDelimiter(" ");
		//On récupère la largeur
		this.largeur = carte_el.nextInt();
		//On récupère la longueur
		this.longueur = carte_el.nextInt();
		carte_el.close();
		inter.close();
		//On crée la carte comme un tableau de territoire de taille largeur * longueur
		this.carte = new Territoire[longueur][largeur];
		//On rempli ce tableau => 0 signifie que le territoire est inexistant
		//                     => 1 signifie que le territoire est existant
		for (int i = 0; i < longueur; i++) {
			inter = new Scanner(Struct_carte.nextLine());
			carte_el = inter.useDelimiter(" ");
			for (int j = 0; j < largeur; j++) {
				int ex = carte_el.nextInt();
				carte[i][j] = new Territoire(ex);
			}
			carte_el.close();
		}
		inter.close();
		Struct_carte.close();
	}

	//renvoie le nombre de territoire existant sur une carte
	public int nbrTerritoire() {
		int nbr = 0;
		for (int i = 0; i < longueur; i++) {
			for (int j = 0; j < largeur; j++) {
				if (this.carte[i][j].getExist()) {
					nbr++;
				}
			}
		}
		return nbr;
	}

	//méthode permettant de récupérer une arraylist des voisins d'un territoire depuis l'id du territoire en question
	public ArrayList<Territoire> recupererVoisin(int idTerritoire) {
    	ArrayList<Territoire> list= new ArrayList<Territoire>();


    
    	int coordonneX=idTerritoire%largeur;
    	int coordonneY=idTerritoire/largeur;
    	/*
     	* 	0 1 2
     	* 	7 . 3
     	* 	6 5 4
     	*/
    	if((coordonneX > 0 && coordonneY > 0))//0
   		 if(carte[coordonneY-1][coordonneX-1].getExist())
			try {
				list.add(rechercherTerritoire((coordonneY-1)*largeur + coordonneX-1));
			} catch (NotFoundException e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
   	 
    	if(coordonneY > 0)//1
   		 if(carte[coordonneY-1][coordonneX].getExist())
			try {
				list.add(rechercherTerritoire((coordonneY-1)*largeur + coordonneX));
			} catch (NotFoundException e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
   	 
    	if(coordonneX < largeur-1 && coordonneY > 0)//2
   		 if(carte[coordonneY-1][coordonneX+1].getExist())
			try {
				list.add(rechercherTerritoire((coordonneY-1)*largeur + coordonneX+1));
			} catch (NotFoundException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
   	 
    	if(coordonneX < largeur-1)//3
   		 if(carte[coordonneY][coordonneX+1].getExist())
			try {
				list.add(rechercherTerritoire((coordonneY)*largeur + coordonneX+1));
			} catch (NotFoundException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
   	 
    	if(coordonneX < largeur-1 && coordonneY < longueur-1 )//4
   		 if(carte[coordonneY+1][coordonneX+1].getExist())
			try {
				list.add(rechercherTerritoire((coordonneY+1)*largeur + coordonneX+1));
			} catch (NotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
   	 
    	if(coordonneY < longueur-1)//5
   		 if(carte[coordonneY+1][coordonneX].getExist())
			try {
				list.add(rechercherTerritoire((coordonneY+1)*largeur + coordonneX));
			} catch (NotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
   	 
    	if(coordonneX > 0 && coordonneY < longueur-1)//6
   		 if(carte[coordonneY+1][coordonneX-1].getExist())
			try {
				list.add(rechercherTerritoire((coordonneY+1)*largeur + coordonneX-1));
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   	 
    	if(coordonneX > 0)//7
   		 if(carte[coordonneY][coordonneX-1].getExist())
			try {
				list.add(rechercherTerritoire((coordonneY)*largeur + coordonneX-1));
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    
    	return list;
}

    //renvoie le maximum de territoire contigue d'un joueur
    public int maxTerritoireContigue(Joueur joueur) {

   	 int maxTerritoireContigue=0;
   	 ArrayList<Territoire> listTerritoireJoueur=joueur.getTerritoires();
   	 System.out.print("territoire du joueur : ");
   	 System.out.println(listTerritoireJoueur);
   	 
   	 for(Territoire territoire : listTerritoireJoueur) { // pour chaque territoire du territoire
   		 
   		 ArrayList<Territoire> listTerritoireParcouru= new ArrayList<Territoire>();
   		 listTerritoireParcouru.add(territoire);
   		 
   		 Queue<Territoire> queue = new LinkedList<Territoire>();
   		 queue.add(territoire);
   		 int maxTerritoireContigueTemp=1;
   		 
   		 //for(int i=0)
   		 while(queue.peek()!=null) { // tant que la file n'est pas vide
   			 Territoire territoireTemp=queue.remove(); //défile
   			 ArrayList<Territoire> listVoisin =recupererVoisin(territoireTemp.getId()); //liste des voisin du territoire
   			 for(Territoire territoireVoisin : listVoisin){
   				 if(listTerritoireJoueur.contains(territoireVoisin)) { // territoire voisin appartenant au même joueur
   					 if(!listTerritoireParcouru.contains(territoireVoisin)) { // territoire non comptabilisé
   						 queue.add(territoireVoisin);
   						 listTerritoireParcouru.add(territoireVoisin);
   						 maxTerritoireContigueTemp++;
   					 }
   				 }
   			 }
   			 
   		 }
   		 if(maxTerritoireContigueTemp>maxTerritoireContigue)
   			 maxTerritoireContigue=maxTerritoireContigueTemp;
   	 }
   		 return maxTerritoireContigue;
   			 
    }

	//permet de rechercher un territoire depuis l'id de celui ci dans la carte
	public Territoire rechercherTerritoire(int idTerritoire) throws NotFoundException {
		int coordonneX = idTerritoire / largeur;
		int coordonneY = idTerritoire % largeur;
		if(coordonneX > longueur || coordonneY > largeur) {
			throw new NotFoundException();
		}
		return carte[coordonneX][coordonneY];
	}

	public Territoire[][] getCarte() {
		return carte;
	}

	public void setCarte(Territoire[][] carte) {
		this.carte = carte;
	}

	public int getLongueur() {
		return longueur;
	}

	public void setLongueur(int longueur) {
		this.longueur = longueur;
	}

	public int getLargeur() {
		return largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	@Override
	public String toString() {
		return "Carte [longueur=" + longueur + ", largeur=" + largeur + ", carte=" + Arrays.toString(carte) + "]";
	}
	
	//méthode qui permet de regarder si autour du territoire un ennemi existe => renvoie true si il y a un terrritoire ennemi dans les voisins
	public boolean existeEnnemiTerritoireVoisin(int idTerritoire) {
		Territoire territoire=null;
		try {
			territoire = this.rechercherTerritoire(idTerritoire);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<Territoire> listVoisin=this.recupererVoisin(idTerritoire);
		for(int i=0;i<listVoisin.size();i++) {
			if(listVoisin.get(i).getJoueurID()!=territoire.getJoueurID()&&listVoisin.get(i).getExist()) {
				return true;
			}
		}
				
		return false;
		

		
	}
	
	//méthode vérifiant si une carte est connexe => si connexe renvoie true
	public boolean connexe() {
        boolean connexe = true;
	if(this.largeur == 1 || this.longueur == 1) {
			return false;
		}
        for (int i = 0; i < this.largeur; i++) {
            for (int j = 0; j < this.longueur; j++) {
                if (this.carte[j][i].getExist()) {
                    int nbrVoisins = this.recupererVoisin(this.carte[j][i].getId()).size();
                    if (nbrVoisins == 0) {
                        return false;
                    }
                }
            }
        }
        return connexe;
    }
	
	//méthode qui renvoie le nombre de territoire voisin que le joueur possède déjà
	public int nbrTerritoireVoisinGagne(Territoire territoire) {
        int nbr = 0;
        for(Territoire t : this.recupererVoisin(territoire.getId())) {
            if(t.getJoueurID() == territoire.getJoueurID()) {
                nbr ++;
            }
        }
        return nbr;
    }

    //méthode qui renvoie le nombre de territoire que le joueur possède déjà d'un territoire voisin
    public int nbrTerritoireGagneduVoisinGagne(Territoire territoireVoisin, Joueur joueur) {
        int nbr = 0;
        for(Territoire t : this.recupererVoisin(territoireVoisin.getId())) {
            if(t.getJoueurID() == joueur.getID()) {
                nbr ++;
            }
        }
        return nbr;
    }
    

    //fonction permettant de generer une carte random avec les longueurs et largeur le plus proche possible des longueurs et largeur donné en argument
	//et en respectant le nombre de territoire multiple du nombre de Joueur
	//crée aussi un nombre aléatoire de territoire inexistant
    public static void generationCarte(int longueur, int largeur, int nbrJoueur) {
		//le maximum de largeur a été fixé à 7 et de longueur à 10 pour des questions de lisibilité
		if (largeur > 7) {
			largeur = 7;
		}
		if (longueur > 10) {
			longueur = 10;
		}
		List<String> existenceTerritoire = new ArrayList<String>();
		int nbrTerritoire = largeur * longueur;
		Random r = new Random();
		//generation du nombre random de territoire inexistant
		int nbrTerritoireInexistant = r.nextInt(largeur);
		//calcul du nombre reel de territoire disponible
		int nbrReel = nbrTerritoire - nbrTerritoireInexistant;
		//verification que le nombre de territoire dsponible est bien multiple du nombre de joueur, sinon on ajuste le nombre de territoire
		while (nbrReel % nbrJoueur != 0) {
			if (largeur < 7) {
				largeur += 1;
				nbrTerritoireInexistant += longueur - 1;
			} else if (longueur < 10) {
				longueur += 1;
				nbrTerritoireInexistant += largeur - 1;
			} else if ((largeur == 7) && (longueur == 10)) {
				largeur = 1;
				longueur = 1;
				nbrTerritoireInexistant = 0;
			}
			nbrReel = largeur * longueur - nbrTerritoireInexistant;
		}	
		//On utilise une liste pour ajouter des 1 (des territoires existant) le nombre de fois où le territoire existe)
		for (int i = 0; i < nbrReel; i++) {
			existenceTerritoire.add("1");
		}
		//On ajoute dans la liste des 0 (des territoires inexistant) le nombre fois de territoire inexistant
		for (int i = 0; i < nbrTerritoireInexistant; i++) {
			existenceTerritoire.add("0");
		}
		//On mélange l'ordre de la liste afin d'avoir une distribution différente des 0 et des 1
		Collections.shuffle(existenceTerritoire);
		//On crée un nouveau fichier
		File file = new File("src/jeu/csv_type");
		try {
			//creation des outils afin d'écrire dans le fichier
			OutputStream out = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
			//On écrit d'abord la largeur
			writer.append("" + largeur);
			writer.append(" ");
			//Puis la longueur
			writer.append("" + longueur);
			writer.append("\n");
			int compteur = 0;
			//On rempli le tableau selon la liste et selon la longueur et la largeur
			for (int i = 0; i < longueur; i++) {
				for (int j = 0; j < largeur; j++) {
					if (j == largeur - 1) {
						writer.append(existenceTerritoire.get(compteur));
					} else {
						writer.append(existenceTerritoire.get(compteur));
						writer.append(" ");
					}
					compteur++;
				}
				if (i != longueur - 1) {
					writer.append("\n");
				}
			}
			writer.close();
		} // I/O
		catch (IOException e) {
			// error handling
		}
	}
	}


