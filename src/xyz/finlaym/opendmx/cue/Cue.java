package xyz.finlaym.opendmx.cue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.driver.HardwareInterface;

public class Cue {
	private List<CueContainer> entries;
	private int curr = 0;
	
	public Cue() {
		this.entries = new ArrayList<CueContainer>();
	}
	
	public boolean executeNext(HardwareInterface hw, OpenDMXStudio studio) throws IOException {
		if(curr >= entries.size())
			return false;
		entries.get(curr).execute(hw, studio);
		curr++;
		return true;
	}
}
