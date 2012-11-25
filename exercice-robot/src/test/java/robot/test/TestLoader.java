package robot.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import robot.impl.Loader;
import robot.impl.LoaderException;


public class TestLoader {


    //Permet de tester que le fichier existe et peut etre lu 
    @Test public void testVerificationFichierOK() throws LoaderException,IOException{
        Loader loader = new Loader(getClass().getResourceAsStream("/test"));
        assertTrue(loader.verify());
    }
    
    /*//Test fichier n'existe pas
    @Test(expected=LoaderException.class) public void testVerificationFichierPOK() throws LoaderException {
    	Loader loader = new Loader(getClass().getResourceAsStream("/testexistepas"));
    	loader.verify();
    	assert false;
    }*/

    
    //Permet de tester que le fichier existe et peut etre lors de l'initialisation
    @Test public void testVerificationFichierErrInit() throws LoaderException {
    	Loader loader = new Loader(getClass().getResourceAsStream("/testErrInit"));
    	System.out.println("Test: "+loader.verify());
    	assertFalse(loader.verify());
    }
    
    //Test de la récupération des infos sur le terrain OK
    @Test public void testgetTerrainOk() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test"));
    	if (loader.verify()) {
    		int[] test = loader.getTerrain();
    		assertEquals(test[0],3);
    		assertEquals(test[1],3);
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur le terrain sans ligne pour le terrain
    @Test(expected=LoaderException.class) public void testgetTerrainPasDeLigne() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/testVide"));
    	if (loader.verify()) {
    		loader.getTerrain();
    	}else{
    		assert false;
    	}
    }
    
//  Test de la récupération des infos sur le terrain sans ligne pour le terrain
    @Test(expected=LoaderException.class) public void testgetTerrainErrParamNew() throws LoaderException{
//        System.out.println("New Test");
        Loader loader = new Loader(getClass().getResourceAsStream("/test3"));
        if (loader.verify()) {
            loader.getTerrain();
        }else{
            assert false;
        }
    }

    //Test de la récupération des infos sur le terrain avec mauvais nombre parametre
    @Test(expected=LoaderException.class) public void testgetTerrainNbParamErreur() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test3"));
    	if (loader.verify()) {
    		loader.getTerrain();
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur le terrain avec mauvais parametre
    @Test(expected=LoaderException.class) public void testgetTerrainErreurParam() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test2"));
    	if (loader.verify()) {
    		loader.getTerrain();
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur le terrain OK
    @Test public void testgetBaseOk() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test"));
    	if (loader.verify()) {
    		int[] test = loader.getBase();
    		assertEquals(test[0],0);
    		assertEquals(test[1],0);
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur la base sans ligne pour le base
    @Test(expected=LoaderException.class) public void testgetBasePasDeLigne() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/testVideBase"));
    	if (loader.verify()) {
    		loader.getBase();	
    	}else{
    		assert false;
    	}
    }
    //Test de la récupération des infos sur la base avec mauvais nombre parametre
    @Test(expected=LoaderException.class) public void testgetBaseNbParamErreur() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test3"));
    	if (loader.verify()) {
    		loader.getBase();
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur la base avec mauvais parametre
    @Test(expected=LoaderException.class) public void testgetBaseErreurParam() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test2"));
    	if (loader.verify()) {
    		loader.getBase();
    	}else{
    		assert false;
    	}
    }
    
//  Test de la récupération des infos sur le terrain OK
    @Test public void testgetNoeudsOk() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test"));
    	if (loader.verify()) {
    		ArrayList<float[]> test = loader.getNoeuds();
    		assertEquals(test.get(0)[0],0.0,0);
    		assertEquals(test.get(0)[1],1.0,0);
    		assertEquals(test.get(0)[2],13.5,0);
    		assertEquals(test.get(0)[3],1.0,0);
    		assertEquals(test.size(),6);
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur la base sans ligne pour le base
    @Test public void testgetNoeudsPasDeLigne() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test5"));
    	if (loader.verify()) {
    		ArrayList<float[]> test = loader.getNoeuds();
    		assertEquals(test.size(),0);
    	}else{
    		assert false;
    	}
    }
    //Test de la récupération des infos sur la base avec mauvais nombre parametre
    @Test(expected=LoaderException.class) public void testgetNoeudsNbParamErreur() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test4"));
    	if (loader.verify()) {
    		loader.getNoeuds();
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur la base avec mauvais parametre
    @Test(expected=LoaderException.class) public void testgetNoeudsErreurParam() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test2"));
    	if (loader.verify()) {
    		loader.getNoeuds();
    	}else{
    		assert false;
    	}
    }

//  Test de la récupération des infos sur le terrain OK
    @Test public void testgetRobotsOk() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test"));
    	if (loader.verify()) {
    		ArrayList<int[]> test = loader.getRobots();
    		assertEquals(test.get(0)[0],1);
    		assertEquals(test.get(0)[1],2);
    		assertEquals(test.get(0)[2],78);
    		assertEquals(test.get(0)[3],100);
    		assertEquals(test.size(),3);
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur la base sans ligne pour le base
    @Test public void testgetRobotsPasDeLigne() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test5"));
    	if (loader.verify()) {
    		ArrayList<int[]> test = loader.getRobots();
    		assertEquals(test.size(),0);
    	}else{
    		assert false;
    	}
    }
    //Test de la récupération des infos sur la base avec mauvais nombre parametre
    @Test(expected=LoaderException.class) public void testgetRobotsNbParamErreur() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test4"));
    	if (loader.verify()) {
    		loader.getRobots();
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur la base avec mauvais parametre
    @Test(expected=LoaderException.class) public void testgetRobotsErreurParamOrientation() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test2"));
    	if (loader.verify()) {
    		loader.getRobots();
    	}else{
    		assert false;
    	}
    }
    
//  Test de la récupération des infos sur la base avec mauvais parametre
    @Test(expected=LoaderException.class) public void testgetRobotsErreurParamEntier() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test3"));
    	if (loader.verify()) {
    		loader.getRobots();
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur le terrain OK
    @Test public void testgetOrdresOk() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test"));
    	if (loader.verify()) {
    		ArrayList<String[]> test = loader.getOrdres();
    		assertEquals(test.get(0)[0],1,1,0);
    		assertEquals(test.get(0)[1],"GAGADADGDDDG");
    		assertEquals(test.size(),3);
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur la base sans ligne pour le base
    @Test public void testgetOrdresPasDeLigne() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test5"));
    	if (loader.verify()) {
    		ArrayList<String[]> test = loader.getOrdres();
    		assertEquals(test.size(),0);
    	}else{
    		assert false;
    	}
    }
    //Test de la récupération des infos sur la base avec mauvais nombre parametre
    @Test(expected=LoaderException.class) public void testgetOrdresNbParamErreur() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test4"));
    	if (loader.verify()) {
    		loader.getOrdres();
    	}else{
    		assert false;
    	}
    }
    
    //Test de la récupération des infos sur la base avec mauvais parametre
    @Test(expected=LoaderException.class) public void testgetOrdresErreurParamOrdre() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test2"));
    	if (loader.verify()) {
    		loader.getOrdres();
    	}else{
    		assert false;
    	}
    }
    
    @Test(expected=LoaderException.class) public void testgetOrdresErreurParamIdentifiant() throws LoaderException{
    	Loader loader = new Loader(getClass().getResourceAsStream("/test3"));
    	if (loader.verify()) {
    		loader.getOrdres();
    	}else{
    		assert false;
    	}
    }
}
