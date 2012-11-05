package glsi.test;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

import glsi.impl.Saver;
import glsi.inter.ISimulation;


public class TestSaver extends MockObjectTestCase{

	
	@Test public void testSaveIntoFile() {
		//Creation d'un mock Simulation
		Mock mock = new Mock(ISimulation.class);
		String resultat="t;3;3\nb;0;0\n";
		resultat+="n;0;1;13.5;1\nn;0;2;4.5;0\nn;1;1;13.5;0\nn;1;2;4.5;0\nn;0;3;13.5;0\nn;1;0;4.5;0\n";
		resultat+="r;1;2;N;100\nr;3;2;S;75\nr;3;0;O;80\n";
		resultat+="o;1;GAGADADGDDDG\no;2;GADGAGDGADGA\no;3\n";
		mock.expects(once()).method("toCsvString").will(returnValue(resultat));
		Saver saver = new Saver();
        saver.setSimulation((ISimulation)mock.proxy());
		saver.setNameFile("testSaver");
		assertTrue(saver.saveIntoFile());
		mock.verify();
	}
}
