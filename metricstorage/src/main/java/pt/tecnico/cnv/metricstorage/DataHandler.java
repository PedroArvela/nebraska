package pt.tecnico.cnv.metricstorage;

import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class DataHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
    	
        String query = t.getRequestURI().getQuery();
        String[] parts = query.split("=");
        Data d = new Data();
        for(int i = 0; i < 2; i += 2){
        	if(parts[i] == "iCount"){
        		d.iCount(Integer.parseInt(parts[i+1]));
        	}else if(parts[i] == "bCount"){
        		d.bCount(Integer.parseInt(parts[i+1]));
        	}
        }

        MetricStorageApp.getInstance().data(d);
        
        String response = new String();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}