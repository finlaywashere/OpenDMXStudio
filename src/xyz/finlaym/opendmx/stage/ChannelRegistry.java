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
