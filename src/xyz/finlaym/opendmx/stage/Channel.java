package xyz.finlaym.opendmx.stage;

public class Channel {
	private int universe;
	private int channel;
	private ChannelType type;
	private int currVal = 0;
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
	public Channel(int universe, int channel, ChannelType type) {
		this.channel = channel;
		this.type = type;
		this.universe = universe;
	}
	@Override
	public String toString() {
		return universe+","+channel+","+type.toString();
	}
}
