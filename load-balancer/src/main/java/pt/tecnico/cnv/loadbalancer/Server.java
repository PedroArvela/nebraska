package pt.tecnico.cnv.loadbalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private int port;
	ExecutorService threadPool;
	Scheduler scheduler;

	public Server(int port, Scheduler scheduler) {
		this.port = port;
		this.threadPool = Executors.newCachedThreadPool();
		this.scheduler = scheduler;
	}

	public void serve() throws IOException {
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Listening on port " + port + "...");

			while (true) {
				final Socket socket = server.accept();
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Proxy.process(socket, scheduler);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
		}
	}

}
