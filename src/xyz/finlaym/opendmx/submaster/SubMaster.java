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

package xyz.finlaym.opendmx.submaster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.command.SendCommand;
import xyz.finlaym.opendmx.stage.Channel;
import xyz.finlaym.opendmx.stage.StageElement;

public class SubMaster {
	private List<SubMasterEntry> channels;
	private int id;
	private String name;
	private SubMasterType type;
	private int value = 0;
	public SubMaster(int id, String name, SubMasterType type) {
		this.channels = new ArrayList<SubMasterEntry>();
		this.id = id;
		this.name = name;
		this.type = type;
	}
	public List<SubMasterEntry> getChannels() {
		return channels;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public SubMasterType getType() {
		return type;
	}
	public int getValue() {
		return value;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Sets the value of an entire sub master, searches for the channels/devices it wants so that they can be linked and change automatically
	 * @throws IOException 
	*/
	public void setValue(int value, OpenDMXStudio studio) throws IOException {
		this.value = value;
		List<Channel> triggers = new ArrayList<Channel>();
		if(type == SubMasterType.MASTER) {
			for(StageElement e : studio.getCurrentStage().getElements()) {
				for(Channel c : e.getChannels()) {
					triggers.add(c);
				}
			}
		}else {
			for(SubMasterEntry channel : channels) {
				for(StageElement e : studio.getCurrentStage().getElements()) {
					if(channel.getType() == SubMasterEntryType.DEVICE && e.getId() != channel.getId())
						continue;
					for(Channel c : e.getChannels()) {
						if(channel.getType() == SubMasterEntryType.DEVICE) {
							triggers.add(c);
						}else if(channel.getType() == SubMasterEntryType.CHANNEL) {
							if(c.getId() == channel.getId())
								triggers.add(c);
						}
					}
				}
			}
		}
		for(Channel c : triggers) {
			c.setCurrVal(value, studio);
			SendCommand cmd = new SendCommand(c.getUniverse(),c.getChannel(),c.getCurrVal(studio), c.getMode());
			studio.getHardwareManager().sendCommand(cmd);
		}
	}
	@Override
	public String toString() {
		return name;
	}
}
