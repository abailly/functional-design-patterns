/**
 * $Id: $
 * $Change:  $
 *
 * Copyright 2012 Murex, S.A. All Rights Reserved.
 *
 * This software is the proprietary information of Murex, S.A.
 * Use is subject to license terms.
 */
package robot.test;

import junit.framework.TestCase;

import org.junit.Test;

import robot.impl.Batterie;


public class TestBatterie extends TestCase {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetAutonomie() {
        Batterie bat = new Batterie(100);
        assertEquals(100, bat.getAutonomie());
    }

    @Test
    public void testSetAutonomie() {
        Batterie bat = new Batterie(100);
        bat.setAutonomie(150);
        assertEquals(150, bat.getAutonomie());
    }

    @Test
    public void testToString() {
        Batterie bat = new Batterie(100);
        assertEquals("100", bat.toString());
    }

    @Test
    public void testEquals() {
        Batterie bat = new Batterie(150);
        Batterie batt = new Batterie(150);
        assertTrue(bat.equals(batt));
    }

    @Test
    public void testEnergieConsommeeEnMontant() {
        Batterie bat = new Batterie(100);
        assertEquals(5.55, bat.energieConsommee(2, 4), 0.01);
    }

    @Test
    public void testEnergieConsommeeEnDescendant() {
        Batterie bat = new Batterie(100);
        assertEquals(2.46, bat.energieConsommee(4, 3), 0.01);
    }
}
