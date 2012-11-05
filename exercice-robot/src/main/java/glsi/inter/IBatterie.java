package glsi.inter;

public interface IBatterie {

	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.IBatterie#getAutonomie()
	 */
	public abstract int getAutonomie();

	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.IBatterie#setAutonomie(int)
	 */
	public abstract void setAutonomie(int autonomie);

	/* (non-Javadoc)
	 * @see fr.licence.agl.robot.impl.IBatterie#toString()
	 */
	public abstract String toString();
	
	public abstract boolean equals(IBatterie ib);

    public abstract int energieConsommee(int hauteurA,int hauteurB);

}
