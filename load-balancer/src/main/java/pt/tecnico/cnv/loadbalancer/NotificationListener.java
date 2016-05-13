package pt.tecnico.cnv.loadbalancer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationListener implements Runnable {
	private int port;
	Scheduler sched;
	ExecutorService threadPool;

	public NotificationListener(int port, Scheduler sched) {
		this.port = port;
		this.sched = sched;
		this.threadPool = Executors.newCachedThreadPool();
	}

	public void process(Socket s) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));

		// Parse client request
		String request = in.readLine();
		String headerLine = in.readLine();
		Map<String, String> headers = new TreeMap<String, String>();
		
		while(headerLine != null && !headerLine.equals("\r\n")) {
			String[] headerPair = headerLine.split("\\s*:\\s*", 2);
			headers.put(headerPair[0], headerPair[1]);			
			
			headerLine = in.readLine();
		}
		
		if(headers.containsKey("x-amz-sns-message-type") && headers.get("x-amz-sns-message-type").equals("Notification")) {
			sched.sendNotification();
		}

		out.print("HTTP/1.1 200 \r\n");
		out.print("Content-Type: text/plain; charset=utf-8\r\n");
		out.print("Connection: close\r\n");
		out.print("\r\n");

		out.close();
		in.close();
		s.close();
	}

	@Override
	public void run() {
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Listening on port " + port + "...");

			while (true) {
				final Socket socket = server.accept();
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							process(socket);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}