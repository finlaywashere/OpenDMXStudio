package xyz.finlaym.opendmx.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.finlaym.opendmx.command.Command;

public class HardwareManager {
	
	private SerialDevice[] devices;
	private List<ControllerHardware> hardware;
	
	public HardwareManager() {
		setup();
	}
	
	public void setup() {
		devices = HardwareProbe.findDevices();
		hardware = new ArrayList<ControllerHardware>();
		for(SerialDevice d : devices) {
			if(d instanceof ControllerHardware) {
				// Find first controller
				hardware.add((ControllerHardware) d);
			}
		}
	}
	public int getTotalCommands() {
		int total = 0;
		for(ControllerHardware h : hardware) {
			total += h.getTotalCommands();
		}
		return total;
	}
	public int getFailureCount() {
		int total = 0;
		for(ControllerHardware h : hardware) {
			total += h.getFailureCount();
		}
		return total;
	}
	
	public boolean sendCommand(Command c) throws IOException {
		for(ControllerHardware h : hardware) {
			// Idk just send to everything, this will be changed in a later release when (if) RDM support is added or when multi device support is added
			if(!h.sendCommand(c))
				return true;
		}
		return true;
	}
	public List<ControllerHardware> getHardware() {
		return hardware;
	}

	public SerialDevice[] getDevices() {
		return devices;
	}
	
}
