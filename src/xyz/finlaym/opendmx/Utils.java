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

package xyz.finlaym.opendmx;

import java.io.File;

import xyz.finlaym.opendmx.stage.Channel;
import xyz.finlaym.opendmx.stage.StageElement;

public class Utils {

	public static void deleteDirectory(File dir) {
		if(!dir.isDirectory()) {
			dir.delete();
			return;
		}
		for(File f : dir.listFiles()) {
			deleteDirectory(f);
		}
		dir.delete();
	}
	@SuppressWarnings("unused")
	public static boolean isInt(String s) {
		try {
			int i = Integer.valueOf(s);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	@SuppressWarnings("unused")
	public static boolean isByte(String s) {
		try {
			byte b = Byte.decode(s);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	public static Channel locateChannel(int id, OpenDMXStudio dmx) {
		for(StageElement e : dmx.getCurrentStage().getElements()) {
			for(Channel c : e.getChannels()) {
				if(c.getId() == id)
					return c;
			}
		}
		return null;
	}
	public static StageElement locateElement(int id, OpenDMXStudio dmx) {
		for(StageElement e : dmx.getCurrentStage().getElements()) {
			if(e.getId() == id)
				return e;
		}
		return null;
	}
}
