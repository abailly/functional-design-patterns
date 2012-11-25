/**
 * $Id: $
 * $Change:  $
 *
 * Copyright 2012 Murex, S.A. All Rights Reserved.
 *
 * This software is the proprietary information of Murex, S.A.
 * Use is subject to license terms.
 */
package robot.impl;

import robot.inter.IBatterie;


/**
 */
public class Batterie implements IBatterie {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Static fields/initializers 
    //~ ----------------------------------------------------------------------------------------------------------------

    /** permet le calcul de l'energie consommee. */
    private static final float CONSTANTE_GRAV = (float) 3.7;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    /** represente l'autonomie de la batterie. */
    private int autonomie;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors 
    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Constructeur.
     *
     * @param auto autonomie de la batterie
     */
    public Batterie(final int auto) {
        this.autonomie = auto;
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.IBatterie#getAutonomie()
     */
    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.IBatterie#getAutonomie()
     */

    /**
     * Retourne l'autonomie de la batterie.
     *
     * @return retourne la valeur de l'autonomie de la batterie
     */
    public int getAutonomie() {
        return autonomie;
    }

    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.IBatterie#setAutonomie(int)
     */
    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.IBatterie#setAutonomie(int)
     */
    /**
     * Permet de modifier l'autonomie de la batterie.
     *
     * @param auto nouvelle autonomie de la batterie
     */
    public void setAutonomie(final int auto) {
        this.autonomie = auto;
    }

    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.IBatterie#toString()
     */
    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.IBatterie#toString()
     */
    /**
     * Retourne l'autonomie de la batterie.
     *
     * @return retourne l'autonomie dans une chaine de caracteres
     */
    public String toString() {
        return autonomie + "";
    }

    /**
     * verifie l'egalite entre deux batterie.
     *
     * @param  ib batterie a tester
     *
     * @return retourne vrai si ib est identique a la batterie
     */
    public boolean equals(final IBatterie ib) {
        return ib.getAutonomie() == autonomie;
    }

    /**
     * calcule l'energie consommee lors d'un deplacement d'un noeud A a un noeud B.
     *
     * @param  hauteurA hauteur du noeud A
     * @param  hauteurB hauteur du noeud B
     *
     * @return retourne la valeur de l'energie consommee a soustraire a l'autonomie de la batterie
     */
    public double energieConsommee(final int hauteurA, final int hauteurB) {
        double res = (float) (hauteurB - hauteurA);
        res /= (float) hauteurB;
        res += 1;
        res *= CONSTANTE_GRAV;
        return res;

    }

}
