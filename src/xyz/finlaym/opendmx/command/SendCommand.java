package xyz.finlaym.opendmx.command;

public class SendCommand extends Command {

	private int universe,value;
	public SendCommand(int universe, int value) {
		super(1);
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
	@Override
	public byte[] encode() {
		// Send has data format
		// command code, universe #, value low, value high
		return new byte[] {(byte) commandCode, (byte) universe, (byte) value, (byte) (value >> 8)};
	}

}
