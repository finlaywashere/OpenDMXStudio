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

import xyz.finlaym.opendmx.stage.Channel;

public class CueEntry {
	private Channel newValue;
	private Channel oldValue;
	private CueTransitionType transitionType;
	private double transitionTime;
	
	public CueEntry(Channel newValue, Channel oldValue, CueTransitionType transitionType, double transitionTime) {
		this.newValue = newValue;
		this.oldValue = oldValue;
		this.transitionType = transitionType;
		this.transitionTime = transitionTime;
	}
	public Channel getNewValue() {
		return newValue;
	}
	public CueTransitionType getTransitionType() {
		return transitionType;
	}
	public double getTransitionTime() {
		return transitionTime;
	}
	public Channel getOldValue() {
		return oldValue;
	}
	@Override
	public String toString() {
		String ret = transitionType.toString()+":"+transitionTime+":"+oldValue.getCurrValRaw()+":"+newValue.getCurrValRaw()+":"+oldValue.toString()+":"+newValue.toString();
		return ret;
	}
	public static CueEntry fromString(String s) {
		String[] split = s.split(":",6);
		CueTransitionType type = CueTransitionType.valueOf(split[0]);
		double time = Double.valueOf(split[1]);
		int oldValue = Integer.valueOf(split[2]);
		int newValue = Integer.valueOf(split[3]);
		Channel oldValueC = Channel.fromString(split[4]);
		oldValueC.setCurrValRaw(oldValue);
		Channel newValueC = Channel.fromString(split[5]);
		newValueC.setCurrValRaw(newValue);
		return new CueEntry(newValueC, oldValueC, type, time);
	}
}
