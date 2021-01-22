package xyz.finlaym.opendmx.ui;

import xyz.finlaym.opendmx.stage.Channel;

public class ChannelWrapper {
	private Channel channel = null;
	private String name;
	public ChannelWrapper(Channel channel, int id) {
		this.channel = channel;
		this.name = "Channel #"+id;
	}
	public ChannelWrapper(String name) {
		this.name = name;
	}
	public Channel getChannel() {
		return channel;
	}
	@Override
	public String toString() {
		return name;
	}
}
