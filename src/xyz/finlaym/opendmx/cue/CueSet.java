package xyz.finlaym.opendmx.cue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.driver.HardwareInterface;

public class CueSet {
	private List<CueContainer> cues;
	private int curr = 0;
	
	public CueSet() {
		this.cues = new ArrayList<CueContainer>();
	}
	public List<CueContainer> getCues() {
		return cues;
	}
	public boolean execute(HardwareInterface hw, OpenDMXStudio studio) throws IOException{
		if(curr >= cues.size())
			return false;
		cues.get(curr).execute(hw,studio);
		curr++;
		return true;
	}
}
