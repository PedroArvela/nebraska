package pt.tecnico.cnv.loadbalancer;

import pt.tecnico.cnv.metricstorage.NebraskaEC2Client;

public class App {
	public static void main(String[] args) throws Exception {
		Scheduler scheduler;
		int port = 8888;

		if (args.length < 1) {
			System.err.println("Wrong number of arguments");
			System.err.println("Usage:\tApp <decider> [ port ]");
		}

		if (args[0].equals("RoundRobin")) {
			NebraskaEC2Client nec2 = NebraskaEC2Client.init("us-west-2.amazonaws.com");
			scheduler = new RoundRobinScheduler(nec2);
		} else {
			scheduler = new DumbScheduler();
		}

		if (args.length > 1) {
			port = Integer.parseInt(args[1]);
		}

		Server s = new Server(port, scheduler);
		s.serve();
	}
}
