package glsi.impl;
import glsi.inter.IPosition;


/**
 * La classe Position permet de déterminer un position sur un terrain ainsi que l'orientation de l'objet se trouvant sur celui-ci.
 * 
 * @author glsi
 *
 */
public class Position implements IPosition {
	private int abscisse = 0;
	private int ordonnee = 0;
	private char orientation = 0; // ou char (je laisse le choix au developpeur)

	private static char[] TAB_CARDINALITE = new char[4];
	static{
		TAB_CARDINALITE[0]='N';
		TAB_CARDINALITE[1]='E';
		TAB_CARDINALITE[2]='S';
		TAB_CARDINALITE[3]='O';
	}

	/**
	 * Initialise un objet Position
	 * @param abscisse L'abscisse de la position.
	 * @param ordonnee L'ordonnee de la position.
	 * @param orientation L'orientation associée a la position.
	 */
	public Position(int abscisse, int ordonnee, char orientation) {
		super();
		if(abscisse >= 0 && ordonnee >= 0 && orientationValide(orientation))
		{
			this.abscisse = abscisse;
			this.ordonnee = ordonnee;
			this.orientation = orientation;
		}
		else
		{
			throw new IllegalArgumentException("erreur constructeur Position");
		}
	}

	/**
	 * Retourne l'abscisse.
	 * @return the abscisse.
	 */
	public int getAbscisse() {
		return abscisse;
	}

	/**
	 * Initialise l'abscisse.
	 * @param abscisse the abscisse to set
	 */
	public void setAbscisse(int abscisse) {
		this.abscisse = abscisse;
	}

	/**
	 * Retourne l'ordonnee.
	 * @return the ordonnee
	 */
	public int getOrdonnee() {
		return ordonnee;
	}

	/**
	 * Initialise l'ordonnee.
	 * @param ordonnee the ordonnee to set
	 */
	public void setOrdonnee(int ordonnee) {
		this.ordonnee = ordonnee;
	}

	/**
	 * Retourne l'orientation.
	 * @return the orientation
	 */
	public char getOrientation() {
		return orientation;
	}

	/**
	 * Initialise l'orientation
	 * @param orientation the orientation to set
	 */
	public void setOrientation(char orientation) {
		this.orientation = orientation;
	}

	/**
	 * Represente l'objet sous forme de chaine de caractere au format csv.
	 * @return Une chaine de caractere representant l'objet au format csv.
	 */
	public String toCsvString(){
		return abscisse+";"+ordonnee+";"+orientation;
	}

	/**
	 * Represente l'objet sous forme de chaine de caractere.
	 * @return Une chaine de caractere representant l'objet.
	 */
	public String toString(){
		return abscisse+" "+ordonnee+" "+orientation;
	}

	/**
	 * Methode permettant d'effectuer un quart de tour gauche.<br>
	 * Un quart de tour gauche est definit par tourner a gauche a 90° puis avancer d'une position.
	 */
	public void tournerAGauche() {


		switch(orientation){
		case 'N' : ordonnee++;
		abscisse--;
		break;
		case 'S' : ordonnee--;;
		abscisse++;
		break;
		case 'O' : abscisse--;
		ordonnee--;
		break;
		case 'E' : abscisse++;
		ordonnee++;
		break;
		}
		int indice = -1;
		for(int i=0;i<4;i++){
			if(TAB_CARDINALITE[i]==orientation) 
				indice=i;
		}
		indice = (indice+3)%4;
		orientation=TAB_CARDINALITE[indice];
	}
	
	/**
	 * Methode permettant d'effectuer un quart de tour droite.<br>
	 * Un droite de tour droite est definit par tourner a droite a 90° puis avancer d'une position.
	 */
	public void tournerADroite() {
		switch(orientation){
		case 'N' : ordonnee++;
		abscisse++;
		break;
		case 'S' : ordonnee--;
		abscisse--;
		break;
		case 'O' : abscisse--;
		ordonnee++;
		break;
		case 'E' : abscisse++;
		ordonnee--;
		break;
		}
		int indice = -1;
		for(int i=0;i<4;i++){
			if(TAB_CARDINALITE[i]==orientation) 
				indice=i;
		}
		indice = (indice+1)%4;
		orientation=TAB_CARDINALITE[indice];
	}
	/**
	 * Permet d'avancer d'une case.
	 */
	public void avancerDUneCase() {
		switch(orientation){
		case 'N' : ordonnee++;
		break;
		case 'S' : ordonnee--;
		break;
		case 'O' : abscisse--;
		break;
		case 'E' : abscisse++;
		break;
		}
	}

	/**
	 * Permet de déterminer su deux objets sont identiques.
	 * @param L'objet IPosition que l'on souhaite comparer avec l'objet courant.
	 * @return true si les deux objets sont identiques, false sinon.
	 */
	public boolean equals(IPosition ip)
	{
		return ip.getAbscisse() == abscisse && ip.getOrdonnee() == ordonnee && ip.getOrientation() == orientation;
	}

	/**
	 * Permet de déterminier si une orientation est valide ou non.
	 * @param caractere
	 * @return true si l'orientation est valide, false sinon.
	 */
	private boolean orientationValide(char caractere)
	{
		for (int i=0;i<TAB_CARDINALITE.length;i++)
			if(caractere == TAB_CARDINALITE[i])
				return true;
		return false;
	}
}
