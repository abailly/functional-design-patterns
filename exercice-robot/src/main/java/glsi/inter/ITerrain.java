package glsi.inter;

import glsi.impl.BaseException;

public interface ITerrain {

	/**
	 * Retourne le plateau sur lequel evolue le robot.
	 * @return tableau de noeud
	 */
	public abstract INoeud[][] getPlateau();

	/**
	 * Initialise le tableau de noeud
	 * @param plateau tableau de noeud
	 */
	public abstract void setPlateau(INoeud[][] plateau);

	/**
	 * Initialise le tableau de noeuds a la valeur nulle.
	 */
	public abstract void init();

	/**
	 * Permet de savoir si un robot peut se positionner a cette case
	 * @param x abscisse du noeud
	 * @param y ordonnee du noeud
	 * @return true si le robot peut se positionner sur une case ; false sinon.
	 */
	public abstract boolean isLibre(int x, int y);

	/**
	 * Permet de savoir si un noeud est a l'exterieur du terrain
	 * @param x abscisse du noeud
	 * @param y ordonne du noeud
	 * @return true si le noeud est a l'exterieur du terrain, false sinon.
	 */
	public abstract boolean isOut(int x, int y);

	/**
	 * Permet une representation du terrain en une chaine de caracteres
	 * @return representation du terrain en une chaine de caracteres
	 */
	public abstract String toString();

	/**
	 * Compare deux terrains
	 * @param terrain terrain a comparer
	 * @return true si les deux terrains sont identiques ; false sinon.
	 */
	public abstract boolean equals(ITerrain terrain);
	
	/**
	 * Permet retourner un noeud du terrain
	 * @param x abscisse du noeud
	 * @param y ordonnee du noeud
	 * @return le noeud recherche
	 */
	public abstract INoeud getNoeud(int x, int y);
	
	/**
	 * initialise un noeud du terrain
	 * @param x abscisse du noeud
	 * @param y ordonne du noeud
	 * @param altitude altitude du noeud
	 * @param obstacle savoir si un noeud est un obstacle ou non
	 */
	public void setNoeud(int x, int y, float altitude, boolean obstacle);

	public void setNoeud(int x, int y, INoeud noeud);
	/**
	 * Permet d'initialiser la base a une position
	 * @param x abscisse de la base
	 * @param y ordonne de la base
	 */
	public void setBase(int x, int y) throws BaseException;
	
	/**
	 * 
	 * @param position
	 * @return
	 */
	public abstract boolean isLibre(IPosition position);

	/**
	 * 
	 * @param position
	 * @return
	 */
	public abstract boolean isOut(IPosition position);
	
	/**
	 * 
	 * @return
	 */
	public String toCsvString ();
	
	/**
	 * 
	 */
	public int getAbscisse();
	
	/**
	 * 
	 */
	public int getOrdonnee();
	public INoeud getBase();}
