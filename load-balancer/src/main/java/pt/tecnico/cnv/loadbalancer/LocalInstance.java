package pt.tecnico.cnv.loadbalancer;

public class LocalInstance {
	public String dns;
	public int port;
	public double averageCPU;
	
	public LocalInstance(String dns, int port) {
		this.dns = dns;
		this.port = port;
	}
}
