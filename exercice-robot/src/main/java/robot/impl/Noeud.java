package robot.impl;

import robot.inter.INoeud;

/**
 * Cette classe permet de représenter un noeud.
 *
 */
public class Noeud implements INoeud {
	
	/**
	 * Determine l'altitude a laquelle se trouve le noeud.
	 */
	private float altitude = 0;
	
	/**
     * Determine si le noeud est un obstacle.
     */
	private boolean obstacle = false;
	
	/**
	 * Constructeur initialisant le noeud.
	 * @param altitude Altitude du noeud
	 * @param obstable Le robot ne peut se positionner sur le noeud s'il s'agit d'un obstacle.
	 */
	public Noeud(float altitude, boolean obstacle) {
		this.altitude = altitude;
		this.obstacle = obstacle;
	}

	/**
	 * Retourne l'altitude du noeud
	 * @return altitude du noeud
	 */
	public float getAltitude() {
		return altitude;
	}

	/**
	 * Initialiser l'altitude
	 * @param altitude modifier l'altitude du noeud
	 */
	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	/**
	 * Permet de savoir si un noeud est un obstacle ou non.
	 * @return true si le noeud est un obstacle ; false sinon.
	 */
	public boolean isObstacle() {
		return obstacle;
	}

	/**
	 * Permet de modifier un noeud afin que celui-ci soit un obstacle ou non.
	 * @param obstable statut 'obstacle' a modifier
	 */
	public void setObstacle(boolean obstacle) {
		this.obstacle = obstacle;
	}
	
	
	/**
	 * Permet de representer le noeud par une chaine de caracteres.
	 * @return representation du noeud en une chaine de caracteres.
	 */
	public String toString(){
		if (obstacle)
		{
			return this.altitude+" "+"x";
		}
		else
			return ""+this.altitude;
	}
	
	/**
	 * Permet de comparer deux noeuds du plateau.
	 * @param noeud noeud a comparer.
	 * @return true si les deux noeuds sont identiques, false sinon.
	 */
	public boolean equals(INoeud noeud){
        if(noeud.getAltitude() != this.getAltitude() || noeud.isObstacle() != this.isObstacle())
            return false;
        return true;
    }
	
	/**
	 * Permet de representer un noeud sous forme de chaine de caractere csv.
	 * @return Une chaine de caractère csv representant un noeud.
	 */
	public String toCsvString(){
		if(this.obstacle) return this.getAltitude()+";"+1;
		return this.getAltitude()+";"+0;
	}
}
