package xyz.finlaym.opendmx;

import java.io.File;

import xyz.finlaym.opendmx.stage.Channel;
import xyz.finlaym.opendmx.stage.StageElement;

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
	@SuppressWarnings("unused")
	public static boolean isInt(String s) {
		try {
			int i = Integer.valueOf(s);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	public static Channel locateChannel(int id, OpenDMXStudio dmx) {
		for(StageElement e : dmx.getCurrentStage().getElements()) {
			for(Channel c : e.getChannels()) {
				if(c.getId() == id)
					return c;
			}
		}
		return null;
	}
	public static StageElement locateElement(int id, OpenDMXStudio dmx) {
		for(StageElement e : dmx.getCurrentStage().getElements()) {
			if(e.getId() == id)
				return e;
		}
		return null;
	}
}
