package pt.tecnico.cnv.loadbalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private int port;
	ExecutorService threadPool;

	public Server(int port) {
		this.port = port;
		threadPool = Executors.newCachedThreadPool();
	}

	public void serve() throws IOException {
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Listening on port " + port + "...");

			while (true) {
				try (Socket socket = server.accept()) {
					threadPool.execute(() -> Proxy.process(socket));
				}
			}
		}
	}

}
