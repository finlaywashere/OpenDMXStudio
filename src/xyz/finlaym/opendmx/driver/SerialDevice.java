package xyz.finlaym.opendmx.driver;

import com.fazecast.jSerialComm.SerialPort;

public class SerialDevice {
	protected SerialPort serialPort;

	public SerialDevice(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public SerialPort getSerialPort() {
		return serialPort;
	}
}
