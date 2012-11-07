package robot.inter;

import java.io.File;
import java.util.ArrayList;

import robot.impl.LoaderException;


/**
 * La classe ILoader specifie ce que la classe de
 * chargement doit faire pour pouvoir recuperer.
 * toutes les informations qui sont contenues dans le
 * programme de la simulation qui est un fichier texte.
 *
 */
public interface ILoader {

    /**
     * Permet de verifier que le fichier existe et qu'il puisse etre lu.
     * Verifie aussi si la structure du fichier est bonne
     * ou non (pas d'espace dans les lignes ..).
     * @throws LoaderException en cas d'erreur avec le fichier.
     * @return le resultat du test sous forme booleene.
     */
    boolean verify() throws LoaderException;

    /**
     * Permet de recuperer les dimensions du terrain.
     * @throws LoaderException en cas d'erreur lors de la
     * recuperation des informations du terrain.
     * @return un tableau d'entier qui donne la taille du terrain.
     */
    int[] getTerrain() throws LoaderException;

    /**
     * Permet de recuperer les coordonnees de la base.
     * @throws LoaderException en cas d'erreur de la base.
     * @return un tableau qui contient les coordonnees de la base.
     */
    int[] getBase() throws LoaderException;

    /**
     * Permet de recuperer les differents noeuds present sur le terrain.
     * @throws LoaderException en cas d'erreur avec les noeuds.
     * @return une liste qui contient toutes les coordonnees de tous les noeuds.
     */
    ArrayList < float[] > getNoeuds() throws LoaderException;

    /**
     * Permet de recuperer la liste de tous les robots.
     * @throws LoaderException en cas d'erreur avec les robots.
     * @return la liste des robots avec leurs coordonnees.
     */
    ArrayList < int[] > getRobots() throws LoaderException;

    /**
     * Permet d'obtenir la liste de tous les ordres.
     * @throws LoaderException en cas d'erreur avec les ordres.
     * @return la liste des ordres.
     */
    ArrayList < String[] > getOrdres() throws LoaderException;

    /**
     * Vérifie que la syntaxe de l'ordre est correcte.
     * @param ordre l'ordre que l'on souhaite tester.
     * @throws LoaderException.
     * @return le resultat sous forme booleene.
     */
    boolean testOrdre(String ordre);

    /**
     * Permet de verifier que le parametre de l'orientation est correct.
     * @param orientation l'orientation que l'on souhaite affecter au robot.
     * @throws LoaderException.
     * @return la reponse sous forme booleene.
     */
    boolean testOrientation(String orientation);
}
