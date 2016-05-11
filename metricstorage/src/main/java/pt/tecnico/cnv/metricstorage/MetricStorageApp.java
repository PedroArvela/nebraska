package pt.tecnico.cnv.metricstorage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;


@SuppressWarnings("restriction")
public class MetricStorageApp extends Thread{
	private static MetricStorageApp _instance;
	private List<Data> _data;
	
	private MetricStorageApp(){
		_data = new ArrayList<Data>();
	}
	public void run(){
		try {
			HttpServer server;
			server = HttpServer.create(new InetSocketAddress(8000), 0);
	        server.createContext("/sendData.html", new DataHandler());
	        server.setExecutor(Executors.newCachedThreadPool()); // creates a default executor
	        server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static MetricStorageApp getInstance(){
		if(_instance == null) _instance = new MetricStorageApp();
		return _instance;
	}
	
	public List<Data> data(){ return _data; }
	
	public void data(Data data){ _data.add(data); }
}
