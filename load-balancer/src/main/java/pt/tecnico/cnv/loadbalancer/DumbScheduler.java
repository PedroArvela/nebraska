package pt.tecnico.cnv.loadbalancer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DumbScheduler implements Scheduler {

	public Pair<String, Integer> getInstance() throws NoMachineException {
		return new ImmutablePair<String, Integer>("localhost", 8000);
	}

	@Override
	public void sendNotification() {
	}
}
