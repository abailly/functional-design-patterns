/**
 * 
 */
package glsi.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import glsi.impl.CommandeCreationPlateau;
import glsi.impl.SimulationException;
import glsi.impl.Terrain;
import glsi.inter.INoeud;
import glsi.inter.ISimulation;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;
import org.jmock.core.MockObjectSupportTestCase;
import org.junit.Test;

/**
 * @author Allienne Aurélien 13 mars 07
 */
public class TestCommandeCreationPlateau /*extends MockObjectTestCase*/ {

	@Test
	public void testAnalyseOK() {
		String s = "creationPlateau 5;5";
		CommandeCreationPlateau ccp = new CommandeCreationPlateau();
		assertTrue(ccp.analyse(s));
	}

	@Test
	public void testAnalyseKO() {
		String s = "creation";
		CommandeCreationPlateau ccp = new CommandeCreationPlateau();
		assertFalse(ccp.analyse(s));
	}

	@Test(expected = SimulationException.class)
	public void testExecutionKO1() throws SimulationException {
		String s = "creationPlateau 3;";
		CommandeCreationPlateau ccp = new CommandeCreationPlateau();
		Mock mock = new Mock(ISimulation.class);
		ccp.execute(s, (ISimulation) mock.proxy());
	}

	@Test(expected = SimulationException.class)
	public void testExecutionKO2() throws SimulationException {
		String s = "creationPlateau 3;3;3";
		CommandeCreationPlateau ccp = new CommandeCreationPlateau();
		Mock mock = new Mock(ISimulation.class);
		ccp.execute(s, (ISimulation) mock.proxy());
		assert true;
	}

	/*@Test
	public void testExecutionOK() throws SimulationException {
		String s = "creationPlateau 3;3";
		CommandeCreationPlateau cpp = new CommandeCreationPlateau();
		Mock mock = new Mock(ISimulation.class);
		mock.expects(once()).method("setTerrain").with(checkTerrain());
		if (cpp.analyse(s)) {
			cpp.execute(s, (ISimulation) mock.proxy());
		}
		mock.verify();
	}

	private Constraint checkTerrain() {
		return new Constraint() {

			public StringBuffer describeTo(StringBuffer arg0) {
				return arg0.append("verification du terrain");
			}

			public boolean eval(Object arg0) {
				Terrain terrain = (Terrain) arg0;
				INoeud[][]n = terrain.getPlateau();
				boolean b = terrain.getPlateau().length == 4
						&& terrain.getPlateau()[0].length == 4 && !terrain.getPlateau()[0][0].isObstacle();
				return b;
			}

		};
	}*/
}
