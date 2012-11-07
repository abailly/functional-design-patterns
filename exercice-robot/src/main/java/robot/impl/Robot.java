package robot.impl;

import robot.inter.IBatterie;
import robot.inter.IPosition;
import robot.inter.IRobot;

/**
 * La classe Robot permet de representer un robot.
 * Un robot est definit par un identifiant, une IPosition
 * ainsi que par une IBatterie.
 */

public class Robot implements IRobot, Cloneable {

    /**
     * L'identifiant du robot.
     */
    private int identifiant = 0;

    /**
     * La position du robot.
     */
    private IPosition position = null;

    /**
     * La batterie du robot.
     */
    private IBatterie batterie = null;

    //private static int VITESSE = 0;

    /**
     *Permet d'initialiser un robot.
     *@param id L'identifiant du robot.
     *@param pos La position du robot.
     *@param bat La batterie du robot.
     */
    public Robot(final int id, final IPosition pos, final IBatterie bat) {
        if (id >= 0) {
                this.identifiant = id;
                this.position = pos;
                this.batterie = bat;
        } else {
            throw new IllegalArgumentException("erreur constructeur Robot");
        }
    }

    /**
     * Retourne la batterie du robot.
     *@return la batterie du robot
     */
    public IBatterie getBatterie() {
        return batterie;
    }
    /**
     * Change la batterie du robot.
     *@param bat une nouvelle batterie
     */
    public void setBatterie(final IBatterie bat) {
        this.batterie = bat;
    }
    /**
     * Retourne l'identifiant du robot.
     *@return l'identifiant du robot
     */
    public int getIdentifiant() {
        return identifiant;
    }
    /**
     * Change l'identifiant du robot.
     *@param id un nouvel identifiant
     */
    public void setIdentifiant(final int id) {
        this.identifiant = id;
    }
    /**
     * Retourne la position du robot.
     *@return la position du robot
     */
    public IPosition getPosition() {
        return position;
    }
    /**
     * Change la position du robot.
     *@param pos une nouvelle position
     */
    public void setPosition(final IPosition pos) {
        this.position = pos;
    }

    /**
     * Permet d'effectuer un deplacement.
     * @param card le deplacement a effectuer
     */

    public void ordre(final char card) {
        switch(card) {
        case 'G': position.tournerAGauche();
            break;
        case 'D': position.tournerADroite();
            break;
        case 'A' :position.avancerDUneCase();
            break;
        default : break;
        }
    }

    /**
     * Donne une representation du robot en chaine de caracteres.
     *@return la representation
     */
    public String toString() {
        return position.toString() + " " + this.batterie.toString();
    }

    /**
     * Donne une representation du robot en csv.
     *@return la representation
     */
    public String toCsvString() {
        return position.toCsvString() + ";" + this.batterie.toString();
    }

    /**
     * Verifie que 2 robots sont egaux.
     *@param ir l'autre robot
     *@return true si les robots sont egaux
     */
    public boolean equals(final IRobot ir) {
        return (ir.getIdentifiant() == identifiant
                && ir.getPosition().equals(position)
                && ir.getBatterie().equals(batterie));
    }

    /**
     * Retourne le hashcode du robot.
     *@return le hashcode du robot
     */
    public int hashCode() {
        return getIdentifiant() ^ getPosition().hashCode()
            ^ getBatterie().hashCode();
    }
}
