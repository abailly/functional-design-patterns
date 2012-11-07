package robot.inter;

import robot.impl.SimulationException;

/**
 * 
 */
public interface ICommande {
    /**
     * Cette méthode permet d'executer les instructions provenant de Main.
     */

    public void execute(String s,ISimulation simul) throws SimulationException;


    /**
     * Permet d'analyser une chaine de caractere pour savoir si elle correspond
     * a ce type d'ordre.
     * @param s La chaine de caractere que l'on souhaite analyser.
     * @return true si c'est la bonne classe, false sinon.
     */
    public boolean analyse(String s); 
}
