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

import java.util.HashMap;
import java.util.Map;

public class ByteDecoder {
	
	public static Map<String,ByteDecoderEntry> getValues(byte[] bytes, ByteDecoderSpec... spec){
		return getValues(bytes, 0, spec);
	}
	public static Map<String,ByteDecoderEntry> getValues(byte[] bytes, int offset, ByteDecoderSpec... spec){
		int len = 0;
		for(int i = 0; i < spec.length; i++) {
			len += spec[i].getLength();
		}
		if(len < bytes.length + offset)
			return null;
		
		Map<String,ByteDecoderEntry> entries = new HashMap<String,ByteDecoderEntry>();
		int index = offset;
		for(int i = 0; i < spec.length; i++) {
			ByteDecoderSpec s = spec[i];
			if(s.getType() == ByteDecoderType.NUMBER) {
				long l = 0;
				for(int i2 = 0; i2 < s.getLength(); i2++) {
					l |= (((long)bytes[index + i2]) << i2 * 8);
				}
				entries.put(s.getName(), new ByteDecoderEntry(String.valueOf(l), s.getType()));
			}else if(s.getType() == ByteDecoderType.STRING){
				String str = "";
				for(int i2 = 0; i2 < s.getLength(); i2++) {
					str += (char) bytes[index+i2];
				}
				entries.put(s.getName(), new ByteDecoderEntry(str, s.getType()));
			}
			index += s.getLength();
		}
		
		return entries;
	}
}
