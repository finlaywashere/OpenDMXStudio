package xyz.finlaym.opendmx.driver;

import com.fazecast.jSerialComm.SerialPort;

import xyz.finlaym.opendmx.command.IdentifyCommand;

public class HardwareProbe {
	public static SerialDevice[] findDevices() {
		SerialPort[] ports = SerialPort.getCommPorts();
		SerialDevice[] devices = new SerialDevice[ports.length];
		for(int i = 0; i < ports.length; i++) {
			try {
				SerialPort comPort = ports[i];
				comPort.setBaudRate(115200);
				comPort.openPort();
				ControllerHardware hardware = new ControllerHardware(comPort, HardwareStatus.UNKNOWN);
				IdentifyCommand c = new IdentifyCommand();
				boolean exists = hardware.sendCommand(c);
				if(exists) {
					hardware.setStatus(HardwareStatus.DISCONNECTED);
					devices[i] = hardware;
				}else {
					devices[i] = new SerialDevice(comPort);
				}
			}catch(Exception e) {
				System.err.println("Error probing serial port!");
				e.printStackTrace();
			}
		}
		return devices;
	}
}
