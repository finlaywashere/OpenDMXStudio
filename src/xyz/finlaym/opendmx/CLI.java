package xyz.finlaym.opendmx;

import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;

public class CLI {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Device Path: ");
		String devicePath = in.nextLine();
		SerialPort port;
		try {
			port = SerialPort.getCommPort(devicePath);
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Failed to open serial port!");
			in.close();
			return;
		}
		port.setBaudRate(115200);
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
		port.openPort();
		if(!port.isOpen()) {
			System.err.println("Failed to open serial port!");
			in.close();
			return;
		}
		System.out.println("Enter byte sequence seperated by space\nAlternatively enter 0 as the first byte to exit");
		while(true) {
			System.out.print("> ");
			String[] data = in.nextLine().split(" ");
			System.out.print("Response length: ");
			String rLen = in.nextLine();
			if(!Utils.isInt(rLen)) {
				System.err.println("Invalid response length!");
				continue;
			}
			byte[] dataI = new byte[data.length];
			boolean failure = false;
			for(int i = 0; i < data.length; i++) {
				String s = data[i];
				if(!Utils.isByte(s)) {
					System.err.println("Invalid byte!");
					break;
				}
				dataI[i] = Byte.decode(s);
			}
			if(failure) {
				continue;
			}
			if(dataI[0] == 0) {
				System.out.println("Exiting!");
				break;
			}
			port.writeBytes(dataI, dataI.length);
			byte[] response = new byte[Integer.valueOf(rLen)];
			port.readBytes(response, response.length);
			
			String resultHex = "";
			for(byte b : response) {
				resultHex += String.format("%02X", b)+" ";
			}
			String result = "";
			for(byte b : response) {
				result += b + " ";
			}
			System.out.println("Result: "+result);
			System.out.println("Result (hex): "+resultHex);
			System.out.println();
		}
		
		in.close();
	}
}
