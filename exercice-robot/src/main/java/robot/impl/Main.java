package robot.impl;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Collections;

import robot.inter.ISaver;
import robot.inter.ISimulation;

/**
 * La classe Main permet d'executer l'application. Elle offre un menu a
 * l'utilisateur lui proposant diff�rents choix:
 * 
 * <ul>
 * <li>Effectuer une simulation au moyen de fichiers : un fichier en entree et
 * un fichier en sortie.</li>
 * <li>Effectuer une simulation grace a la ligne de commande. Dans ce cas,
 * l'utilisateur doit saisir l'ensemble des informations au fur et a mesure en
 * utilisant les commandes mis a se disposition (voir plus loin).</li>
 * <li>Quitter l'application</li>
 * </ul>
 * 
 * Pour l'interface en ligne de commande, l'utilisateur doit utiliser les
 * commandes suivantes :
 * 
 * <ul>
 * <li>creerPlateau pour la cr�ation du terrain.</li>
 * <li>positionnerBase pour positionner la base.</li>
 * <li>initNoeud pour initialiser un noeud.</li>
 * <li>envoieInitNoeud pour envoyer l'initialisation lorsque tous les noeuds ont
 * �t� saisie.</li>
 * <li>creerRobot pour cr�er un robot.</li>
 * <li>ordre pour donner un ordre � un robot.</li>
 * <li>exit pour quitter l'interface.</li>
 * </ul>
 * 
 */
public class Main {

	// ~
	// ----------------------------------------------------------------------------------------------------------------
	// ~ Constructors
	// ~
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Permet de lancer l'application.
	 * 
	 * @throws FileNotFoundException
	 */
	public Main() throws LoaderException, SimulationException,
			FileNotFoundException {
		System.out
				.println("\nBienvenue sur l'application de gestion de robots martiens.\n");
		menu();

	}

	// ~
	// ----------------------------------------------------------------------------------------------------------------
	// ~ Methods
	// ~
	// ----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) throws LoaderException,
			SimulationException, FileNotFoundException {
		new Main();
	}

	/**
	 * Methode permettant l'affichage de l'aide pour la gestion par fichier.
	 */
	public void helpFichier() {
		System.out.println("");
		System.out.println("\t\tAide Fichier");
		System.out.println("NAME");
		System.out.println("\t \n");
		System.out.println("SYNOPSIS");
		System.out.println("\t \n");
		System.out.println("DESCRIPTION");
		System.out.println("\tA venir...");
		System.out.println("");
	}

	public void save() {

	}

