package robot.inter;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import robot.impl.BaseException;
import robot.impl.LoaderException;
import robot.impl.SimulationException;

/**
 * L'Interface ISimulation permet de simuler la réalisation d'un programme martien.
 * 
 */
public interface ISimulation {
    /**
     * Retourne la liste des robots associes a la simulation.
     * @return La liste des robots associés a la simulation.
     */
    public ArrayList<IRobot> getListeDeRobot();

    /**
     * Permet d'initialiser la liste de robots par la liste passee en parametre.
     * @param listeDeRobot La nouvelle liste de robots. 
     */
    public void setListeDeRobot(ArrayList<IRobot> listeDeRobot);

    /**
     * Retourne le terrain associe a la simulation
     * @return Le terrain associe a la simulation
     */
    public ITerrain getTerrain();

    /**
     * Permet d'initialiser le terrain par le terrain passe en parametre.
     * @param listeDeRobot Le nouveau terrain.
     */
    public void setTerrain(ITerrain terrain);

    /**
     * Methode permettant de lancer l'execution de la simulation. Cette méthode prend en parametre un fichier. Ce fichier correspond a l'ensemble
     * des ordres pour la simulation. A la fin de la simulation, la méthode permet de suavegarder les informations dans un fichier de sauvegarde.
     * @param file Le fichier "programme" qui permet de lancer la simulation.
     */
    public void run(File file) throws LoaderException,FileNotFoundException;

    /**
     * Permet d'initialiser le terrain grace a un tableau d'entier passe en parametre. Ce tableau 
     * doit etre de taille 2.
     * @param infos Tableau de taille 2 qui comporte les tailles du terrain. La premiere case contient les abscisses, la seconde les ordonnees.
     */
    public void initTerrain(int[] infos) throws SimulationException;

    /**
     * Permet d'initialiser les noeuds. Si le taille de l'arrayList n'est pas egale au nombre de noeuds necessaire, la methode retourne une SimulationException.
     * @param infos La liste des infos necessaire a la configuration des noeuds.
     */
    public void initNoeuds(ArrayList<float[]> infos) throws SimulationException;

    /**
     * Permet de positionner la base sur le terrain.
     * @param infos Tableau de taille 2 qui comporte les coordonnees de la base.
     * @throws BaseException 
     */
    public void initBase(int[] infos) throws SimulationException, BaseException;
    /**
     * Permet d'initialiser les robots.
     * @param infos La liste d'informations necessaire a l'initialisation des robots.
     */
    public void initRobots(ArrayList<int[]> infos) throws SimulationException;

    /**
     * Permet d'effectuer l'ensemble des ordres passes en parametre.
     * @param infos L'ensemble des ordres passes en parametre.
     */
    public void routine(ArrayList<String[]> infos) throws SimulationException;

    /**
     * Permet de recuperer une chaine de caractere representant la simulation.
     * @return Retourne une chaine de caractere representant la simulation.
     */
    public String toString();

    /**
     * Permet de récupérer une chaine de caractere representant la simulation sous forme csv.
     * @return la chaine csv de la simulation.
     */
    public String toCsvString();
    /**
     * Retourne la base associée à la simulation.
     * @return La base associée à la simulation.
     */
    public IBase getBase();

    /**
     * Initialise la base associee a la simulation.
     * @param base La base associee a la simulation.
     */
    public void setBase(IBase base);

}
