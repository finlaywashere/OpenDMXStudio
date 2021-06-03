/*
    Copyright (C) 2021 Finlay Maroney
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package xyz.finlaym.opendmx.cue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.finlaym.opendmx.command.SendCommand;
import xyz.finlaym.opendmx.driver.ControllerHardware;
import xyz.finlaym.opendmx.stage.Channel;
import xyz.finlaym.opendmx.stage.ChannelType;


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
	
	public void execute(ControllerHardware hw) throws IOException {
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
				if(e.getTransitionType() == CueTransitionType.CROSSFADE && e.getTransitionTime() != 0 && e.getNewValue().getType() != ChannelType.LIGHT_MASTER) {
					if(e.getTransitionTime() > timeElapsed) {
						int diff = e.getNewValue().getCurrValRaw() - e.getOldValue().getCurrValRaw();
						double dps = diff/e.getTransitionTime();
						int newValue = e.getOldValue().getCurrValRaw() + (int) (timeElapsed * dps);
						Channel c = e.getNewValue();
						SendCommand cmd = new SendCommand(c.getUniverse(),c.getChannel(),newValue);
						hw.sendCommand(cmd);
					}else {
						Channel c = e.getNewValue();
						SendCommand cmd = new SendCommand(c);
						hw.sendCommand(cmd);
					}
				}else {
					Channel c = e.getNewValue();
					SendCommand cmd = new SendCommand(c);
					hw.sendCommand(cmd);
				}
			}
			lastTime = System.currentTimeMillis();
			try {
				Thread.sleep(200); // Sleep for a fifth of a second
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		for(CueEntry e : entries) {
			Channel c = e.getNewValue();
			SendCommand cmd = new SendCommand(c);
			hw.sendCommand(cmd);
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
