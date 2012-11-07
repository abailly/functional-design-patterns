/**
 * 
 */
package robot.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import robot.inter.ISaver;
import robot.inter.ISimulation;


/**
 *
 */
public class Saver implements ISaver {

    private String nameFile = null;
    private ISimulation simulation = null;


    public Saver(ISimulation simulation) {
        this.simulation = simulation;
    }


    public Saver() {
    }


    /**
     * @return the nameFile
     */
    public String getNameFile() {
        return nameFile;
    }


    /**
     * @param nameFile the nameFile to set
     */
    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }


    /**
     * @return the simulation
     */
    public ISimulation getSimulation() {
        return simulation;
    }

    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.ISaver#askNameFileText()
     */
    public void askNameFileText() {

        //demande du nom du fichier
        System.out.println("Veuillez choisir le nom du fichier d'enregistrement (sans l'extension): ");
        System.out.print("> ");
        String nameTemp = Keyboard.readString();
        File test = new File(nameTemp+".txt");
        if(test.exists()) {
            //si le fichier existe, on demande si on le garde quand meme ou non
            System.out.println("Le fichier existe deja.");
            String rep ="";
            while(!rep.equals("oui") && !rep.equals("non")) {
                //on demande si on ecrase le fichier ou non
                System.out.println("Voulez-vous changer de nom pour eviter d'ecraser le fichier ? (oui/non)");
                System.out.print("> ");
                rep = Keyboard.readString();
            }
            if(rep.equals("oui")) {
                //si on change de nom on rappelle la methode
                askNameFileText();
            }else{
                this.setNameFile(nameTemp);
            }
        }else {
            //si le fichier n'existe pas on affecte le nom
            this.setNameFile(nameTemp);
        }	
    }

    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.ISaver#saveIntoFile()
     */
    public boolean saveIntoFile() {
            PrintWriter out;
            try {
                out = new PrintWriter(getNameFile()+".txt");
                out.write(getSimulation().toCsvString());
                out.flush();
                out.close();
                return true;
            } catch (FileNotFoundException e) {
                return false;
            }
            
    }

    /**
     * @param simulation the simulation to set
     */
    public void setSimulation(ISimulation simulation) {
        this.simulation = simulation;
    }
}
