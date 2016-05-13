package pt.tecnico.cnv.loadbalancer;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.ec2.model.Instance;

import pt.tecnico.cnv.metricstorage.NebraskaEC2Client;

public class LoadScheduler implements Scheduler {
	NebraskaEC2Client nec2;
	Set<Instance> insts;
	Set<LocalInstance> linsts;
	Iterator<LocalInstance> instanceIt;

	private TimerTask getTimerTask() {
		return new TimerTask() {
			@Override
			public void run() {
				sendNotification();
				Timer timer = new Timer();
				timer.schedule(getTimerTask(), 15000);
			}
		};
	}

	public LoadScheduler(NebraskaEC2Client nec2) {
		this.nec2 = nec2;
		this.insts = nec2.getInstances();

		gatherStatistics();

		NotificationListener notifications = new NotificationListener(8080, this);
		new Thread(notifications).start();
		new Thread(getTimerTask()).start();
	}

	private void gatherStatistics() {
		Set<LocalInstance> linsts = new HashSet<LocalInstance>();

		Dimension instanceDimension = new Dimension();
		instanceDimension.setName("InstanceId");

		long offsetInMilliseconds = 1000 * 60 * 2;

		for (Instance inst : insts) {
			String name = inst.getInstanceId();
			instanceDimension.setValue(name);

			GetMetricStatisticsRequest request = new GetMetricStatisticsRequest()
					.withStartTime(new Date(new Date().getTime() - offsetInMilliseconds)).withNamespace("AWS/EC2")
					.withPeriod(60).withMetricName("CPUUtilization").withStatistics("Average")
					.withDimensions(instanceDimension).withEndTime(new Date());

			GetMetricStatisticsResult getMetricStatisticsResult = nec2.cloudWatch.getMetricStatistics(request);

			List<Datapoint> datapoints = getMetricStatisticsResult.getDatapoints();

			float average = 0;
			for (Datapoint dp : datapoints) {
				average += dp.getAverage();
			}
			average = average / datapoints.size();

			LocalInstance linst = new LocalInstance(inst.getPublicDnsName(), 8000);
			linst.averageCPU = average;

			linsts.add(linst);
			System.out.println("Found instance " + linst.dns + " with load " + linst.averageCPU);
		}

		this.linsts = linsts;
	}

	@Override
	public Pair<String, Integer> getInstance() throws NoMachineException {
		if (linsts.isEmpty()) {
			throw new NoMachineException();
		}

		if (instanceIt == null || !instanceIt.hasNext()) {
			instanceIt = linsts.iterator();
		}

		LocalInstance in = instanceIt.next();
		System.out.println("Instance: " + in.dns + ":" + 8000);
		return new ImmutablePair<String, Integer>(in.dns, 8000);
	}

	@Override
	public void sendNotification() {
		System.out.println("Fetching server info remotelly");
		this.insts = nec2.getInstances();
		gatherStatistics();
	}
}
