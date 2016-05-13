package pt.tecnico.cnv.loadbalancer;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.amazonaws.services.ec2.model.Instance;

import pt.tecnico.cnv.metricstorage.NebraskaEC2Client;

public class RoundRobinScheduler implements Scheduler {
	NebraskaEC2Client nec2;
	Set<Instance> insts;
	Iterator<Instance> instanceIt;

	NotificationListener notifications;

	public RoundRobinScheduler(NebraskaEC2Client nec2) {
		this.nec2 = nec2;
		this.insts = nec2.getInstances();

		notifications = new NotificationListener(8080, this);
	}

	@Override
	public Pair<String, Integer> getInstance() throws NoMachineException {
		if (insts.isEmpty()) {
			throw new NoMachineException();
		}

		if (instanceIt == null || !instanceIt.hasNext()) {
			instanceIt = insts.iterator();
		}

		Instance in = instanceIt.next();
		System.out.println("Instance: " + in.getPublicDnsName() + ":" + 8000);
		return new ImmutablePair<String, Integer>(in.getPublicDnsName(), 8000);
	}

	@Override
	public void sendNotification() {
		this.insts = nec2.getInstances();
	}
}
