package xyz.finlaym.opendmx.command;

import xyz.finlaym.opendmx.driver.ControllerHardware;

public class IdentifyCommand extends Command{

	public IdentifyCommand() {
		super((byte) 4);
		
	}

	@Override
	public byte[] encode() {
		return new byte[] {commandCode};
	}

	@Override
	public int responseLength() {
		return 7;
	}

	@Override
	public boolean handleResponse(byte[] response, ControllerHardware hardware) {
		if(response[0] != commandCode) return false;
		int magic = (((int) response[1]) << 8) | response[2];
		if(magic != ControllerHardware.MAGIC) return false;
		hardware.setProtocol(response[3]);
		int softwareVersion = (((int) response[4]) << 8) | response[5];
		hardware.setSoftwareVersion(softwareVersion);
		hardware.setHardwareVersion(response[6]);
		return true;
	}

}
