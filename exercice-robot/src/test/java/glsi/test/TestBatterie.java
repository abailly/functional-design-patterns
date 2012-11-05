package glsi.test;

import junit.framework.TestCase;

import org.junit.Test;

import glsi.impl.Batterie;

public class TestBatterie extends TestCase {
	
	@Test public void testGetAutonomie() {
		Batterie bat = new Batterie(100);
		assertEquals(100,bat.getAutonomie());		
	}
	
	@Test public void testSetAutonomie() {
		Batterie bat = new Batterie(100);
		bat.setAutonomie(150);
		assertEquals(150,bat.getAutonomie());
	}
	
	@Test public void testToString() {
		Batterie bat = new Batterie(100);
		assertEquals("100",bat.toString());
	}
	
	@Test public void testEquals() {
		Batterie bat = new Batterie(150);
		Batterie batt = new Batterie(150);
		assertTrue(bat.equals(batt));
	}
	
	@Test public void testEnergieConsommee() {
		Batterie bat = new Batterie(100);
		assertEquals(555,bat.energieConsommee(2,4));
	}
}
