package glsi.test;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

import glsi.impl.Batterie;
import glsi.impl.Robot;
import glsi.inter.IBatterie;
import glsi.inter.IPosition;

public class TestRobot extends MockObjectTestCase {

    @Test public void testTournerAGauche() {
	   
	Mock mock = new Mock(IPosition.class);
	mock.expects(once()).method("tournerAGauche");
	Robot r = new Robot(1,(IPosition)mock.proxy(),new Batterie(100));
	r.ordre('G');
	mock.verify();
    }

    @Test public void testTournerADroite() {
	Mock mock = new Mock(IPosition.class);
	mock.expects(once()).method("tournerADroite");
	Robot r = new Robot(1,(IPosition)mock.proxy(),new Batterie(100));
	r.ordre('D');
	mock.verify();
    }

    @Test public void testAvancerDUneCase() {
	Mock mock = new Mock(IPosition.class);
	mock.expects(once()).method("avancerDUneCase");
	Robot r = new Robot(1,(IPosition)mock.proxy(),new Batterie(100));
	r.ordre('A');
	mock.verify();
    }
    
    @Test public void testEquals() {

	Mock mockPos = new Mock(IPosition.class);
	Mock mockBat = new Mock(IBatterie.class);
	Robot nono = new Robot(1,(IPosition)mockPos.proxy(),(IBatterie)mockBat.proxy());
	Robot no = new Robot(1,(IPosition)mockPos.proxy(),(IBatterie)mockBat.proxy());
	assertTrue(nono.equals(no));
    }
	
    @Test public void testToCsvString() {

	Mock mockPos = new Mock(IPosition.class);
	Mock mockBat = new Mock(IBatterie.class);
	    
	mockPos.expects(once()).method("toCsvString").will(returnValue("3;4;N"));
	mockBat.expects(once()).method("toString").will(returnValue("100"));
	Robot nono = new Robot(1,(IPosition)mockPos.proxy(),(IBatterie)mockBat.proxy());
	assertEquals("3;4;N;100",nono.toCsvString());
    }
	
    @Test public void testToString() {
	Mock mockPos = new Mock(IPosition.class);
	Mock mockBat = new Mock(IBatterie.class);
	    
	mockPos.expects(once()).method("toString").will(returnValue("3 4 N"));
	mockBat.expects(once()).method("toString").will(returnValue("100"));
	Robot nono = new Robot(1,(IPosition)mockPos.proxy(),(IBatterie)mockBat.proxy());
	assertEquals("3 4 N 100",nono.toString());
    }

    /*
    @Test public void testHashCode() {
        Mock mockPos = new Mock(IPosition.class);
	Mock mockBat = new Mock(IBatterie.class);
	    
	mockPos.expects(once()).method("hashCode").will(returnValue("10"));
	mockBat.expects(once()).method("hashCode").will(returnValue("12"));
	Robot nono = new Robot(1,(IPosition)mockPos.proxy(),(IBatterie)mockBat.proxy());
        int hash = 1 ^ 10 ^ 12;
        assertEquals(hash,nono.hashCode());
    }
    */

    @Test public void testSetGetIdentifiant() {
        Robot nono = new Robot(1,null,null);
        nono.setIdentifiant(4);
        assertEquals(4,nono.getIdentifiant());
    }
    
    @Test public void testSetGetBatterie() {
	Mock mockBat = new Mock(IBatterie.class);
        Robot nono = new Robot(1,null,null);
        nono.setBatterie((IBatterie) mockBat.proxy());
        assertEquals(mockBat.proxy(),nono.getBatterie());
    }

    /**@Test(expected=IllegalArgumentException.class) public void testMauvaisConstructeur() {
         Robot nono = new Robot(-4,null,null);
    }
*/
    @Test public void testSetGetPosition() {
	Mock mockPos = new Mock(IPosition.class);
        Robot nono = new Robot(1,null,null);
        nono.setPosition((IPosition) mockPos.proxy());
        assertEquals(mockPos.proxy(),nono.getPosition());
    }

}

