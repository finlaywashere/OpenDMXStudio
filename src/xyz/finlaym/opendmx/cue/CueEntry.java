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
		String ret = transitionType.toString()+":"+transitionTime+":"+oldValue.getCurrVal()+":"+newValue.getCurrVal()+":"+oldValue.toString()+":"+newValue.toString();
		return ret;
	}
	public static CueEntry fromString(String s) {
		String[] split = s.split(":",6);
		CueTransitionType type = CueTransitionType.valueOf(split[0]);
		double time = Double.valueOf(split[1]);
		int oldValue = Integer.valueOf(split[2]);
		int newValue = Integer.valueOf(split[3]);
		Channel oldValueC = Channel.fromString(split[4]);
		oldValueC.setCurrVal(oldValue);
		Channel newValueC = Channel.fromString(split[5]);
		newValueC.setCurrVal(newValue);
		return new CueEntry(newValueC, oldValueC, type, time);
	}
}
