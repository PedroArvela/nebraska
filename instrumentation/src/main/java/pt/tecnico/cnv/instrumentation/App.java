package pt.tecnico.cnv.instrumentation;

import BIT.highBIT.ClassInfo;

public class App {
	public static void main(String[] args) {
		String filename = args[0];

		if (filename.endsWith(".class")) {
			// Read the class data into an object
			ClassInfo ci = new ClassInfo(filename);

			// Write the new object back into the same file
			ci.write(filename);
		}
	}
}
