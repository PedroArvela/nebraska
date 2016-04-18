package pt.tecnico.cnv.loadbalancer;

public class NoMachineException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoMachineException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoMachineException(String message) {
		super(message);
	}

	public NoMachineException(Throwable cause) {
		super(cause);
	}

	public NoMachineException() {
		super();
	}

}
