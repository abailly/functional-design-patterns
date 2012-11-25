package robot.inter;
public interface IRobot {

	/**
	 * 
	 * @return
	 */
	public abstract IBatterie getBatterie();

	/**
	 * 
	 * @param batterie
	 */
	public abstract void setBatterie(IBatterie batterie);

	/**
	 * 
	 * @return
	 */
	public abstract int getIdentifiant();

	/**
	 * 
	 * @param identifiant
	 */
	public abstract void setIdentifiant(int identifiant);

	/**
	 * 
	 * @return
	 */
	public abstract IPosition getPosition();

	/**
	 * 
	 * @param position
	 */
	public abstract void setPosition(IPosition position);

	/**
	 * Permet d'effectuer un deplacement 
	 * @param c le deplacement a effectuer
	 * @return la nouvelle position du robot
	 */
	public abstract void ordre(char c);

	/**
	 * 
	 */
	public abstract String toString();
	
	/**
	 * 
	 */
	public abstract String toCsvString();
	
	public abstract boolean equals(IRobot ir);

}
