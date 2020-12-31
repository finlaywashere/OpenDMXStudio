package xyz.finlaym.opendmx;

import com.fazecast.jSerialComm.SerialPort;

import javafx.application.Application;
import javafx.stage.Stage;
import xyz.finlaym.opendmx.driver.HardwareInterface;

public class OpenDMXStudio extends Application{
	public static void main(String[] args) {
		launch(args);
	}
	private HardwareInterface hwInterface;
	@Override
	public void start(Stage primaryStage) throws Exception {
		// We starting bois
		SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.setBaudRate(9600);
		comPort.openPort();
		
		// Now we have an open port
		hwInterface = new HardwareInterface(comPort);
		
	}
	public HardwareInterface getHwInterface() {
		return hwInterface;
	}
}
