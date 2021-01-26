package xyz.finlaym.opendmx.submaster;

import java.util.ArrayList;
import java.util.List;

public class SubMasterSet {
	private List<SubMaster> masters;

	public SubMasterSet() {
		this.masters = new ArrayList<SubMaster>();
	}
	public List<SubMaster> getMasters() {
		return masters;
	}
}
