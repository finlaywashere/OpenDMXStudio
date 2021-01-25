package xyz.finlaym.opendmx.command;

import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.stage.Channel;

public class SendCommand extends Command {

	private int universe,value, channel;
	public SendCommand(int universe, int channel, int value) {
		super(1);
		this.universe = universe;
		this.channel = channel;
		this.value = value;
	}
	public SendCommand(Channel c, OpenDMXStudio studio) {
		this(c.getUniverse(), c.getChannel(), c.getCurrVal(studio));
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
		return new byte[] {(byte) commandCode, (byte) universe, (byte) channel, (byte) (channel >> 8), (byte) value};
	}

}
