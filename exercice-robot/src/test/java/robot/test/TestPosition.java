package robot.test;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

import robot.impl.Position;


public class TestPosition extends MockObjectTestCase {
    
    @Test public void testTournerAGaucheNord() {
        Position pos = new Position(1,2,'N');
        pos.tournerAGauche();
        assertEquals("0 3 O",pos.toString());
    }

    @Test public void testTournerAGaucheSud() {
        Position pos = new Position(1,2,'S');
        pos.tournerAGauche();
        assertEquals("2 1 E",pos.toString());
    }

    @Test public void testTournerAGaucheOuest() {
        Position pos = new Position(1,2,'O');
        pos.tournerAGauche();
        assertEquals("0 1 S",pos.toString());
    }

    @Test public void testTournerAGaucheEst() {
        Position pos = new Position(1,2,'E');
        pos.tournerAGauche();
        assertEquals("2 3 N",pos.toString());
    }

    @Test public void testTournerADroiteNord() {
        Position pos = new Position(1,2,'N');
        pos.tournerADroite();
        assertEquals("2 3 E",pos.toString());
    }

    @Test public void testTournerADroiteSud() {
        Position pos = new Position(1,2,'S');
        pos.tournerADroite();
        assertEquals("0 1 O",pos.toString());
    }

    @Test public void testTournerADroiteOuest() {
        Position pos = new Position(1,2,'O');
        pos.tournerADroite();
        assertEquals("0 3 N",pos.toString());
    }

    @Test public void testTournerADroiteEst() {
        Position pos = new Position(1,2,'E');
        pos.tournerADroite();
        assertEquals("2 1 S",pos.toString());
    }

    @Test public void testAvancerNord() {
        Position pos = new Position(1,2,'N');
        pos.avancerDUneCase();
        assertEquals("1 3 N",pos.toString());
    }

    @Test public void testAvancerSud() {
        Position pos = new Position(1,2,'S');
        pos.avancerDUneCase();
        assertEquals("1 1 S",pos.toString());
    }

    @Test public void testAvancerOuest() {
        Position pos = new Position(1,2,'O');
        pos.avancerDUneCase();
        assertEquals("0;2;O",pos.toCsvString());
    }

    @Test public void testAvancerEst() {
        Position pos = new Position(1,2,'E');
        pos.avancerDUneCase();
        assertEquals("2 2 E",pos.toString());
    }

    /**@Test(expected=IllegalArgumentException.class) public void testMauvaisConstructeur() {
         Position pos = new Position(-4,1,'N');
    }
*/
    @Test public void testSetGetAbscisse() {
        Position pos = new Position(1,2,'E');
        pos.setAbscisse(2);
        assertEquals(2,pos.getAbscisse());
    }

    @Test public void testSetGetOrdonnee() {
        Position pos = new Position(1,2,'E');
        pos.setOrdonnee(3);
        assertEquals(3,pos.getOrdonnee());
    }


    @Test public void testSetGetOrientation() {
        Position pos = new Position(1,2,'E');
        pos.setOrientation('S');
        assertEquals('S',pos.getOrientation());
    }

    /**@Test(expected=IllegalArgumentException.class) public void testOrientationNonValide() {
        Position pos = new Position(1,2,'U');
    }
*/
    @Test public void testEquals() {
         Position pos = new Position(1,2,'E');
         Position pos2 = new Position(1,2,'E');
         assertTrue(pos.equals(pos2));
    }
}
