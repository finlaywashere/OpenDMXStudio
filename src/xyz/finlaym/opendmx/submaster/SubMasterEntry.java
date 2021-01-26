package xyz.finlaym.opendmx.submaster;

public class SubMasterEntry {
	private SubMasterEntryType type;
	private int id;
	public SubMasterEntry(SubMasterEntryType type, int id) {
		this.type = type;
		this.id = id;
	}
	public SubMasterEntryType getType() {
		return type;
	}
	public int getId() {
		return id;
	}
	@Override
	public String toString() {
		return id+","+type;
	}
	public static SubMasterEntry fromString(String s) {
		String[] split = s.split(",",2);
		int id = Integer.valueOf(split[0]);
		SubMasterEntryType type = SubMasterEntryType.valueOf(split[1]);
		return new SubMasterEntry(type, id);
	}
}
