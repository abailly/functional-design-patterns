/**
 * $Id: $
 * $Change:  $
 *
 * Copyright 2012 Murex, S.A. All Rights Reserved.
 *
 * This software is the proprietary information of Murex, S.A.
 * Use is subject to license terms.
 */
/**
 *
 */
package glsi.test;

import glsi.impl.Noeud;

import glsi.inter.INoeud;

import org.junit.Test;


public class TestNoeud {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    @Test
    public void isObstacle() {
        INoeud noeud = new Noeud(17, false);
        org.junit.Assert.assertEquals(false, noeud.isObstacle());
    }

    @Test
    public void setObstacle() {
        INoeud noeud = new Noeud(17, false);
        noeud.setObstacle(true);
        noeud.toString();
        org.junit.Assert.assertEquals(true, noeud.isObstacle());
    }

    @Test
    public void setGetAltitude() {
        INoeud noeud = new Noeud(17, false);
        noeud.setAltitude((float) 19.0);
        org.junit.Assert.assertEquals((float) 19.0, noeud.getAltitude(), 0.00001);
    }

    @Test
    public void equals() {
        INoeud noeud = new Noeud(17, false);
        INoeud noeud2 = new Noeud(19, true);
        org.junit.Assert.assertFalse(noeud.equals(noeud2));
    }

    @Test
    public void stringObstacle() {
        INoeud noeud = new Noeud(17, true);
        String chaine = "17.0 x";
        org.junit.Assert.assertEquals(chaine, noeud.toString());
    }

    @Test
    public void stringNonObstacle() {
        INoeud noeud = new Noeud(17, false);
        String chaine = "17.0";
        org.junit.Assert.assertEquals(chaine, noeud.toString());
    }

    @Test
    public void testToCsvStringObstacle() {
        INoeud noeud = new Noeud(17, true);
        String chaine = "17.0;1";
        org.junit.Assert.assertEquals(chaine, noeud.toCsvString());
    }

    @Test
    public void testToCsvStringNonObstacle() {
        INoeud noeud = new Noeud(17, false);
        String chaine = "17.0;0";
        org.junit.Assert.assertEquals(chaine, noeud.toCsvString());
    }

}
