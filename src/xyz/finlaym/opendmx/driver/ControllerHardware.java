package xyz.finlaym.opendmx.driver;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import xyz.finlaym.opendmx.command.Command;

public class ControllerHardware extends SerialDevice{
	protected byte[] serialNumber;
	protected HardwareStatus status;
	protected int softwareVersion;
	protected int hardwareVersion;
	protected int protocol;
	protected int numUniverses;
	protected int totalCommands;
	protected int failureCount;
	
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
	public int getNumUniverses() {
		return numUniverses;
	}
	public void setNumUniverses(int numUniverses) {
		this.numUniverses = numUniverses;
	}
	public int getTotalCommands() {
		return totalCommands;
	}
	public int getFailureCount() {
		return failureCount;
	}
	public String getSeralNumberString() {
		String sn = "";
		int middle = serialNumber.length/2-1;
		for(int i = 0; i < serialNumber.length; i++) {
			sn += Integer.toHexString(serialNumber[i]);
			if(i == middle)
				sn += "-";
		}
		return sn;
	}
	public boolean sendCommand(Command c) throws IOException {
		totalCommands++;
		byte[] data = c.encode();
		if(!serialPort.isOpen()) {
			System.err.println("Error: Port is not open!");
			return false;
		}
		serialPort.writeBytes(data, data.length);
		byte[] response = new byte[c.responseLength()];
		serialPort.readBytes(response, response.length);
		boolean success = c.handleResponse(response, this);
		if(!success)
			failureCount++;
		return success;
	}
}
