package pt.tecnico.cnv.instrumentation;

import java.io.File;
import java.io.IOException;

import BIT.highBIT.BasicBlock;
import BIT.highBIT.ClassInfo;
import BIT.highBIT.Routine;

public class App {
	private static int i_count = 0, b_count = 0;

	public static void dummy() {
		return;
	}

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

			// Loop through all the routines in the class
			for (Object i : ci.getRoutines()) {
				Routine rout = (Routine) i;

				// Loop through all the basic blocks in the class
				for (Object j : rout.getBasicBlocks().getBasicBlocks()) {
					BasicBlock bb = (BasicBlock) j;
					bb.addBefore("pt/tecnico/cnv/instrumentation/App", "count", new Integer(bb.size()));
				}

				rout.addAfter("pt/tecnico/cnv/instrumentation/App", "printICount", ci.getClassName());
			}

			// Write the new object back into the same file
			ci.write(filename);
		} else {
			System.err.println("File must be a .class and exist");
			try {
				System.err.println("File passed was " + f.getCanonicalPath());
			} catch (IOException e) {
				// Ignore
			}
		}
	}

	// Actual instrumentation data
	public static synchronized void printICount(String foo) {
		System.out.println(i_count + " instructions in " + b_count + " basic blocks were executed.");
	}

	public static synchronized void count(int incr) {
		i_count += incr;
		b_count++;
	}
}
