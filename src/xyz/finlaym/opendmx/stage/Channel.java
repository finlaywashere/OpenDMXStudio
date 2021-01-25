package xyz.finlaym.opendmx.stage;

public class Channel {
	private int universe;
	private int channel;
	private ChannelType type;
	private int currVal = 0;
	
	public Channel(int universe, int channel, ChannelType type) {
		this.channel = channel;
		this.type = type;
		this.universe = universe;
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
	public int getCurrVal() {
		return currVal;
	}
	public void setCurrVal(int currVal) {
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
		return universe+","+channel+","+type.toString();
	}
	public static Channel fromString(String s) {
		String[] split = s.split(",",3);
		int universe = Integer.valueOf(split[0]);
		int channel = Integer.valueOf(split[1]);
		ChannelType type = ChannelType.valueOf(split[2]);
		return new Channel(universe,channel,type);
	}
}
