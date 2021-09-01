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

package xyz.finlaym.opendmx.stage;

import xyz.finlaym.opendmx.OpenDMXStudio;

public class Channel {
	public static final int MODE_DEFAULT = 0;
	
	private int universe;
	private int channel;
	private ChannelType type;
	private int currVal = 0;
	private int id;
	private int mode;
	
	public Channel(int universe, int channel, ChannelType type, int id, int mode) {
		this.channel = channel;
		this.type = type;
		this.universe = universe;
		this.id = id;
		this.mode = mode;
	}
	public int getMode() {
		return mode;
	}

	public int getId() {
		return id;
	}

	public int getChannel() {
		return channel;
	}
	public ChannelType getType() {
		return type;
	}
	public int getUniverse() {
		return universe;
	}
	public int getCurrValRaw() {
		return Math.min(currVal, 255);
	}
	public int getCurrVal(OpenDMXStudio studio) {
		return Math.min(studio.getCRegistry().getChannel(this),255);
	}
	public void setCurrVal(int currVal, OpenDMXStudio studio) {
		this.currVal = currVal;
		studio.getCRegistry().setChannel(this);
	}
	public void setCurrValRaw(int currVal){
		this.currVal = currVal;
	}
	public void setUniverse(int universe) {
		this.universe = universe;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public void setType(ChannelType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Universe: "+universe+" Channel: "+channel;
	}
	public String encode() {
		return universe+","+channel+","+type.toString()+","+id;
	}
	public static Channel fromString(String s) {
		String[] split = s.split(",",5);
		int universe = Integer.valueOf(split[0]);
		int channel = Integer.valueOf(split[1]);
		ChannelType type = ChannelType.valueOf(split[2]);
		int id = Integer.valueOf(split[3]);
		int mode = Integer.valueOf(split[4]);
		return new Channel(universe,channel,type,id,mode);
	}
}
