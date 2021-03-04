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

package xyz.finlaym.opendmx.cue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.finlaym.opendmx.driver.HardwareInterface;

public class CueSet {
	private List<CueContainer> cues;
	private int curr = 0;
	
	public CueSet() {
		this.cues = new ArrayList<CueContainer>();
	}
	public List<CueContainer> getCues() {
		return cues;
	}
	public boolean execute(HardwareInterface hw) throws IOException{
		if(curr >= cues.size())
			return false;
		cues.get(curr).execute(hw);
		curr++;
		return true;
	}
}
