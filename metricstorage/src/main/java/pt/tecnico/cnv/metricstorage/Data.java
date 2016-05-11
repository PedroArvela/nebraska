package pt.tecnico.cnv.metricstorage;

public class Data {
	private int _iCount;
	private int _bCount;
	
	protected int iCount(){ return _iCount; }
	protected int bCount(){ return _bCount; }
	protected void iCount(int count){ _iCount = count; }
	protected void bCount(int count){ _bCount = count; }
}