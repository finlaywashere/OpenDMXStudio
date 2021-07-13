package xyz.finlaym.opendmx.virtualdevice;

import java.io.IOException;

import xyz.finlaym.opendmx.command.Command;
import xyz.finlaym.opendmx.command.SendCommand;
import xyz.finlaym.opendmx.driver.ControllerHardware;
import xyz.finlaym.opendmx.driver.HardwareStatus;

public class VirtualHardware extends ControllerHardware{

	private VirtualDeviceUI ui;
	
	public VirtualHardware(VirtualDeviceUI ui) {
		super(null, HardwareStatus.CONNECTED);
		this.ui = ui;
		this.serialNumber = new byte[24];
		this.softwareVersion = 5;
		this.hardwareVersion = 5;
		this.protocol = 1<<8;
		this.numUniverses = 1;
	}
	@Override
	public boolean sendCommand(Command c) throws IOException {
		// Intercept commands and do magic
		
		// We only care about the send command because all of the info from other commands is hard coded
		
		if(c instanceof SendCommand) {
			SendCommand send = (SendCommand) c;
			ui.handleCommand(send);
		}
		
		return true;
	}
}
