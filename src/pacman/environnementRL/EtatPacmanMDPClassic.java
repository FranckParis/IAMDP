package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.sun.xml.internal.fastinfoset.tools.FI_StAX_SAX_Or_XML_SAX_SAXEvent;
import pacman.elements.Position2D;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	private ArrayList<Position2D> ghosts;
	private ArrayList<Position2D> dots;
	private ArrayList<Position2D> pacmans;

	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){

		//Pacman
		this.pacmans = new ArrayList<>();
		for (int i=0; i<_stategamepacman.getNumberOfPacmans();i++) {
			if(!_stategamepacman.getPacmanState(i).isDead()){
				this.pacmans.add(new Position2D(_stategamepacman.getPacmanState(i).getX(),
						_stategamepacman.getPacmanState(i).getY()));
			}
		}

		//Ghosts
		this.ghosts = new ArrayList<>();
		for (int i=0; i<_stategamepacman.getNumberOfGhosts();i++) {
			if(!_stategamepacman.getGhostState(i).isDead()){
				this.ghosts.add(new Position2D(_stategamepacman.getGhostState(i).getX(),
						_stategamepacman.getGhostState(i).getY()));
			}
		}

		//Dots
		this.dots = new ArrayList<>();
		for(int i =0; i<_stategamepacman.getMaze().getSizeX(); i++){
			for(int j =0; j<_stategamepacman.getMaze().getSizeY(); j++){
				if(_stategamepacman.getMaze().isFood(i, j)){
					this.dots.add(new Position2D(i, j));
				}
			}
		}
	}
	
	@Override
	public String toString() {
		
		return "";
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}


		// on renvoie le clone
		return clone;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

		if (ghosts != null ? !ghosts.equals(that.ghosts) : that.ghosts != null) return false;
		if (dots != null ? !dots.equals(that.dots) : that.dots != null) return false;
		return pacmans != null ? pacmans.equals(that.pacmans) : that.pacmans == null;
	}

	@Override
	public int hashCode() {
		int result = ghosts != null ? ghosts.hashCode() : 0;
		result = 31 * result + (dots != null ? dots.hashCode() : 0);
		result = 31 * result + (pacmans != null ? pacmans.hashCode() : 0);
		return result;
	}
}
