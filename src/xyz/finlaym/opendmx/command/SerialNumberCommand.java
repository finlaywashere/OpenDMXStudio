package xyz.finlaym.opendmx.command;

import xyz.finlaym.opendmx.driver.ControllerHardware;

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
	public boolean handleResponse(byte[] response, ControllerHardware hardware) {
		if(response[0] != commandCode) return false;
		byte[] sn = new byte[10];
		for(int i = 1; i < response.length; i++) {
			sn[i-1] = response[i];
		}
		hardware.setSerialNumber(sn);
		return true;
	}
}
