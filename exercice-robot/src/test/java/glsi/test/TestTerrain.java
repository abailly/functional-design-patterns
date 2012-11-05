/**
 * 
 */
package glsi.test;

import static org.junit.Assert.assertTrue;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

import glsi.impl.Batterie;
import glsi.impl.Noeud;
import glsi.impl.Position;
import glsi.impl.Robot;
import glsi.impl.Terrain;
import glsi.inter.INoeud;
import glsi.inter.IPosition;
import glsi.inter.ITerrain;

public class TestTerrain extends MockObjectTestCase{
	
	@Test public void isOut()
	{
	    ITerrain terrain = new Terrain (3,4);
	    assertTrue(terrain.isOut(5,5));
	}
	
	@Test public void isLibre()
	{
	    ITerrain terrain = new Terrain (3,4);
	    assertTrue(terrain.isLibre(1,2));
	}
	
	@Test public void testIsLibrePosition()
	{
		ITerrain terrain = new Terrain (3,4);
		terrain.setNoeud(1,1,0,false);
		IPosition p = new Position(1,1,'N');
		assertTrue(terrain.isLibre(p));
	}
	
	@Test public void testIsOutPosition()
	{
		ITerrain terrain = new Terrain (3,4);
		IPosition p = new Position(8,8,'N');
		assertTrue(terrain.isOut(p));
	}
	
	@Test public void getPlateau()
	{
	    ITerrain terrain = new Terrain (3,4);
	    INoeud[][] p = new INoeud[4][5];
	    org.junit.Assert.assertEquals(terrain.getPlateau(),p);
	}
	
	/*@Test public void testInit()
	{
		ITerrain t = new Terrain (3,4);
		ITerrain t2 = new Terrain(3,4);
		t2.setPlateau(t.getPlateau());
		org.junit.Assert.assertTrue(t.equals(t2));
	}*/
	
	@Test public void setPlateau()
	{
	    ITerrain terrain = new Terrain (3,4);
	    INoeud[][] p = new INoeud[4][5];
	    terrain.setPlateau(p);
	    org.junit.Assert.assertEquals(terrain.getPlateau(),p);
	}
	
	@Test public void equals()
    {
		Terrain t1 = new Terrain (3,4);
		Terrain t2 = new Terrain (4,4);
		org.junit.Assert.assertFalse(t1.equals(t2));
    }
    
    @Test public void getNoeud()
	{
	    ITerrain terrain = new Terrain (3,4);	    
	    org.junit.Assert.assertEquals(terrain.getNoeud(1,1),null);
	}
	
	@Test public void setNoeud()
	{
	    ITerrain t = new Terrain (3,4);
	    INoeud noeud = new Noeud (0,false);
	    t.setNoeud(3,2,0,false);
	    org.junit.Assert.assertEquals(t.getNoeud(3,2),noeud);
	}

    @Test public void testString()
    {
		ITerrain terrain = new Terrain (0,0);
		String chaine="0.0 | \n";
		org.junit.Assert.assertEquals(chaine,terrain.toString());
    }
    
    @Test public void testIsLibreNoeudPasLibre(){
    	Terrain t = new Terrain(3,3);
    	Mock mPosition = new Mock(IPosition.class);
    	mPosition.expects(once()).method("getAbscisse").will(returnValue(1));
    	mPosition.expects(once()).method("getOrdonnee").will(returnValue(1));
    	
    	Mock mNoeud = new Mock(INoeud.class);
    	t.setNoeud(1,1,(INoeud)mNoeud.proxy());
    	mNoeud.expects(once()).method("isObstacle").will(returnValue(1));
    	
    	
    }
    
    @Test public void testIsOut() {
 	   
    	Mock mock = new Mock(IPosition.class);
    	mock.expects(once()).method("getAbscisse").will(returnValue(5));
    	mock.expects(once()).method("getOrdonnee").will(returnValue(5));
    	Terrain t = new Terrain(3,4);
    	t.isOut((IPosition)mock.proxy());
    	mock.verify();
        }
    
    @Test public void testIsLibre() {
  	   
    	Mock mock = new Mock(IPosition.class);
    	mock.expects(once()).method("getAbscisse").will(returnValue(1));
    	mock.expects(once()).method("getOrdonnee").will(returnValue(2));
    	Terrain t = new Terrain(3,4);
    	t.isLibre((IPosition)mock.proxy());
    	mock.verify();
        }
}	