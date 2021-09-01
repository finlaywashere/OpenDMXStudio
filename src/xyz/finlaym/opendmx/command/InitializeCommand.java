package xyz.finlaym.opendmx.command;

import xyz.finlaym.opendmx.driver.ControllerHardware;
import xyz.finlaym.opendmx.driver.HardwareStatus;

public class InitializeCommand extends Command {

	private int universeSize = 512;
	public InitializeCommand(int universeSize) {
		super((byte) 5);
		this.universeSize = universeSize;
	}
	public InitializeCommand() {
		super((byte) 5);
	}

	@Override
	public byte[] encode() {
		return new byte[] {commandCode, (byte) universeSize, (byte) (universeSize >> 8)};
	}

	@Override
	public int responseLength() {
		return 2;
	}

	@Override
	public boolean handleResponse(byte[] response, ControllerHardware hardware) {
		if(response[0] != commandCode) return false;
		if(response[1] == 0) {
			hardware.setStatus(HardwareStatus.CONNECTED);
			return true;
		}else {
			hardware.setStatus(HardwareStatus.FAILURE);
			System.err.println("Hardware failed self test with code "+response[1]);
			return false;
		}
	}

}
