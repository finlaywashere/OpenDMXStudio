package xyz.finlaym.opendmx;

import java.io.File;

public class Utils {

	public static void deleteDirectory(File dir) {
		if(!dir.isDirectory()) {
			dir.delete();
			return;
		}
		for(File f : dir.listFiles()) {
			deleteDirectory(f);
		}
		dir.delete();
	}

}
