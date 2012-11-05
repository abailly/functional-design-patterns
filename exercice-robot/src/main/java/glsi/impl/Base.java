package glsi.impl;

import java.util.ArrayList;

import glsi.inter.IBase;
import glsi.inter.IRobot;

/**
 * Cette classe définit la base dans laquelle les robots sont recharges.
 * Une base peut contenir un nombre illimite de robots.
 * Elle est définit par une abscisse et une ordonnée.
 * @author glsi
 */
public class Base extends Noeud implements IBase {

	/* Unique instance de la base */
	public static IBase base;
	/* Liste des robots contenue dans la base */ 
	protected ArrayList<IRobot> listeRobots;

	private int abscisse=-1;
	private int ordonnee=-1;



	/** Initialise la liste des robots presents dans la base */
	private Base ()
	{
		super(0, false);
		listeRobots = new ArrayList<IRobot>(5);
	}

	/**
	 * Permet de retourner l'instance de classe.
	 * @return L'instance de classe.
	 */
	public static IBase getInstance()
	{
		if (base == null)
			base = new Base();
		return base;
	}

	/**
	 * Liste des robots contenue dans la base.
	 */
	public ArrayList<IRobot> getListeRobots()
	{
		return listeRobots;
	}

	/**
	 * Initialiser une liste de robots.
	 * @param listeRobots liste des robots a initialiser
	 */
	public void setListeRobots(ArrayList<IRobot> listeRobots)
	{
		this.listeRobots=listeRobots;
	}

	/**
	 * Permet de recuperer un robot dans la base et
	 * d'augmenter son energie au maximum
	 * @param r robor a recuperer
	 * @return true si le robot a bien ete recupere ; false sinon
	 */
	public boolean recupererRobot (IRobot r)
	{
		return listeRobots.add(r);		
		// Gestion energie : augmenter energie robot a 100%...
	}

	/**
	 * Permet de liberer un robot de la base, c'est-a-dire
	 * de le relacher sur le terrain
	 * @param r robot a relacher
	 * @return true si le robot a bien ete relache ; false sinon
	 */
	public boolean libererRobot (IRobot r)
	{
		return listeRobots.remove(r);
	}

	/**
	 * Representation d'une base en une chaine de caracteres
	 */
	public String listeRobots ()
	{
		String tmp = "";
		for (int i=0 ; i<this.listeRobots.size() ; i++)
			tmp = this.listeRobots.get(i)+"\n";
		return tmp;	
	}	

	/**
	 * Permet de representer la Base au moyen d'une chaine de caractère.
	 * @return Une chaine de caractere permettant de representer la base.
	 */
	public String toString ()
	{
		return "B";
	}


	/**
	 * Permet de comparer deux bases.
	 * @param base a comparer.
	 * @return true si les deux bases sont identiques ; false sinon.
	 */
	public boolean equals(IBase base){
		if (base.getListeRobots().size() != this.getListeRobots().size())
			return false;
		for(int i=0; i<this.getListeRobots().size(); i++)
			if(base.getListeRobots().get(i) != this.getListeRobots().get(i))
				return false;
		return true;
	}

	/**
	 * Permet de représenter la base sous forme csv.
	 * @return La base sous forme de chaine de caractere csv.
	 */
	public String toCsvString()
	{
		return "b;"+this.abscisse+";"+this.ordonnee;
	}

	/**
	 * Retourne l'abscisse de la base.
	 * @return L'abscisse de la base.
	 */
	public int getAbscisse() {
		return abscisse;
	}

	/**
	 * Initialise l'absicisse.
	 * @param La valeur de l'abscisse.
	 */
	public void setAbscisse(int abscisse) {
		this.abscisse = abscisse;
	}

	/**
	 * Retourne l'ordonnee de la base.
	 * @return L'ordonne de la base.
	 */
	public int getOrdonnee() {
		return ordonnee;
	}

	/**
	 * Initialise l'ordonnee de la base.
	 * @param La valeur de l'ordonnee
	 */
	public void setOrdonnee(int ordonnee) {
		this.ordonnee = ordonnee;
	}

} // fin classe Base
