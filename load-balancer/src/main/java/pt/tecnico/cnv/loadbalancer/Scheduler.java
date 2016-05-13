package pt.tecnico.cnv.loadbalancer;

import org.apache.commons.lang3.tuple.Pair;

public interface Scheduler {

	/**
	 * Gives the best web server for the task
	 * 
	 * @return pair URL, Port to use
	 */
	Pair<String, Integer> getInstance() throws NoMachineException;

	/**
	 * Informs the scheduler that the instance list needs to be updated
	 */
	void sendNotification();
}