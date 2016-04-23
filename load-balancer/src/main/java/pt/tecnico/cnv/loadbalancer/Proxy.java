package pt.tecnico.cnv.loadbalancer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Proxy {

	public static void process(Socket s) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));

		String request = in.readLine();
		String[] requestParams = request.split(" ");
		String path = requestParams[1];

		out.print("HTTP/1.1 200 \r\n");
		out.print("Content-Type: text/plain\r\n");
		out.print("Connection: close\r\n");
		out.print("\r\n");

		out.println(path);

		out.close();
		in.close();
		s.close();
	}

	public static List<String> processPath(String pathString) {
		List<String> components = new ArrayList<String>();

		if (pathString == null) {
			pathString = "";
		}

		for (String component : pathString.split("/")) {
			if (!component.isEmpty()) {
				components.add(component);
			}
		}

		return components;
	}

	public static Map<String, String> processQueries(String queryString) {
		Map<String, String> queries = new HashMap<String, String>();

		if (queryString == null) {
			queryString = "";
		}

		for (String query : queryString.split("&")) {
			if (!query.isEmpty()) {
				String[] queryArgs = query.split("=", 2);
				if (queryArgs.length == 2) {
					queries.put(queryArgs[0], queryArgs[1]);
				} else {
					queries.put(queryArgs[0], "true");
				}
			}
		}

		return queries;
	}

}
