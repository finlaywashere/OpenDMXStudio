package xyz.finlaym.opendmx.ui;

import xyz.finlaym.opendmx.stage.Channel;

public class ChannelWrapper {
	private Channel channel = null;
	private String name;
	public ChannelWrapper(Channel channel) {
		this.channel = channel;
		this.name = "Universe #"+this.channel.getUniverse()+" Channel #"+this.channel.getChannel();
	}
	public ChannelWrapper() {
		this.name = "Add channel";
	}
	public Channel getChannel() {
		return channel;
	}
	@Override
	public String toString() {
		return name;
	}
}
