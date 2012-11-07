package robot.inter;

public interface INoeud {

	/**
	 * Retourne l'altitude du noeud
	 * @return altitude du noeud
	 */
	public abstract float getAltitude();

	/**
	 * Initialiser l'altitude
	 * @param altitude modifier l'altitude du noeud
	 */
	public abstract void setAltitude(float altitude);

	/**
	 * Permet de savoir si un noeud est un obstacle ou non.
	 * @return true si le noeud est un obstacle ; false sinon.
	 */
	public abstract boolean isObstacle();

	/**
	 * Permet de modifier un noeud afin que celui-ci soit un obstacle ou non.
	 * @param obstable statut 'obstacle' a modifier
	 */
	public abstract void setObstacle(boolean obstable);

	/**
	 * Permet de representer le noeud par une chaine de caracteres.
	 * @return representation du noeud en une chaine de caracteres.
	 */
	public abstract String toString();

	/**
	 * Permet de comparer deux noeuds du plateau
	 * @param noeud noeud a comparer
	 * @return true si les deux noeuds sont identiques, false sinon.
	 */
	public abstract boolean equals(INoeud noeud);
	
	/**
	 * Permet de representer un noeud sous forme de chaine de caractere csv.
	 * @return Une chaine de caractère csv representant un noeud.
	 */
	public String toCsvString();

}
