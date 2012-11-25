/**
 * $Id: $
 * $Change:  $
 *
 * Copyright 2012 Murex, S.A. All Rights Reserved.
 *
 * This software is the proprietary information of Murex, S.A.
 * Use is subject to license terms.
 */
package robot.inter;

public interface IBatterie {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.IBatterie#getAutonomie()
     */
    int getAutonomie();

    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.IBatterie#setAutonomie(int)
     */
    void setAutonomie(int autonomie);

    /* (non-Javadoc)
     * @see fr.licence.agl.robot.impl.IBatterie#toString()
     */
    String toString();

    boolean equals(IBatterie ib);

    double energieConsommee(int hauteurA, int hauteurB);

}
