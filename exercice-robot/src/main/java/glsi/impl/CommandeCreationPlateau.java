/**
 * 
 */
package glsi.impl;

import glsi.inter.ICommande;
import glsi.inter.ISimulation;
import glsi.inter.ITerrain;

/**
 * @author Allienne Aurélien
 * 13 mars 07
 */
public class CommandeCreationPlateau implements ICommande {
    /**
     * 
     *
     */
    public CommandeCreationPlateau() {

    }
    /**
     * Cette méthode permet d'executer les instructions provenant de Main.
     * @throws SimulationException 
     */
    public void execute(String s,ISimulation simul) throws SimulationException {
        String[] tmp = s.split(" ");
        String[] infos = tmp[1].split(";");
        if(infos.length != 2)
			throw new SimulationException("ERREUR : Le nombre de parametre pour la création d'un terrain est strictement egal a 2.");
		if(Integer.parseInt(infos[0]) < 0 || Integer.parseInt(infos[1])<0)
			throw new SimulationException("ERREUR : Les elements pour la construction du terrain doivent etre superieurs ou egaux a 0.");
		ITerrain terrain = new Terrain(Integer.parseInt(infos[0]), Integer.parseInt(infos[1]));
		terrain.init();
        simul.setTerrain(terrain);
    }
    /**
     * Permet d'analyser une chaine de caractere pour savoir si elle correspond
     * a ce type d'ordre.
     * @param s La chaine de caractere que l'on souhaite analyser.
     * @return true si c'est la bonne classe, false sinon.
     */
    public boolean analyse(String s) {
        String[] tmp = s.split(" ");
        return tmp[0].equals("creationPlateau");
    }
}
