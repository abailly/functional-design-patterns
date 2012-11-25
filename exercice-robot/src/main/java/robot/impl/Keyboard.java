package robot.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Guy Melançon, UPV, Janvier 2004.
 * 
 * Cette classe permet de lire au clavier des valeurs (entières, réelles, caractères, chaînes de caractères)
 * sans avoir à instancier les classe du package java.io
 * 
 * Elle se veut une aide au débutant. Toutes les méthodes sont ainsi statiques,
 * autorisant leur utilisation directe (sans instanciation).
 * 
 * @see java.io.BufferedReader
 * @see java.io.InputStreamReader
 */
public final class Keyboard {

	/** La classe utilise un flot d'entrée statique, commun à toutes les méthodes
	 * (qui peut être utilisée par toutes).*/
	private static InputStreamReader inStream =
		new InputStreamReader(System.in);

	/** La classe utilise un tampon (buffer) de lecture statique, commun à toutes les méthodes
	 * (qui peut être utilisée par toutes).*/
	private static BufferedReader stdin = new BufferedReader(inStream);

	/** La classe utilise une variable Stirng qui stocke ce qui est effectivement lu.
	 * Commun à toutes les méthodes (qui peut être utilisée par toutes).*/
	private static String line = "";

	/** Cette méthode statique permet de lire un entier à partir du clavier,
	 * sans avoir à déclarer et manipuler les objets Java impliqués dans le processus. */
	public static int readInt() {
		try {
			line = stdin.readLine();
			return Integer.parseInt(line);
		} catch (Exception e) {
			System.out.println(
				"Problème à la lecture au clavier d'un entier (la valeur entrée au clavier était : "
					+ line);
			System.exit(-1);
			return Integer.MIN_VALUE;
			/** Pour faire taire le compilateur ... */
		}
	}

	/** Cette méthode statique permet de lire un réel (float) à partir du clavier,
	 * sans avoir à déclarer et manipuler les objets Java impliqués dans le processus. */
	public static float readFloat() {
		try {
			line = stdin.readLine();
			return Float.parseFloat(line);
		} catch (Exception e) {
			System.out.println(
				"Problème à la lecture au clavier d'un réel (float) (la valeur entrée au clavier était : "
					+ line);
			System.exit(-1);
			return Float.MIN_VALUE; /** Pour faire taire le compilateur ... */
		}
	}

	/** Cette méthode statique permet de lire un réel double précision à partir du clavier,
	 * sans avoir à déclarer et manipuler les objets Java impliqués dans le processus. */
	public static double readDouble() {
		try {
			line = stdin.readLine();
			return Double.parseDouble(line);
		} catch (Exception e) {
			System.out.println(
				"Problème à la lecture au clavier d'un réel (double) (la valeur entrée au clavier était : "
					+ line);
			System.exit(-1);
			return Double.MIN_VALUE; /** Pour faire taire le compilateur ... */
		}
	}

	/** Cette méthode statique permet de lire une chaîne de caractères à partir du clavier,
	 * sans avoir à déclarer et manipuler les objets Java impliqués dans le processus.
	 * Notez bien que la méthode retourne un objet String. */
	public static String readString() {
		try {
			return stdin.readLine();
		} catch (Exception e) {
			System.out.println(
				"Problème à la lecture au clavier d'une chaîne de caractères (la valeur entrée au clavier était : "
					+ line);
			System.exit(-1);
			return null; /** Pour faire taire le compilateur ... */
		}
	}

	/** Cette méthode statique permet de lire un caractère à partir du clavier,
	 * sans avoir à déclarer et manipuler les objets Java impliqués dans le processus.
	 * Notez bien que la méthode retourne un caractère (type primitif char). */
	public static char readChar() {
		try {
			line = stdin.readLine();
			char[] charArray = line.toCharArray();
			return charArray[0];
		} catch (Exception e) {
			System.out.println(
				"Problème à la lecture au clavier d'un caractère (la valeur entrée au clavier était : "
					+ line);
			System.exit(-1);
			return Character.MIN_VALUE;
			/** Pour faire taire le compilateur ... */
		}
	}
}
