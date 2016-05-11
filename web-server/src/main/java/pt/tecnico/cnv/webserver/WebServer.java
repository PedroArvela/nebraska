package pt.tecnico.cnv.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import pt.tecnico.cnv.instrumentation.InstrumentationData;
@SuppressWarnings("restriction")
public class WebServer {
	public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/f.html", new MyHandler());
        server.setExecutor(Executors.newCachedThreadPool()); // creates a default executor
        server.start();
    }
    
    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            IntFactorization i = new IntFactorization();
            String query = t.getRequestURI().getQuery();
            String[] parts = query.split("=");
            String response = "Response: " +i.primeFactors(new BigInteger(parts[1]));
            InstrumentationData data = InstrumentationData.getInstance(Thread.currentThread().getId());
            System.err.println(data.i_count + " instructions in " + data.b_count + " basic blocks were executed.");

            //TODO send data to MetricStorage
            
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
