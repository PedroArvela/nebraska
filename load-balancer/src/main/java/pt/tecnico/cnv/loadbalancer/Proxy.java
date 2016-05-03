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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

public class Proxy {
	public static void process(Socket s, Scheduler scheduler) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));

		try {
			// Parse client request
			String request = in.readLine();
			String[] requestParams = request.split(" ");
			String path = requestParams[1];

			Pair<String, Integer> target;
			target = scheduler.getInstance();

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
		} catch (NoMachineException e) {
			out.print("HTTP/1.1 500 \r\n");
			out.print("Content-Type: text/html\r\n");
			out.print("Connection: close\r\n");
			out.print("\r\n");
			out.print("<h1>Internal Server Error</h1>\r\n");
			out.print("<p>No servers are available to fullfill your request.</p>\r\n");
		} finally {
			out.close();
			in.close();
			s.close();
		}
	}
}
