package robot.impl;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import robot.inter.ILoader;

/**
 * La classe Loader implemente l'interface ILoader et permet d'effectuer les
 * fonctionnalites prevues. dans l'interface

 * @see robot.inter.ILoader
 */
public class Loader implements ILoader {

	/**
	 * Permet de garder en memoire toutes les lignes du fichier.
	 */
	private ArrayList<String[]> inFile;

	/**
	 * Le nom du fichier qui est charge.
	 */
	private BufferedReader in;

	/**
	 * Cree un chargeur de programme a partir d'un fichier.
	 * 
	 * @param prog
	 *            le fichier programme que l'on souhaite utiliser.
	 */
	public Loader(InputStream isr) {
		this.inFile = new ArrayList<String[]>();
		Reader read = new InputStreamReader(isr);
		this.in = new BufferedReader(read);
	}

	/**
	 * Permet de verifier que le fichier passe en parametre respecte bien la
	 * syntaxe des fichiers de programme.
	 * 
	 * @throws LoaderException
	 *             la fonction init peut apporter une erreur.
	 * @return la validite du fichier passe en parametre
	 */
	public boolean verify()  {
		// on effectue les verifications
		try {
			init();
		}
		catch(Exception e){
			System.out.println("Erreur");
			return false;
		}
		return true;
	}

	/**
	 * Permet d'effectuer la lecture du fichier et de ranger les informations du
	 * fichier.
	 * 
	 * @throws LoaderException
	 *             erreur en cas de ligne incorrect.
	 * @return le resultat sous forme booleene.
	 */
	private void init() throws LoaderException {
		try {

			String read = null;
			// System.out.println("test: "+in.ready());
			while ((read = in.readLine()) != null) {
				String[] seperate = read.split(";");
				// on verifie que seul les lettres pouvant etre utilisees se
				// trouvent dans le fichier
				if (seperate[0].equals("t") || seperate[0].equals("b")
						|| seperate[0].equals("n") || seperate[0].equals("r")
						|| seperate[0].equals("o")) {
					inFile.add(seperate);
				} else {
					throw new LoaderException(
							"Le programme comprend une ligne incorrecte,"
									+ " verifier votre programme");
				}
			}
		} catch (IOException ioe) {
			throw new LoaderException(
					"Le programme comprend une ligne incorrecte,"
							+ " verifier votre programme");
		}
		
	}

	/**
	 * Permet d'obtenir les dimension du terrain.
	 * 
	 * @return les dimensions du terrain.
	 * @throws LoaderException
	 *             en cas d'erreur.
	 */
	public int[] getTerrain() throws LoaderException {
		// on recupere la bonne ligne correspondant aux coordonnees du terrain
		boolean find = false;
		int i = 0;
		while (i < inFile.size() && !find) {
			if (inFile.get(i)[0].equals("t")) {
				find = true;
			} else {
				i++;
			}
		}
		// si on a trouve la ligne, on peut faire les verifications
		if (find) {
			String[] terrainLine = inFile.get(i);
			// si le nombre de param est correct, on affecte et on retourne
			// System.out.println("nbElem:"+ terrainLine.length);
			if (terrainLine.length == 3) {
				try {
					int lar = Integer.parseInt(terrainLine[1]);
					int lon = Integer.parseInt(terrainLine[2]);
					return new int[] { lar, lon };
				} catch (NumberFormatException nfe) {
					throw new LoaderException("Erreur init terrain: " + nfe);
				}
			} else {
				// System.out.println("Je passe par ici");
				throw new LoaderException(
						"Erreur lors de l'initilialisation du terrain.\nLe nombre de parametres est incorrect.");
			}
		} else {
			throw new LoaderException(
					"Aucune Ligne concernant le terrain n'est prÈsente dans le fichier.");
		}
	}

