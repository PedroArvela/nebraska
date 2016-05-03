package pt.tecnico.cnv.loadbalancer;

import java.util.Iterator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.amazonaws.services.ec2.model.Instance;

import pt.tecnico.cnv.metricstorage.NebraskaEC2Client;

public class RoundRobinScheduler implements Scheduler {
	NebraskaEC2Client nec2;
	Iterator<Instance> instanceIt;

	public RoundRobinScheduler(NebraskaEC2Client nec2) {
		this.nec2 = nec2;
	}

	@Override
	public Pair<String, Integer> getInstance() throws NoMachineException {
		if (nec2.getInstances().isEmpty()) {
			throw new NoMachineException();
		}

		if (instanceIt == null || !instanceIt.hasNext()) {
			instanceIt = nec2.getInstances().iterator();
		}

		Instance in = instanceIt.next();
		return new ImmutablePair<String, Integer>(in.getPublicDnsName(), 8000);
	}

}
