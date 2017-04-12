package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
	
	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour les clés si vous préférez
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;

	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 * @param nbS attention ici il faut tous les etats (meme obstacles) car Q avec tableau ...
	 * @param nbA
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
		
		
	
	}


	
	
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)
	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		// retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)
		List<Action> returnactions = new ArrayList<Action>();
		if (this.getActionsLegales(e).size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
		}

		Double max = this.getValeur(e);
		for (Action a : this.getActionsLegales(e)) {
			if (this.getQValeur(e, a) == max) {
				returnactions.add(a);
			}
		}

		return returnactions;
	}
	
	@Override
	public double getValeur(Etat e) {

		Double val = 0.0;
		HashMap<Action,Double> actions = this.qvaleurs.get(e);
		if(actions != null){
			val = actions.entrySet()
					.stream()
					.max((entry1, entry2) -> getQValeur(e, entry1.getKey()) > getQValeur(e, entry2.getKey()) ? 1 : -1)
					.map(Map.Entry::getValue).orElse(0.0);
		}
		return val;
	}

	@Override
	public double getQValeur(Etat e, Action a) {
		double val = 0.0;
		if(qvaleurs.get(e) != null){
			if(qvaleurs.get(e).get(a) != null){
				val = qvaleurs.get(e).get(a);
			}
			else{
				//System.out.println("a introuvable");
				this.qvaleurs.get(e).put(a, 0.0);
			}
		}
		else{
			//System.out.println("e introuvable");
			this.qvaleurs.put(e, new HashMap<>());
			this.qvaleurs.get(e).put(a, 0.0);
		}
		return val;
	}
	
	
	
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//this.qvaleurs.get(e).put(a, d);

		// mise a jour vmax et vmin pour affichage du gradient de couleur:
				//vmax est la valeur de max pour tout s de V
				//vmin est la valeur de min pour tout s de V
				// ...
		HashMap<Action, Double> actions = this.qvaleurs.get(e);
		if(actions != null){
			actions.put(a, d);
		}
		else{
			HashMap<Action, Double> actionsToPut = new HashMap<>();
			actionsToPut.put(a, d);
			this.qvaleurs.put(e, actionsToPut);
		}

		vmax = Math.max(vmax, d);
		vmin = Math.min(vmin, d);

		this.notifyObs();
		
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);
		setQValeur(e, a, (1-alpha)*getQValeur(e, a) + alpha*(reward + gamma*getValeur(esuivant)));

	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		qvaleurs.clear();
		
		this.episodeNb =0;
		this.notifyObs();
	}









	


}
