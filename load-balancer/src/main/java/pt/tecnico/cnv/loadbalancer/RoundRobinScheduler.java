package pt.tecnico.cnv.loadbalancer;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.amazonaws.services.ec2.model.Instance;

import pt.tecnico.cnv.metricstorage.NebraskaEC2Client;

public class RoundRobinScheduler implements Scheduler {
	NebraskaEC2Client nec2;
	Set<Instance> insts;
	Iterator<Instance> instanceIt;

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			sendNotification();
			Timer timer = new Timer();
			timer.schedule(this, 15000);
		}
	};

	public RoundRobinScheduler(NebraskaEC2Client nec2) {
		this.nec2 = nec2;
		this.insts = nec2.getInstances();

		NotificationListener notifications = new NotificationListener(8080, this);
		new Thread(notifications).start();
		new Thread(task).start();
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
		System.out.println("Fetching server info remotelly");
		this.insts = nec2.getInstances();
	}
}
