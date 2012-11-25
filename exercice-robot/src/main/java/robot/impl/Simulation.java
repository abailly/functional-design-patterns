package robot.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;

import robot.inter.IBase;
import robot.inter.ILoader;
import robot.inter.IPosition;
import robot.inter.IRobot;
import robot.inter.ISaver;
import robot.inter.ISimulation;
import robot.inter.ITerrain;



/**
 * La classe Simulation�permet de simuler la r�alisation d'un programme martien . Cette classe prend en lecture un
 * fichier d'ordres et cr�e un fichier de sortie contenant l'�tat du terrain, ou se trouve la base, l'etat des
 * diff�rents noeuds, la localisation des robots.
 *
 */

//Pour le moment ne sont pas pris en compte les robots ayant le meme identifiant.

public class Simulation implements ISimulation {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    private ITerrain terrain = null;
    private ArrayList<IRobot> listeDeRobot = null;
    private IBase base;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors 
    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Permet l'initialisation de la simulation.
     */
    public Simulation() {
        listeDeRobot = new ArrayList<IRobot>();
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Retourne la liste des robots associes a la simulation.
     *
     * @return La liste des robots associ�s a la simulation.
     */
    public ArrayList<IRobot> getListeDeRobot() {
        return listeDeRobot;
    }

    /**
     * Permet d'initialiser la liste de robots par la liste passee en parametre.
     *
     * @param listeDeRobot La nouvelle liste de robots.
     */
    public void setListeDeRobot(ArrayList<IRobot> listeDeRobot) {
        this.listeDeRobot = listeDeRobot;
    }

    /**
     * Retourne le terrain associe a la simulation
     *
     * @return Le terrain associe a la simulation
     */
    public ITerrain getTerrain() {
        return terrain;
    }

    /**
     * Permet d'initialiser le terrain par le terrain passe en parametre.
     *
     * @param terrain listeDeRobot Le nouveau terrain.
     */
    public void setTerrain(ITerrain terrain) {
        this.terrain = terrain;
    }

    /**
     * Retourne la base associ�e � la simulation.
     *
     * @return La base associ�e � la simulation.
     */
    public IBase getBase() {
        return base;
    }

    /**
     * Initialise la base associee a la simulation.
     *
     * @param base La base associee a la simulation.
     */
    public void setBase(IBase base) {
        this.base = base;
    }

    /**
     * Methode permettant de lancer l'execution de la simulation. Cette m�thode prend en parametre un fichier. Ce
     * fichier correspond a l'ensemble des ordres pour la simulation. A la fin de la simulation, la m�thode permet de
     * suavegarder les informations dans un fichier de sauvegarde.
     *
     * @param file Le fichier "programme" qui permet de lancer la simulation.
     */
    public void run(File file) throws LoaderException, FileNotFoundException {
        ILoader action = new Loader(new FileInputStream(file.getName()));
        if (action.verify()) {

            try {
                initTerrain(action.getTerrain()); // initialise le terrain
                initNoeuds(action.getNoeuds()); // initialise l'ensemble des noeuds
                initBase(action.getBase()); // initialise la base
                initRobots(action.getRobots()); // initialise les robots (les positionne)
                routine(action.getOrdres()); // execute les ordres
            } catch (SimulationException s) {
                System.out.println(s);
            } catch (BaseException e) {
                e.printStackTrace();
            }
            ISaver s = new Saver(this); // cree la sauvegarde
            s.askNameFileText(); // demande le nom de sauvegarde
            s.saveIntoFile(); // effectue la sauvegarde
        } else
            System.out.println("Erreur : Le fichier est incorrect.");
    }

    /**
     * Permet d'initialiser le terrain grace a un tableau d'entier passe en parametre. Ce tableau doit etre de taille 2.
     *
     * @param infos Tableau de taille 2 qui comporte les tailles du terrain. La premiere case contient les abscisses, la
     *              seconde les ordonnees.
     */
    public void initTerrain(int[] infos) throws SimulationException {
        if (infos.length != 2)
            throw new SimulationException("ERREUR : Le nombre de parametre pour la cr�ation d'un terrain est strictement egal a 2.");
        if ((infos[0] < 0) || (infos[1] < 0))
            throw new SimulationException("ERREUR : Les elements pour la construction du terrain doivent etre superieurs ou egaux a 0.");
        terrain = new Terrain(infos[0], infos[1]);
        terrain.init();
    }

    /**
     * Permet d'initialiser les noeuds. Si le taille de l'arrayList n'est pas egale au nombre de noeuds necessaire, la
     * methode retourne une SimulationException.
     *
     * @param infos La liste des infos necessaire a la configuration des noeuds.
     */
    public void initNoeuds(ArrayList<float[]> infos) throws SimulationException {
        this.terrain.init();
        for (int i = 0; i < infos.size(); i++) {
            float[] tmp = infos.get(i);
            if (tmp.length != 4)
                throw new SimulationException("ERREUR : Le nombre de parametre pour l'initialisation des noeuds est 3.");
            if (tmp[3] == 1)
                this.terrain.setNoeud((int) tmp[0], (int) tmp[1], tmp[2], true);
            else
                this.terrain.setNoeud((int) tmp[0], (int) tmp[1], tmp[2], false);
        }

    }

    /**
     * Permet de positionner la base sur le terrain.
     *
     * @param  infos Tableau de taille 2 qui comporte les coordonnees de la base.
     *
     * @throws BaseException
     */
    public void initBase(int[] infos) throws SimulationException, BaseException {
        if (infos.length != 2)
            throw new SimulationException("ERREUR : Le nombre de parametre pour l'initialisation de la base est de 2.");
        this.terrain.setBase(infos[0], infos[1]);
        base = Base.getInstance();
        base.setAbscisse(infos[0]);
        base.setOrdonnee(infos[1]);
    }

    /**
     * Permet d'initialiser les robots.
     *
     * @param infos La liste d'informations necessaire a l'initialisation des robots.
     */
    public void initRobots(ArrayList<int[]> infos) throws SimulationException {
        for (int i = 0; i < infos.size(); i++) {
            int[] tmp = infos.get(i);
            if (tmp.length != 4)
                throw new SimulationException("ERREUR : Le nombre de parametre pour l'initialisation des robots est de 4.");
            if (!this.terrain.isLibre(new Position(tmp[0], tmp[1], (char) tmp[2])))
                throw new SimulationException("Erreur : Un robot ou un obstacle est deja present en " + new Position(tmp[0], tmp[1], (char) tmp[2]));
            this.listeDeRobot.add(new Robot(tmp[this.listeDeRobot.size() + 1], new Position(tmp[0], tmp[1], (char) tmp[2]), new Batterie(tmp[3])));
            int x = tmp[0];
            int y = tmp[1];
            float alt = this.terrain.getNoeud(x, y).getAltitude();
            this.terrain.setNoeud(x, y, alt, true);
        }
    }

    /**
     * Permet d'effectuer l'ensemble des ordres passes en parametre.
     *
     * @param infos L'ensemble des ordres passes en parametre.
     */
    public void routine(ArrayList<String[]> infos) throws SimulationException {
        for (int i = 0; i < infos.size(); i++) { // pour chaque suite d'ordre.
            for (int k = 0; k < infos.get(i)[1].length(); k++) { // pour chaque ordre (char)
                IRobot robot = this.listeDeRobot.get(Integer.parseInt(infos.get(i)[0]) - 1);
                IPosition save = robot.getPosition(); // on sauvegarde la position
                robot.ordre(infos.get(i)[1].charAt(k)); // on effectue l'ordre
                IPosition pos = robot.getPosition(); // on recup la position courante
                if ((pos != null) && this.terrain.isLibre(pos)) { // si new pos pas ok
                    robot.setPosition(save); // on remet le robot sur son ancienne position
                }
            }
        }
    }

    /**
     * Permet de recuperer une chaine de caractere representant la simulation.
     *
     * @return Retourne une chaine de caractere representant la simulation.
     */
    public String toString() {
        String s = new String();
        s += "Terrain :\n" + this.terrain + "\n";
        s += "Robot :\n";
        for (int i = 0; i < this.getListeDeRobot().size(); i++)
            s += this.getListeDeRobot().get(i) + "\n";
        return s;
    }

    /**
     * Permet de r�cup�rer une chaine de caractere representant la simulation sous forme csv.
     *
     * @return la chaine csv de la simulation.
     */
    public String toCsvString() {
        String csv = "";
        csv += this.terrain.toCsvString();
        for (IRobot robot : this.listeDeRobot) {
            csv += "r;" + robot.getPosition().toCsvString() + ";" + robot.getBatterie().toString();
        }
        csv += "\n";
        return csv;
    }
}
