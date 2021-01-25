package xyz.finlaym.opendmx.cue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.finlaym.opendmx.command.SendCommand;
import xyz.finlaym.opendmx.driver.HardwareInterface;
import xyz.finlaym.opendmx.stage.Channel;


public class CueContainer {
	private String name;
	private List<CueEntry> entries;

	public CueContainer(String name) {
		this.entries = new ArrayList<CueEntry>();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public List<CueEntry> getEntries() {
		return entries;
	}
	
	public void execute(HardwareInterface hw) throws IOException {
		double maxTime = 0;
		for(CueEntry e : entries) {
			if(e.getTransitionTime() > maxTime)
				maxTime = e.getTransitionTime();
		}
		double timeElapsed = 0;
		long lastTime = System.currentTimeMillis();
		
		while(timeElapsed < maxTime) {
			timeElapsed += ((double)(System.currentTimeMillis()-lastTime)) / 1000; // Get time elapsed in seconds
			for(CueEntry e : entries) {
				if(e.getTransitionType() == CueTransitionType.CROSSFADE) {
					if(e.getTransitionTime() > timeElapsed) {
						int diff = e.getNewValue().getCurrVal() - e.getOldValue().getCurrVal();
						double dps = diff/e.getTransitionTime();
						int newValue = e.getOldValue().getCurrVal() + (int) (timeElapsed * dps);
						Channel c = e.getNewValue();
						SendCommand cmd = new SendCommand(c.getUniverse(),c.getChannel(),newValue);
						hw.sendCommand(cmd);
					}else {
						Channel c = e.getNewValue();
						SendCommand cmd = new SendCommand(c);
						hw.sendCommand(cmd);
					}
				}
			}
			lastTime = System.currentTimeMillis();
			try {
				Thread.sleep(250); // Sleep for a quarter second
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	@Override
	public String toString() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
