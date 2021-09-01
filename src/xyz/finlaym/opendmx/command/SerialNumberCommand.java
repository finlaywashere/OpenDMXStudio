package xyz.finlaym.opendmx.command;

import xyz.finlaym.opendmx.driver.ControllerHardware;

public class SerialNumberCommand extends Command {

	public SerialNumberCommand() {
		super((byte) 2);
	}

	@Override
	public byte[] encode() {
		return new byte[] {commandCode};
	}

	@Override
	public int responseLength() {
		// 8 serial number bytes + 1 status byte
		return 9;
	}

	@Override
	public boolean handleResponse(byte[] response, ControllerHardware hardware) {
		if(response[0] != commandCode) return false;
		byte[] sn = new byte[2];
		for(int i = 1; i < response.length; i++) {
			sn[i-1] = response[i];
		}
		hardware.setSerialNumber(sn);
		return true;
	}
}
