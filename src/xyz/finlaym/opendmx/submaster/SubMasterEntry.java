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

import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.Utils;
import xyz.finlaym.opendmx.stage.Channel;
import xyz.finlaym.opendmx.stage.StageElement;

public class SubMasterEntry {
	private SubMasterEntryType type;
	private int id;
	private OpenDMXStudio dmx;
	public SubMasterEntry(SubMasterEntryType type, int id, OpenDMXStudio dmx) {
		this.type = type;
		this.id = id;
		this.dmx = dmx;
	}
	public SubMasterEntryType getType() {
		return type;
	}
	public int getId() {
		return id;
	}
	public String toString() {
		if(type == SubMasterEntryType.DEVICE) {
			StageElement elem = Utils.locateElement(id, dmx);
			return "Name: "+elem.getName();
		}else {
			Channel c = Utils.locateChannel(id, dmx);
			return "Universe: "+c.getUniverse()+" Channel: "+c.getChannel();
		}
	}
	
	public String encode() {
		return id+","+type;
	}
	public static SubMasterEntry fromString(String s, OpenDMXStudio dmx) {
		String[] split = s.split(",",2);
		int id = Integer.valueOf(split[0]);
		SubMasterEntryType type = SubMasterEntryType.valueOf(split[1]);
		return new SubMasterEntry(type, id, dmx);
	}
}
