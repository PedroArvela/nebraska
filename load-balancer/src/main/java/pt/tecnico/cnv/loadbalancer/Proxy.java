package pt.tecnico.cnv.loadbalancer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.amazonaws.transform.MapEntry;

public class Proxy {

	public static void process(Socket s) throws IOException, NoMachineException {
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));

		// Parse client request
		String request = in.readLine();
		String[] requestParams = request.split(" ");
		String path = requestParams[1];

		Pair<String, Integer> target = new DumbDecider().decide();

		// Connect to the proxy target
		URL url = new URL("http", target.getLeft(), target.getRight(), path);
		URLConnection conn = url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		HttpURLConnection huc = (HttpURLConnection) conn;

		if (conn.getContentLengthLong() < 0) {
			throw new IOException();
		}

		int response = huc.getResponseCode();
		BufferedReader rd;

		if (response == 200) {
			rd = new BufferedReader(new InputStreamReader(huc.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(huc.getErrorStream()));
		}

		Map<String, List<String>> responseHeaders = huc.getHeaderFields();
		for (Map.Entry<String, List<String>> headerField : responseHeaders.entrySet()) {
			String outputLine = "";

			if (headerField.getKey() != null) {
				outputLine = headerField.getKey() + ":";
			}

			for (String fieldValue : headerField.getValue()) {
				outputLine += " " + fieldValue;
			}
			outputLine += "\r\n";
			out.write(outputLine);
		}
		out.write("\r\n");

		// Read the rest of the client input and send it to the target
		String outputLine = rd.readLine();
		while (outputLine != null) {
			out.write(outputLine + "\r\n");
			outputLine = rd.readLine();
		}

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
