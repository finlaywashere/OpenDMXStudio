package xyz.finlaym.opendmx.driver;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import xyz.finlaym.opendmx.command.Command;

public class ControllerHardware {
	private SerialPort serialPort;
	private byte[] serialNumber;
	private HardwareStatus status;
	public ControllerHardware(SerialPort serialPort, HardwareStatus status) {
		this.serialPort = serialPort;
		this.status = status;
	}
	public SerialPort getSerialPort() {
		return serialPort;
	}
	public byte[] getSerialNumber() {
		return serialNumber;
	}
	public HardwareStatus getStatus() {
		return status;
	}
	public void setSerialNumber(byte[] serialNumber) {
		this.serialNumber = serialNumber;
	}
	public void setStatus(HardwareStatus status) {
		this.status = status;
	}
	public void sendCommand(Command c) throws IOException {
		byte[] data = c.encode();
		if(!serialPort.isOpen()) {
			System.err.println("Error: Port is not open!");
			return;
		}
		serialPort.writeBytes(data, data.length);
		
	}
}
