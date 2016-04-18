package pt.tecnico.cnv.loadbalancer;

import org.apache.commons.lang3.tuple.Pair;

public class DumbDecider implements Decider {

	public Pair<String, Integer> decide() throws NoMachineException {
		throw new NoMachineException();
	}
}
