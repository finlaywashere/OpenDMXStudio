package xyz.finlaym.opendmx.command;

public abstract class Command {
	
	protected int commandCode;
	public Command(int commandCode) {
		this.commandCode = commandCode;
	}
	
	public int getCommandCode() {
		return commandCode;
	}

	public abstract byte[] encode();
}
