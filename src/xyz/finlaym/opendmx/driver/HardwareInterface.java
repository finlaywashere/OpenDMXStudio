/*
    Copyright (C) 2021 Finlay Maroney
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package xyz.finlaym.opendmx.driver;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import xyz.finlaym.opendmx.command.Command;

public class HardwareInterface {
	private SerialPort serial;
	
	public HardwareInterface(SerialPort serial) {
		this.serial = serial;
	}
	public SerialPort getSerial() {
		return serial;
	}
	public void sendCommand(Command c) throws IOException {
		byte[] data = c.encode();
		if(!serial.isOpen()) {
			System.err.println("Error: Port is not open!");
			return;
		}
		serial.writeBytes(data, data.length);
		byte[] result = new byte[4];
		serial.readBytes(result, result.length);
		for(byte b : result) {
			System.out.println("Data: "+Byte.toString(b));
		}
	}
}
