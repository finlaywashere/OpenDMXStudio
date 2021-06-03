package xyz.finlaym.opendmx.driver;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import xyz.finlaym.opendmx.command.Command;

public class ControllerHardware extends SerialDevice{
	public static final int MAGIC = 0xBEEF;
	
	private byte[] serialNumber;
	private HardwareStatus status;
	private int softwareVersion;
	private int hardwareVersion;
	private int protocol;
	
	
	public ControllerHardware(SerialPort serialPort, HardwareStatus status) {
		super(serialPort);
		this.status = status;
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
	public int getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(int softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public int getHardwareVersion() {
		return hardwareVersion;
	}
	public void setHardwareVersion(int hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}
	public int getProtocol() {
		return protocol;
	}
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
	public boolean sendCommand(Command c) throws IOException {
		byte[] data = c.encode();
		if(!serialPort.isOpen()) {
			System.err.println("Error: Port is not open!");
			return false;
		}
		serialPort.writeBytes(data, data.length);
		byte[] response = new byte[c.responseLength()];
		serialPort.readBytes(response, response.length);
		return c.handleResponse(response, this);
	}
}
