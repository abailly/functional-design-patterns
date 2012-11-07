package robot.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Guy Melan�on, UPV, Janvier 2004.
 * 
 * Cette classe permet de lire au clavier des valeurs (enti�res, r�elles, caract�res, cha�nes de caract�res)
 * sans avoir � instancier les classe du package java.io
 * 
 * Elle se veut une aide au d�butant. Toutes les m�thodes sont ainsi statiques,
 * autorisant leur utilisation directe (sans instanciation).
 * 
 * @see java.io.BufferedReader
 * @see java.io.InputStreamReader
 */
public final class Keyboard {

	/** La classe utilise un flot d'entr�e statique, commun � toutes les m�thodes
	 * (qui peut �tre utilis�e par toutes).*/
	private static InputStreamReader inStream =
		new InputStreamReader(System.in);

	/** La classe utilise un tampon (buffer) de lecture statique, commun � toutes les m�thodes
	 * (qui peut �tre utilis�e par toutes).*/
	private static BufferedReader stdin = new BufferedReader(inStream);

	/** La classe utilise une variable Stirng qui stocke ce qui est effectivement lu.
	 * Commun � toutes les m�thodes (qui peut �tre utilis�e par toutes).*/
	private static String line = "";

	/** Cette m�thode statique permet de lire un entier � partir du clavier,
	 * sans avoir � d�clarer et manipuler les objets Java impliqu�s dans le processus. */
	public static int readInt() {
		try {
			line = stdin.readLine();
			return Integer.parseInt(line);
		} catch (Exception e) {
			System.out.println(
				"Probl�me � la lecture au clavier d'un entier (la valeur entr�e au clavier �tait : "
					+ line);
			System.exit(-1);
			return Integer.MIN_VALUE;
			/** Pour faire taire le compilateur ... */
		}
	}

	/** Cette m�thode statique permet de lire un r�el (float) � partir du clavier,
	 * sans avoir � d�clarer et manipuler les objets Java impliqu�s dans le processus. */
	public static float readFloat() {
		try {
			line = stdin.readLine();
			return Float.parseFloat(line);
		} catch (Exception e) {
			System.out.println(
				"Probl�me � la lecture au clavier d'un r�el (float) (la valeur entr�e au clavier �tait : "
					+ line);
			System.exit(-1);
			return Float.MIN_VALUE; /** Pour faire taire le compilateur ... */
		}
	}

	/** Cette m�thode statique permet de lire un r�el double pr�cision � partir du clavier,
	 * sans avoir � d�clarer et manipuler les objets Java impliqu�s dans le processus. */
	public static double readDouble() {
		try {
			line = stdin.readLine();
			return Double.parseDouble(line);
		} catch (Exception e) {
			System.out.println(
				"Probl�me � la lecture au clavier d'un r�el (double) (la valeur entr�e au clavier �tait : "
					+ line);
			System.exit(-1);
			return Double.MIN_VALUE; /** Pour faire taire le compilateur ... */
		}
	}

	/** Cette m�thode statique permet de lire une cha�ne de caract�res � partir du clavier,
	 * sans avoir � d�clarer et manipuler les objets Java impliqu�s dans le processus.
	 * Notez bien que la m�thode retourne un objet String. */
	public static String readString() {
		try {
			return stdin.readLine();
		} catch (Exception e) {
			System.out.println(
				"Probl�me � la lecture au clavier d'une cha�ne de caract�res (la valeur entr�e au clavier �tait : "
					+ line);
			System.exit(-1);
			return null; /** Pour faire taire le compilateur ... */
		}
	}

	/** Cette m�thode statique permet de lire un caract�re � partir du clavier,
	 * sans avoir � d�clarer et manipuler les objets Java impliqu�s dans le processus.
	 * Notez bien que la m�thode retourne un caract�re (type primitif char). */
	public static char readChar() {
		try {
			line = stdin.readLine();
			char[] charArray = line.toCharArray();
			return charArray[0];
		} catch (Exception e) {
			System.out.println(
				"Probl�me � la lecture au clavier d'un caract�re (la valeur entr�e au clavier �tait : "
					+ line);
			System.exit(-1);
			return Character.MIN_VALUE;
			/** Pour faire taire le compilateur ... */
		}
	}
}