	/**
	 * Methode permettant d'afficher l'aide en ligne de commandes.
	 */
	public void helpCmde() {
		System.out.println("");
		System.out.println("\t\tAide Lignes de Commandes");
		System.out.println("NAME");
		System.out.println("\t \n");
		System.out.println("SYNOPSIS");
		System.out.println("\tinitPlateau abscisse;ordonn�e");
		System.out.println("\tinitBase abscisse;ordonn�e");
		System.out.println("\tinitNoeud abscisse;ordonn�e;altitude;obstacle");
		System.out.println("\tenvoieInitNoeud");
		System.out
				.println("\tinitRobot abscisse;ordonn�e;orientation;batterie");
		System.out.println("\tordre idRobot;listeOrdre");
		System.out.println("\tsave");
		System.out.println("\tload");
		System.out.println("\tafficherSimulation");
		System.out.println("\thelp");
		System.out.println("\texit\n");
		System.out.println("DESCRIPTION");
		System.out
				.println("\tL'ensemble des commandes permettent de g�rer la simulation.");
		System.out
				.println("\tLa commande 'initPlateau' permet de cr�er et d'initialiser un plateau. Pour ce faire,"
						+ "il faut fournir en parametre l'abscisse et l'ordonn�e de la facon suivante : abscisse;ordonn�e.");
		System.out
				.println("\tLa commande 'initBase' permet de positionner la base sur le terrain."
						+ "Pour positionner une base, il suffit de passer les coordonn�es de la base en parametre de la "
						+ "mani�re suivante : abscisse;ordonn�e.");
		System.out
				.println("\tLa commande 'initNoeud' permet d'initialiser un noeud sur du terrain. Il faut respecter le "
						+ "formalisme suivant abscisse;ordonnee;altitude;obstacle. L'abscisse et l'ordonn�e doivent etre des entiers,"
						+ "l'altitude est un nombre r�el. Obstacle ne peut prendre que deux valeurs : 0 si le noeud n'est pas un obstacle,"
						+ " 1 sinon.");
		System.out
				.println("\tUne fois que tous les noeuds sont initialis�s, on les prends en compte pour la simulation"
						+ "grace � la commande 'envoieInitNoeud'");
		System.out
				.println("\tPour pouvoir cr�er un robot, il faut utiliser la commande 'initRobot'"
						+ "avec les parametres suivants : abscisse;ordonn�e;orientation;batterie. Abscisse et ordonn�e sont des entiers."
						+ "Orientation repr�sente l'orientation du robot. Les valeurs possibles sont 'N' pour le nord, 'O' pour l'ouest"
						+ ", 'S' pour le sud et pour l'est 'E'. Enfin, on indique le niveau de batterie par un entier.");
		System.out
				.println("\tPour donner un ordre � un robot, il faut utiliser la commande 'ordre' avec le formalisme suivant"
						+ " idRobot;listeOrdre. idRobot repr�sente l'identifiant du robot. listeOrdre est une suite de caract�re repr�sentant les diff�rents "
						+ "ordre que l'on passe au robot. Les caract�res permit sont 'A' pour faire avancer le robot d'une position, 'G' pour faire avancer-gauche-avancer au robot, et "
						+ "enfin 'D' pour faire avancer-droite-avancer.");
		System.out
				.println("\tPour sauver la simulation, il faut utiliser la commande 'save'");
		System.out
				.println("\tPour charger un fichier, utilisez la commande 'load'");
		System.out
				.println("\t'afficherSimulation' permet d'afficher la simulation.");
		System.out
				.println("\tPour afficher l'aide, employez la commande 'help'");
		System.out
				.println("\tPour terminer la session en ligne de commande, utilisez la commande 'exit'");
		System.out.println("");

	}

	/**
	 * Menu propose a l'utilisateur.
	 * 
	 * @throws FileNotFoundException
	 */
	private void menu() throws LoaderException, SimulationException,
			FileNotFoundException {
		int choix = 0;
		while ((choix < 1) || (choix > 3)) {
			System.out.println("Menu :");
			System.out.println("1 - Gestion via fichier.");
			System.out.println("2 - Gestion via ligne de commande.");
			System.out.println("3 - Quitter.");
			System.out.print("Choix (Valider par enter) : ");
			choix = Keyboard.readInt();
		}
		switch (choix) {

		case 1:
			gestionFichier();
			break;

		case 2:
			// TODO: gestionLigneCmd();
			break;

		case 3:
			fin();
		}

	}

	/**
	 * Ensemble des commandes permettant le lancement et le deroulement d'une
	 * simulation au moyen de fichiers.
	 * 
	 * @throws FileNotFoundException
	 */
	private void gestionFichier() throws LoaderException, SimulationException,
			FileNotFoundException {
		ISimulation s = new Simulation();
		System.out.print("Veuillez saisir le nom de votre fichier : ");
		String fileName = Keyboard.readString();
		if (fileName.equals("exit")) {
			this.menu();
		} else if (fileName.equals("help")) {
			this.helpFichier();
			this.gestionFichier();
		} else {
			File file = new File(fileName);
			s.run(file);
		}
		menu();

	}