	/**
	 * Permet d'obtenir les coordonnees de la base.
	 * 
	 * @throws LoaderException
	 *             en cas d'erreur.
	 * @return la base
	 */
	public int[] getBase() throws LoaderException {
		// on recupere la bonne ligne correspondant aux coordonnees du terrain
		boolean find = false;
		int i = 0;
		while (i < inFile.size() && !find) {
			if (inFile.get(i)[0].equals("b")) {
				find = true;
			} else {
				i++;
			}
		}
		// si on a trouve la ligne, on peut faire les verifications
		if (find) {
			// on recupËre la seconde ligne du fichier
			String[] baseLine = inFile.get(i);
			// on effectue des vÈrifications.
			if (baseLine.length == 3) {
				try {
					int x = Integer.parseInt(baseLine[1]);
					int y = Integer.parseInt(baseLine[2]);
					return new int[] { x, y };
				} catch (NumberFormatException nfe) {
					throw new LoaderException("Erreur init base: " + nfe);
				}
			} else {
				throw new LoaderException(
						"Erreur lors de la crÈation de la base.\nLe nombre de paramËtre est incorrect");
			}
		} else {
			throw new LoaderException(
					"Aucune Ligne concernant la base n'est prÈsente dans le fichier.");
		}
	}

	/**
	 * Permet d'obtenir la liste des noeuds avec tous leurs informations.
	 * 
	 * @throws LoaderException
	 *             en cas d'erreur.
	 * @return la liste de noueds
	 */
	public ArrayList<float[]> getNoeuds() throws LoaderException {
		// on récup?re toutes les lignes correspondantes a un noeud
		ArrayList<String[]> tmp = new ArrayList<String[]>();
		int i = 0;
		while (i < inFile.size()) {
			if (inFile.get(i)[0].equals("n"))
				tmp.add(inFile.get(i));
			i++;
		}
		// on vérifie que l'on a bien trouve des lignes sinon on retourne une
		// liste vide
		if (tmp.size() > 0) {
			ArrayList<float[]> nodeList = new ArrayList<float[]>();
			// pour chaque ligne de noeud, on effectue les vérifications
			for (String[] node : tmp) {
				if (node.length == 5) {
					// on separe les differentes informations
					try {
						int abscisseObstacle = Integer.parseInt(node[1]);
						int ordonneObstacle = Integer.parseInt(node[2]);
						float altitude = Float.parseFloat(node[3]);
						int obstacle = Integer.parseInt(node[4]);
						nodeList.add(new float[] { abscisseObstacle,
								ordonneObstacle, altitude, obstacle });
					} catch (NumberFormatException nfe) {
						throw new LoaderException(
								"Erreur lors de la recuperation des noeuds.\nUn parametre est incorrect");
					}
				} else {
					throw new LoaderException(
							"Erreur lors de la recuperation des noeuds.\nLe nombre de parametre est incorrect");
				}
			}
			// Si on arrive ici, c'est que tout s'est bien passe.On peut
			// retourner la liste de noeuds
			return nodeList;
		} else {
			ArrayList<float[]> nothing = new ArrayList<float[]>();
			return nothing;
		}
	}

	/**
	 * Permet d'obtenir la liste des robots accompagnees de leurs informations.
	 * 
	 * @throws LoaderException
	 *             en cas d'erreur.
	 * @return the robots.
	 */
	public ArrayList<int[]> getRobots() throws LoaderException {
		// on recupere toutes les lignes correspondantes a un noeud
		ArrayList<String[]> tmp = new ArrayList<String[]>();
		int i = 0;
		while (i < inFile.size()) {
			if (inFile.get(i)[0].equals("r"))
				tmp.add(inFile.get(i));
			i++;
		}
		// on vérifie que l'on a bien trouve des lignes sinon on retourne une
		// liste vide
		if (tmp.size() > 0) {
			ArrayList<int[]> robotList = new ArrayList<int[]>();
			// pour chaque ligne de noeud, on effectue les vérifications
			for (String[] robot : tmp) {
				if (robot.length == 5) {
					// on separe les differentes informations
					try {
						int abscisseRobot = Integer.parseInt(robot[1]);
						int ordonneRobot = Integer.parseInt(robot[2]);
						int orientation = 0;
						if (testOrientation(robot[3])) {
							orientation = (int) (robot[3].charAt(0));
						} else {
							throw new LoaderException(
									"Erreur lors de la recuperation des robots.\n"
											+ "L'orientation a un caractere incorrect");
						}
						int batterie = Integer.parseInt(robot[4]);
						robotList.add(new int[] { abscisseRobot, ordonneRobot,
								orientation, batterie });
					} catch (NumberFormatException nfe) {
						throw new LoaderException(
								"Erreur lors de la recuperation des robots."
										+ "\nUn parametre est incorrect");
					}
				} else {
					throw new LoaderException(
							"Erreur lors de la recuperation des robots."
									+ "\nLe nombre de parametre est incorrect");
				}
			}
			// Si on arrive ici, c'est que tout s'est bien passe.On peut
			// retourner la liste de noeuds
			return robotList;
		} else {
			ArrayList<int[]> nothing = new ArrayList<int[]>();
			return nothing;
		}
	}

