package xyz.finlaym.opendmx.driver;

import com.fazecast.jSerialComm.SerialPort;

import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.command.IdentifyCommand;
import xyz.finlaym.opendmx.command.SerialNumberCommand;

public class HardwareProbe {
	public static SerialDevice[] findDevices() {
		SerialPort[] ports = SerialPort.getCommPorts();
		SerialDevice[] devices = new SerialDevice[ports.length];
		for(int i = 0; i < ports.length; i++) {
			try {
				SerialPort comPort = ports[i];
				comPort.setBaudRate(115200);
				comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
				comPort.openPort();
				ControllerHardware hardware = new ControllerHardware(comPort, HardwareStatus.UNKNOWN);
				IdentifyCommand c = new IdentifyCommand();
				boolean exists = hardware.sendCommand(c);
				if(exists) {
					if(OpenDMXStudio.DEBUG)
						System.out.println("Found OpenDMXController on port "+hardware.getSerialPort().getSystemPortName());
					hardware.setStatus(HardwareStatus.DISCONNECTED);
					
					SerialNumberCommand sn = new SerialNumberCommand();
					boolean success = hardware.sendCommand(sn);
					if(!success) {
						System.err.println("Error probing hardware serial number!");
					}
					if(success && OpenDMXStudio.DEBUG)
						System.out.println("Data for OpenDMXController on port "+hardware.getSerialPort().getSystemPortName()+": SN "+hardware.getSeralNumberString());
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
