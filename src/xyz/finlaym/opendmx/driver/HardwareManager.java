package xyz.finlaym.opendmx.driver;

import java.io.IOException;

import xyz.finlaym.opendmx.command.Command;
import xyz.finlaym.opendmx.command.SendCommand;

public class HardwareManager {
	
	private SerialDevice[] devices;
	private ControllerHardware[] hardware;
	
	public HardwareManager() {
		setup();
	}
	
	public void setup() {
		devices = HardwareProbe.findDevices();
		int count = 0;
		for(SerialDevice d : devices) {
			if(d instanceof ControllerHardware) {
				// Find first controller
				count++;
			}
		}
		hardware = new ControllerHardware[count];
		count = 0;
		for(SerialDevice d : devices) {
			if(d instanceof ControllerHardware) {
				// Find first controller
				hardware[count] = (ControllerHardware) d;
				count++;
			}
		}
	}
	
	public boolean sendCommand(Command c) throws IOException {
		int curr = 0;
		for(ControllerHardware h : hardware) {
			if(c instanceof SendCommand) {
				int universe = ((SendCommand)c).getUniverse();
				if(curr + 1 >= universe && curr + h.getNumUniverses() <= universe) {
					return h.sendCommand(c);
				}else {
					curr += h.getNumUniverses();
				}
			}else {
				// Idk just send most commands to everything, this will be changed in a later release when (if) RDM support is added
				if(!h.sendCommand(c))
					return false;
			}
		}
		return true;
	}
	public ControllerHardware[] getHardware() {
		return hardware;
	}

	public SerialDevice[] getDevices() {
		return devices;
	}
	
}
