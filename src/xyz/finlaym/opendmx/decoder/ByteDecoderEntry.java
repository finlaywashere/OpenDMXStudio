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

package xyz.finlaym.opendmx.decoder;

public class ByteDecoderEntry {
	private String value;
	private ByteDecoderType type;
	
	public ByteDecoderEntry(String value, ByteDecoderType type) {
		this.value = value;
		this.type = type;
	}
	public String getValueString() {
		return value;
	}
	public int getValueInt() {
		return Integer.valueOf(value);
	}
	public short getValueShort() {
		return Short.valueOf(value);
	}
	public byte getValueByte() {
		return Byte.valueOf(value);
	}
	public ByteDecoderType getType() {
		return type;
	}
	
}
