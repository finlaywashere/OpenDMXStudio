package xyz.finlaym.opendmx.stage;

import xyz.finlaym.opendmx.OpenDMXStudio;

public class Channel {
	private int universe;
	private int channel;
	private ChannelType type;
	private int currVal = 0;
	private int id;
	
	public Channel(int universe, int channel, ChannelType type, int id) {
		this.channel = channel;
		this.type = type;
		this.universe = universe;
		this.id = id;
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
		return universe+","+channel+","+type.toString()+","+id;
	}
	public static Channel fromString(String s) {
		String[] split = s.split(",",4);
		int universe = Integer.valueOf(split[0]);
		int channel = Integer.valueOf(split[1]);
		ChannelType type = ChannelType.valueOf(split[2]);
		int id = Integer.valueOf(split[3]);
		return new Channel(universe,channel,type,id);
	}
}
