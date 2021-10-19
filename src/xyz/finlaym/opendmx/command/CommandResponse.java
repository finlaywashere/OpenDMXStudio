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

package xyz.finlaym.opendmx.command;

import xyz.finlaym.opendmx.driver.ControllerHardware;

public abstract class CommandResponse {
	protected boolean success = false;
	private byte[] bytes;

	public CommandResponse(byte[] bytes) {
		this.bytes = bytes;
	}
	public abstract boolean validate(Command cmd);
	public abstract boolean applyResponse(ControllerHardware hardware);
	
	public boolean isSuccess() {
		return success;
	}
	public byte[] getBytes() {
		return bytes;
	}
}