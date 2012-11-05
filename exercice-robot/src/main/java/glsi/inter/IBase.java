package glsi.inter;

import java.util.ArrayList;

/**
 * Cette classe permet de positionner une base sur le terrain.
 * @author GLSI
 *
 */
public interface IBase extends INoeud{

	/**
	 * Liste des robots contenue dans la base.
	 */
	public abstract ArrayList<IRobot> getListeRobots();

	/**
	 * Initialiser une liste de robots.
	 * @param listeRobots liste des robots a initialiser
	 */
	public abstract void setListeRobots(ArrayList<IRobot> listeRobots);

	/**
	 * Permet de recuperer un robot dans la base et
	 * d'augmenter son energie au maximum
	 * @param r robor a recuperer
	 * @return true si le robot a bien ete recupere ; false sinon
	 */
	public abstract boolean recupererRobot(IRobot r);

	/**
	 * Permet de liberer un robot de la base, c'est-a-dire
	 * de le relacher sur le terrain
	 * @param r robot a relacher
	 * @return true si le robot a bien ete relache ; false sinon
	 */
	public abstract boolean libererRobot(IRobot r);

	/**
	 * Representation d'une base en une chaine de caracteres
	 */
	public abstract String toString();

	/**
	 * Permet de comparer deux bases.
	 * @param base a comparer
	 * @return true si les deux bases sont identiques ; false sinon.
	 */
	public abstract boolean equals(IBase base);
	
	/**
	 * Representation d'une base en une chaine de caracteres
	 */
	public String listeRobots ();
	/**
	 * Permet de représenter la base sous forme csv.
	 * @return La base sous forme de chaine de caractere csv.
	 */
	public String toCsvString();

	/**
	 * Retourne l'abscisse de la base.
	 * @return L'abscisse de la base.
	 */
	public int getAbscisse();

	/**
	 * Initialise l'absicisse.
	 * @param La valeur de l'abscisse.
	 */
	public void setAbscisse(int abscisse);

	/**
	 * Retourne l'ordonnee de la base.
	 * @return L'ordonne de la base.
	 */
	public int getOrdonnee();

	/**
	 * Initialise l'ordonnee de la base.
	 * @param La valeur de l'ordonnee
	 */
	public void setOrdonnee(int ordonnee);

}
