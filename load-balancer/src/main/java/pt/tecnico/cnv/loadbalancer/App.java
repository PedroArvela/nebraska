package pt.tecnico.cnv.loadbalancer;

public class App {
	public static void main(String[] args) throws Exception {
		int port = 8888;

		if (args.length >= 1) {
			port = Integer.parseInt(args[1]);
		}

		Server s = new Server(port);
		s.serve();
	}
}
