package xyz.finlaym.opendmx.driver;

import xyz.finlaym.opendmx.command.Command;

public class HardwareInterface {
	public static void sendCommand(Command c) {
		byte[] data = c.encode();
		
	}
}
