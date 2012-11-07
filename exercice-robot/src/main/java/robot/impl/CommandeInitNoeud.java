/**
 * 
 */
package robot.impl;

import robot.inter.ICommande;
import robot.inter.ISimulation;

public class CommandeInitNoeud implements ICommande{
	/**
     * 
     *
     */
	public CommandeInitNoeud() {
		
	}
	
	/**
     * Cette méthode permet d'executer les instructions provenant de Main.
     * @throws SimulationException 
     */
    public void execute(String s,ISimulation simul) throws SimulationException {
        String[] tmp = s.split(" ");
        String[] infos = tmp[1].split(";");
        if(infos.length != 4)
			throw new SimulationException("ERREUR : Le nombre de parametre pour la création d'un noeud est strictement egal a 4.");
		if(Integer.parseInt(infos[0]) < 0 || Integer.parseInt(infos[1])<0 || Integer.parseInt(infos[2])<0 || Float.parseFloat(infos[2])<0 && (infos[3].equals("0")||infos[3].equals("1")))
			throw new SimulationException("ERREUR : Les elements pour la construction de la base doivent etre superieurs ou egaux a 0.");
		
		boolean obstacle=false;
		if(infos[3].equals("1"))
			obstacle=true;
		simul.getTerrain().setNoeud(Integer.parseInt(infos[0]), Integer.parseInt(infos[1]), Float.parseFloat(infos[2]), obstacle);
    }
    /**
     * Permet d'analyser une chaine de caractere pour savoir si elle correspond
     * a ce type d'ordre.
     * @param s La chaine de caractere que l'on souhaite analyser.
     * @return true si c'est la bonne classe, false sinon.
     */
    public boolean analyse(String s) {
        String[] tmp = s.split(" ");
        return tmp[0].equals("creationBase");
    }
}
