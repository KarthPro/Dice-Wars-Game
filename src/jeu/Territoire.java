package jeu;

import java.io.Serializable;

public class Territoire implements Serializable{
    private static int statidId=0;
    private int id;
    private int joueurID;
    private int nbDe;
	private boolean exist;
	private boolean jouer; // util pour l'interface, savoir si le territoire a deja jouer pendant le tour

    
    
    public Territoire(int exist) {
    	if (exist == 1)
	    	this.exist = true;
	    else
	    	this.exist = false;
   	 	this.id=statidId++;
    }
    
    

    public int getId() {
   	 return id;
    }

    public int getJoueurID() {
   	 return joueurID;
    }

    public void setJoueurID(int joueurID) {
   	 this.joueurID = joueurID;
    }


    public int getNbDe() {
   	 return nbDe;
    }


    public void setNbDe(int nbDe) {
   	 this.nbDe = nbDe;
    }

    public void setExist(boolean exist) {
			this.exist = exist;
		}
    
    public boolean getExist() {
    	return this.exist;
    }
    


	public static void resetStatidId() {
		Territoire.statidId = 0;
	}



	@Override
	public String toString() {
		return "Territoire [id=" + id + ", joueurID=" + joueurID + ", nbDe=" + nbDe + ", exist=" + exist + "]";
	}



	public boolean isJouer() {
		return jouer;
	}



	public void setJouer(boolean jouer) {
		this.jouer = jouer;
	}

    
}

