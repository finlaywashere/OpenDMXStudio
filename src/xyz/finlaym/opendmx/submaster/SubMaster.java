package xyz.finlaym.opendmx.submaster;

import java.util.ArrayList;
import java.util.List;

import xyz.finlaym.opendmx.stage.Channel;

public class SubMaster {
	private List<Channel> channels;
	private int id;
	private String name;
	public SubMaster(int id, String name) {
		this.channels = new ArrayList<Channel>();
		this.id = id;
		this.name = name;
	}
	public List<Channel> getChannels() {
		return channels;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
}
