package robot.inter;

public interface ISaver {

	/**
	 * Demande a l'utilisateur de definir le nom sous lequel sera enregistree la simulation
	 * Cette fonction gere le cas ou le fichier existerait.
	 */
	public void askNameFileText();

	/**
	 * Permet d'ecrire l'etat d'une simulation dans un fichier programme
	 * @return l'etat de l'enregistrement
	 */
	public boolean saveIntoFile();

}