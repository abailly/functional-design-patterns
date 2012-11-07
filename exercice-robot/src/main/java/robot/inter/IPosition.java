package robot.inter;



/**
 * La classe Position permet de déterminer un position sur un terrain ainsi que l'orientation de l'objet se trouvant sur celui-ci.
 * 
 */
public interface IPosition {

	/**
	 * Retourne l'abscisse.
	 * @return the abscisse.
	 */
	public int getAbscisse();

	/**
	 * Initialise l'abscisse.
	 * @param abscisse the abscisse to set
	 */
	public void setAbscisse(int abscisse) ;

	/**
	 * Retourne l'ordonnee.
	 * @return the ordonnee
	 */
	public int getOrdonnee();

	/**
	 * Initialise l'ordonnee.
	 * @param ordonnee the ordonnee to set
	 */
	public void setOrdonnee(int ordonnee);

	/**
	 * Retourne l'orientation.
	 * @return the orientation
	 */
	public char getOrientation();

	/**
	 * Initialise l'orientation
	 * @param orientation the orientation to set
	 */
	public void setOrientation(char orientation);

	/**
	 * Represente l'objet sous forme de chaine de caractere au format csv.
	 * @return Une chaine de caractere representant l'objet au format csv.
	 */
	public String toCsvString();

	/**
	 * Represente l'objet sous forme de chaine de caractere.
	 * @return Une chaine de caractere representant l'objet.
	 */
	public String toString();
	
	/**
	 * Methode permettant d'effectuer un quart de tour gauche.<br>
	 * Un quart de tour gauche est definit par tourner a gauche a 90° puis avancer d'une position.
	 */
	public void tournerAGauche();
	
	/**
	 * Methode permettant d'effectuer un quart de tour droite.<br>
	 * Un droite de tour droite est definit par tourner a droite a 90° puis avancer d'une position.
	 */
	public void tournerADroite();
	
	/**
	 * Permet d'avancer d'une case.
	 */
	public void avancerDUneCase();

	/**
	 * Permet de déterminer su deux objets sont identiques.
	 * @param L'objet IPosition que l'on souhaite comparer avec l'objet courant.
	 * @return true si les deux objets sont identiques, false sinon.
	 */
	public boolean equals(IPosition ip);


}
