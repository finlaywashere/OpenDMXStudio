package xyz.finlaym.opendmx.command;

public class ResetCommand extends Command{

	public ResetCommand() {
		super((byte) 3);
	}

	@Override
	public byte[] encode() {
		return new byte[] {commandCode};
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