	/**
	 * Ensemble des commandes permettant le lancement et le deroulement d'une
	 * simulation en ligne de commandes
	 * 
	 * @throws FileNotFoundException
	 */
	private void gestionLigneCmd() throws LoaderException, SimulationException,
			FileNotFoundException {
		ArrayList<float[]> listeNoeud = new ArrayList<float[]>();
		System.out.println("Mode Ligne de Commande.\n");
		System.out.println("Pour quitter, tapez \"exit\"");
		String ordre = "";
		ISimulation s = new Simulation();
		while (!ordre.equals("exit")) { // tant que l'utilisateur ne souhaite
										// pas quitter.
			System.out.print("> ");
			ordre = Keyboard.readString();
			String[] tmp = ordre.split(" ");
			if ((tmp.length != 2) || !tmp[0].equals("envoieInitNoeud")) { // on
																			// teste
																			// sa
																			// saisie
				if (tmp[0].equals("initPlateau")) { // il souhaite init un
													// plateau
					tmp = tmp[1].split(";");
					int[] terrain = new int[] { Integer.parseInt(tmp[0]),
							Integer.parseInt(tmp[1]) };
					try {
						s.initTerrain(terrain);
					} catch (SimulationException e) {
						System.err.println(e);
					}
				} else {
					if (tmp[0].equals("initBase")) { // il souhaite
														// positionner la base
						tmp = tmp[1].split(";");
						int[] base = new int[] { Integer.parseInt(tmp[0]),
								Integer.parseInt(tmp[1]) };
						try {
							s.initBase(new int[0]);
						} catch (SimulationException e) {
							System.err.println(e);
						} catch (BaseException e) {
							System.err.println(e);
						}
					} else {
						if (tmp[0].equals("initNoeud")) { // init d'un noeud
							tmp = tmp[1].split(";");
							float[] noeud = new float[] {
									Float.parseFloat(tmp[0]),
									Float.parseFloat(tmp[1]),
									Float.parseFloat(tmp[2]),
									Float.parseFloat(tmp[3]) };
							listeNoeud.add(noeud);
						} else {
							if (tmp[0].equals("envoieInitNoeud")) { // envoie
																	// des
																	// noeuds a
																	// la
																	// simulation
								try {
									s.initNoeuds(new ArrayList<float[]>());
								} catch (SimulationException e) {
									System.err.println(e);
								}
							} else {
								if (tmp[0].equals("initRobot")) { // creation
																	// d'un
																	// robot
									tmp = tmp[1].split(";");
									int[] robot = new int[] {
											Integer.parseInt(tmp[0]),
											Integer.parseInt(tmp[1]),
											(int) (tmp[2].charAt(0)),
											Integer.parseInt(tmp[3]) };
									ArrayList<int[]> listeRobot = new ArrayList<int[]>();
									listeRobot.add(robot);
									try {
										s.initRobots(listeRobot);
									} catch (SimulationException e) {
										System.err.println(e);
									}
								} else {
									if (tmp[0].equals("ordre")) { // sequence
																	// d'ordre
										tmp = tmp[1].split(";");
										String[] action = new String[] {
												tmp[0], tmp[1] };
										ArrayList<String[]> listeAction = new ArrayList<String[]>();
										listeAction.add(action);
										try {
											s.routine(listeAction);
										} catch (SimulationException e) {
											System.err.println(e);
										}
									} else {
										if (tmp[0].equals("save")) {
											ISaver save = new Saver(s);
											save.askNameFileText(); // demande
																	// le nom de
																	// sauvegarde
											save.saveIntoFile(); // effectue
																	// la
																	// sauvegarde
										} else {
											if (tmp[0].equals("help")) {
												helpCmde();
											} else {
												if (tmp[0].equals("load")) {
													System.out
															.println("A venir");
												} else {
													if (tmp[0]
															.equals("afficherSimulation")) {
														try {
															System.out
																	.println("\n"
																			+ s);
														} catch (NullPointerException e) {
															throw new SimulationException(
																	"La simulation n'est pas initialis�e.");
														}
													} else {
														this.helpCmde();
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				System.err.println("Erreur dans la saisie de la commande.");
			}
		}
		System.out.println("Resultat de la simulation : \n" + s);
		menu();
	}

	/**
	 * Methode de fin d'application.
	 */
	private void fin() {
		System.out.println("-- Bye --");
		System.exit(1);
	}

}