	/**
	 * Permet de verifier que le parametre de l'orientation est correct.
	 * 
	 * @param orientation
	 *            l'orientation que l'on souhaite affecter au robot.
	 * @return la reponse sous forme booleene.
	 */
	public boolean testOrientation(String orientation) {
		return orientation.equals("N") || orientation.equals("S")
				|| orientation.equals("O") || orientation.equals("E");
	}

	/**
	 * Permet d'obtenir la liste des ordres.
	 * 
	 * @throws LoaderException
	 *             en cas d'erreur.
	 * @return tous les ordres.
	 */
	public ArrayList<String[]> getOrdres() throws LoaderException {
		// on recupere toutes les lignes correspondantes a un noeud
		ArrayList<String[]> tmp = new ArrayList<String[]>();
		int i = 0;
		while (i < inFile.size()) {
			if (inFile.get(i)[0].equals("o"))
				tmp.add(inFile.get(i));
			i++;
		}
		// on verifie que l'on a bien trouve des lignes sinon on retourne une
		// liste vide
		if (tmp.size() > 0) {
			ArrayList<String[]> ordreList = new ArrayList<String[]>();
			for (String[] ordre : tmp) {
				// verifications
				if (ordre.length == 2 || ordre.length == 3) {
					try {
						String listeDesOrdres = "";
						// Sert a verifier que l'identifiant est bien un entier
						Integer.parseInt(ordre[1]);
						if (ordre.length == 3) {
							if (testOrdre(ordre[2])) {
								listeDesOrdres = ordre[2];
							} else {
								throw new LoaderException(
										"Erreur lors de la recuperation des ordres."
												+ "\nLa liste des ordres comporte un caractere incorrecte");
							}
						}
						ordreList
								.add(new String[] { ordre[1], listeDesOrdres });
					} catch (NumberFormatException nfe) {
						throw new LoaderException(
								"Erreur lors de la recuperation des ordres.\n"
										+ "L'identifiant du robot est incorrect");
					}
				} else {
					throw new LoaderException(
							"Erreur lors de la recuperation des ordres.\n"
									+ "Le nombre de parametre est incorrect");
				}
			}
			System.out.flush();
			return ordreList;
		} else {
			ArrayList<String[]> nothing = new ArrayList<String[]>();
			return nothing;
		}
	}

	/**
	 * VÈrifie que la syntaxe de l'ordre est correcte.
	 * 
	 * @param ordre
	 *            l'ordre que l'on souhaite tester.
	 * @return le resultat sous forme booleene.
	 */
	public boolean testOrdre(String ordre) {
		// si on a pas d'ordre, il est quand meme correct
		for (int i = 0; i < ordre.length(); i++) {
			// pour chaque caractere de l'ordre
			String test = ordre.substring(i, i + 1);
			// on verifie qu'il est bien l'un des caractËres utilisable
			if (!test.equals("G") && !test.equals("D") && !test.equals("A"))
				return false;
		}
		// si on arrive ici tout c'est bien passe, donc l'ordre est correct
		return true;
	}
}
