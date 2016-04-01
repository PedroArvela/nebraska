package pt.tecnico.cnv.instrumentation;

import java.util.HashMap;
import java.util.Map;

public class InstrumentationData {
	private static Map<Long, InstrumentationData> data = new HashMap<Long, InstrumentationData>();
	
	public int i_count = 0, b_count = 0;

	private InstrumentationData() {

	}

	public static InstrumentationData getInstance(long threadID) {
		if(!data.containsKey(threadID)) {
			data.put(threadID, new InstrumentationData());
		}
		return data.get(threadID);
	}
	
	public static void clearInstance(long threadID) {
		if(data.containsKey(threadID)) {
			data.remove(threadID);
		}
	}
}
