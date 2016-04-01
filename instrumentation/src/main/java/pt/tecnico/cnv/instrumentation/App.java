package pt.tecnico.cnv.instrumentation;

import java.io.File;
import java.io.IOException;

import BIT.highBIT.BasicBlock;
import BIT.highBIT.ClassInfo;
import BIT.highBIT.Routine;

public class App {

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

				if (rout.getMethodName().equals("primeFactors")) {
					rout.addBefore("pt/tecnico/cnv/instrumentation/App", "initData", ci.getClassName());
				}

				// Loop through all the basic blocks in the class
				for (Object j : rout.getBasicBlocks().getBasicBlocks()) {
					BasicBlock bb = (BasicBlock) j;
					bb.addBefore("pt/tecnico/cnv/instrumentation/App", "count", new Integer(bb.size()));
				}
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
	public static synchronized void initData(String foo) {
		InstrumentationData.clearInstance(Thread.currentThread().getId());
	}

	public static synchronized void count(int incr) {
		InstrumentationData data = InstrumentationData.getInstance(Thread.currentThread().getId());
		data.i_count += incr;
		data.b_count++;
	}
}
