package pt.tecnico.cnv.loadbalancer;

import org.apache.commons.lang3.tuple.Pair;

public interface Decider {

	/**
	 * Gives the best web server for the task
	 * 
	 * @return pair URL, Port to use
	 */
	Pair<String, Integer> decide() throws NoMachineException;

}