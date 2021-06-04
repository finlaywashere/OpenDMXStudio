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
		int magic = ((int) response[1]);
		
		if(magic != (byte) 0xff) return false;
		
		hardware.setProtocol(response[2]);
		int softwareVersion = (((int) response[3]) << 8) | response[4];
		hardware.setSoftwareVersion(softwareVersion);
		hardware.setHardwareVersion(response[5]);
		hardware.setNumUniverses(response[6]);
		return true;
	}

}
