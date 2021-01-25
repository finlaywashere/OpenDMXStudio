package xyz.finlaym.opendmx.stage;

import java.util.HashMap;
import java.util.Map;

public class ChannelRegistry {
	private Map<Integer,Map<Integer,Integer>> values;
	public ChannelRegistry() {
		values = new HashMap<Integer,Map<Integer,Integer>>();
	}
	public Map<Integer, Map<Integer, Integer>> getValues() {
		return values;
	}
	public void setChannel(Channel c) {
		setChannel(c.getUniverse(), c.getChannel(), c.getCurrValRaw());
	}
	public void setChannel(int universe, int channel, int value) {
		Map<Integer,Integer> map = values.get(universe);
		if(map == null)
			map = new HashMap<Integer,Integer>();
		map.put(channel, value);
		values.put(universe, map);
	}
	public int getChannel(int universe, int channel) {
		Map<Integer,Integer> map = values.get(universe);
		if(map == null)
			return 0;
		return map.get(channel);
	}
	public int getChannel(Channel c) {
		return getChannel(c.getUniverse(), c.getChannel());
	}
}
