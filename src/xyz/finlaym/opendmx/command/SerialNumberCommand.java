package xyz.finlaym.opendmx.command;

public class SerialNumberCommand extends Command {

	public SerialNumberCommand() {
		super((byte) 2);
	}

	@Override
	public byte[] encode() {
		return null;
	}

	@Override
	public int responseLength() {
		// 10 serial number bytes + 1 status byte
		return 11;
	}

	@Override
	public boolean handleResponse(byte[] response) {
		if(response[0] != commandCode) return false;
		
		return true;
	}
}
