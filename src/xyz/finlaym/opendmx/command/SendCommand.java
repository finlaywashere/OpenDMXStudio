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

package xyz.finlaym.opendmx.command;

import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.stage.Channel;

public class SendCommand extends Command {

	private int universe,value, channel;
	public SendCommand(int universe, int channel, int value) {
		super((byte) 1);
		this.universe = universe;
		this.channel = channel;
		this.value = value;
	}
	public SendCommand(Channel c, OpenDMXStudio studio) {
		this(c.getUniverse(), c.getChannel(), c.getCurrVal(studio));
	}
	public SendCommand(Channel c) {
		this(c.getUniverse(), c.getChannel(), c.getCurrValRaw());
	}
	public int getUniverse() {
		return universe;
	}
	public void setUniverse(int universe) {
		this.universe = universe;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	@Override
	public byte[] encode() {
		// Send has data format
		// command code, universe #, value low, value high
		return new byte[] {commandCode, (byte) universe, (byte) channel, (byte) (channel >> 8), (byte) value};
	}
	@Override
	public int responseLength() {
		return 1;
	}
	@Override
	public boolean handleResponse(byte[] response) {
		if(response[0] != commandCode) return false;
		return true;
	}

}
