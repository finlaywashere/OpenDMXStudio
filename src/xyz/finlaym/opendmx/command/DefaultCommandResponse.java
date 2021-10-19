package xyz.finlaym.opendmx.command;

import xyz.finlaym.opendmx.driver.ControllerHardware;

public class DefaultCommandResponse extends CommandResponse {

	public DefaultCommandResponse(byte[] bytes) {
		super(bytes);
	}

	@Override
	public boolean validate(Command cmd) {
		return getBytes().length > 0 && getBytes()[0] == cmd.getCommandCode();
	}

	@Override
	public boolean applyResponse(ControllerHardware hardware) {
		return true;
	}

}
