package pt.tecnico.cnv.instrumentation;

import java.io.File;
import java.io.IOException;

import BIT.highBIT.ClassInfo;

public class App {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Path to .class file required");
			return;
		}
		String filename = args[0];
		File f = new File(filename);

		if (filename.endsWith(".class") && f.isFile()) {
			// Read the class data into an object
			ClassInfo ci = new ClassInfo(filename);

			// Write the new object back into the same file
			ci.write(filename);
		} else {
			System.err.println("File must be a .class and exist");
			try {
				System.err.println("File passed was" + f.getCanonicalPath());
			} catch (IOException e) {
				// Ignore
			}
		}
	}
}
