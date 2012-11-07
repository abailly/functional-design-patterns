package robot.impl;

import robot.inter.IBase;
import robot.inter.INoeud;
import robot.inter.IPosition;
import robot.inter.ITerrain;

/**
 * Cette classe permet de definir les proprietes d'un terrain. Un terrain est
 * constitue d'un tableau de noeuds ; ces derniers representant les differents
 * elements (Robots, Base...) du Terrain.
 */
public class Terrain implements ITerrain {
	/**
     * Plateau ou seront regoupes les differents elements (Base, Robots...)
     */
	private INoeud[][] plateau = null;
	
	private IBase base = null;

	/**
     * Constructeur permettant d'initialiser un terrain
     * @param plateau
     */
	public Terrain(INoeud[][] plateau) {
//		 this.plateau = plateau;
        this.plateau = new INoeud[plateau.length][plateau[0].length];
		for (int i=0 ; i< plateau.length ; i++)
            for (int j=0 ; j< plateau[0].length ; j++)
                this.plateau[i][j]=plateau[i][j];
	}

	/**
	 * Constructeur initialisant le terrain
	 * @param maxX abscisse maximale 
	 * @param maxY ordonne maximale
	 */
    public Terrain(int x, int y) {
    	plateau = new INoeud[x+1][y+1];
    	base = Base.getInstance();
    	this.init();
    }

	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.ITerrain#getPlateau()
	 */
	public INoeud[][] getPlateau() {
		INoeud[][] newplateau = new INoeud[this.plateau.length][this.plateau[0].length];
        for (int i=0 ; i< this.plateau.length ; i++)
            for (int j=0 ; j< this.plateau[0].length ; j++)
                newplateau[i][j]=this.plateau[i][j];
        return newplateau;
	}

	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.ITerrain#setPlateau(fr.licence.agl.robot.impl.INoeud[][])
	 */
	public void setPlateau(INoeud[][] plateau) {
//		 this.plateau = plateau;
        this.plateau = new INoeud[plateau.length][plateau[0].length];
        for (int i=0 ; i< plateau.length ; i++)
            for (int j=0 ; j< plateau[0].length ; j++)
                this.plateau[i][j]=plateau[i][j];
	}
	
	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.ITerrain#init()
	 */
	public void init(){
    	for (int i=0 ; i<this.plateau.length ; i++)
    		for (int j=0 ; j<this.plateau[0].length ; j++)
    			this.plateau[i][j]=new Noeud(0,false);
    	// ici doit etre ajoute une base (Noeud particulier), relie a la lecture des donnees de l'utilisateur
		
	}
	
	
	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.ITerrain#isLibre(int, int)
	 */
	public boolean isLibre(int x, int y){
		if(x<0 || y<0 || x>=this.plateau.length || y>=this.plateau[0].length)
			return false;
		return (! this.plateau[x][y].isObstacle()) && (! this.isOut(x,y));
	}
	
	
	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.ITerrain#isOut(int, int)
	 */
	public boolean isOut(int x, int y){
		return (this.plateau.length<x) || (this.plateau[0].length<y);
	}
	
	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.ITerrain#isLibre(IPosition)
	 */
	public boolean isLibre(IPosition position){
		return this.isLibre(position.getAbscisse(), position.getOrdonnee());
	}

	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.ITerrain#isOut(IPosition)
	 */
	public boolean isOut(IPosition position){
		return this.isOut(position.getAbscisse(), position.getOrdonnee());
	}
	
	/*
	 * (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.ITerrain#toString()
	 */
	public String toString(){
		StringBuilder chaine = new StringBuilder();
        for (int i = 0; i < this.plateau.length; i++) {
            for (int j = 0; j < this.plateau[0].length; j++)
                chaine.append(this.plateau[i][j]).append(" | ");
            chaine.append("\n");
        }
        return chaine.append(((IBase) base).listeRobots()).toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see fr.licence.agl.robot.inter.ITerrain#equals(ITerrain)
	 */
	public boolean equals(ITerrain terrain){
		if(terrain.getPlateau().length != this.getPlateau().length || terrain.getPlateau()[0].length != this.getPlateau()[0].length) return false;
        for(int i=0; i<this.getPlateau().length; i++)
            for(int j=0; j<this.getPlateau()[0].length; j++)
                if(!terrain.getPlateau()[i][j].equals(this.getPlateau()[i][j]))
                        return false;
        return true;
    }
	public INoeud getNoeud(int x, int y){
		return this.plateau[x][y];
	}

	public void setNoeud(int x, int y, INoeud noeud){
		this.plateau[x][y] = noeud;
	}
	
	public void setNoeud(int x, int y, float altitude, boolean obstacle){
		this.plateau[x][y] = new Noeud(altitude, obstacle);
	}

	public void setBase(int x, int y) throws BaseException {
        if ((x == 0 && y == 0) || (x == 0 && y == this.getOrdonnee() - 1)
                || (x == this.getAbscisse() - 1 && y == 0)
                || (x == this.getAbscisse() - 1 && y == this.getOrdonnee() - 1)) {
            this.plateau[x][y] = Base.getInstance();
            base = (IBase) this.getPlateau()[x][y];
            base.setAbscisse(x);
            base.setOrdonnee(y);
        } else
            throw new BaseException(
                    "ERREUR : La base ne se situe pas a une des quatre extremites du Terrain.");
    }
	
	/**
	 * Permet d'obtenir la chaine CSV avec toutes les informations du terrain
	 * @return la chaine CSV representant le terrain
	 */
	public String toCsvString ()
	{
//		 Initialisation des variables
        StringBuilder infoTerrain = new StringBuilder(); 
        infoTerrain.append("t;").append(getAbscisse() - 1).append(";").append(getOrdonnee() - 1).append("\n");
        StringBuilder infoBase = new StringBuilder(); 
        infoBase.append(getBase().toCsvString()).append("\n");
        StringBuilder infoNoeuds = new StringBuilder(); 
        infoNoeuds.append("");
        //parcours du tableau
        for (int i = 0; i < getAbscisse(); i++) {
            for (int j = 0; j < getOrdonnee(); j++) {
                INoeud noeud = getNoeud(i, j);
                if (noeud.getAltitude() > 0)
                    infoNoeuds.append("n;").append(i).append(";").append(j).append(";").append(noeud.toCsvString()).append("\n");
            }
        }
        return infoTerrain.append(infoBase).append(infoNoeuds).toString();
	}
	
	/**
	 * 
	 */
	public int getAbscisse(){
		return this.getPlateau().length;
	}

	/**
	 * 
	 */
	public int getOrdonnee(){
		return this.getPlateau()[0].length;
	}
	
	/**
	 * 
	 * @return
	 */
	public INoeud getBase() {
		return this.base;
	}
}