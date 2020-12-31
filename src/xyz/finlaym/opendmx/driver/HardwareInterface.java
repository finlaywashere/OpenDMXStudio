package xyz.finlaym.opendmx.driver;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import xyz.finlaym.opendmx.command.Command;

public class HardwareInterface {
	private SerialPort serial;
	
	public HardwareInterface(SerialPort serial) {
		this.serial = serial;
	}
	public SerialPort getSerial() {
		return serial;
	}
	public void sendCommand(Command c) throws IOException {
		byte[] data = c.encode();
		serial.getOutputStream().write(data);
		serial.getOutputStream().flush();
	}
}
