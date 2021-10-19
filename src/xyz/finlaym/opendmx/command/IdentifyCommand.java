package xyz.finlaym.opendmx.command;

import java.util.Map;

import xyz.finlaym.opendmx.decoder.ByteDecoder;
import xyz.finlaym.opendmx.decoder.ByteDecoderEntry;
import xyz.finlaym.opendmx.decoder.ByteDecoderSpec;
import xyz.finlaym.opendmx.decoder.ByteDecoderType;
import xyz.finlaym.opendmx.driver.ControllerHardware;

public class IdentifyCommand extends Command{

	public IdentifyCommand() {
		super((byte) 4);
	}

	@Override
	public byte[] encode() {
		return new byte[] {commandCode};
	}

	@Override
	public int responseLength() {
		return 7;
	}

	public class IdentityCommandResponse extends CommandResponse{
		
		private int softwareVersion;
		private int hardwareVersion;
		private int universeCount;
		private int protocol;
		
		public IdentityCommandResponse(byte[] response) {
			super(response);
		}

		@Override
		public boolean applyResponse(ControllerHardware hardware) {
			if(hardware == null) return false;
			hardware.setNumUniverses(universeCount);
			hardware.setProtocol(protocol);
			hardware.setSoftwareVersion(softwareVersion);
			hardware.setHardwareVersion(hardwareVersion);
			return true;
		}

		@Override
		public boolean validate(Command cmd) {
			Map<String,ByteDecoderEntry> entries = ByteDecoder.getValues(getBytes(), 
					new ByteDecoderSpec(ByteDecoderType.NUMBER, 1, "code"),
					new ByteDecoderSpec(ByteDecoderType.NUMBER, 1, "magic"),
					new ByteDecoderSpec(ByteDecoderType.NUMBER, 1, "protocol"),
					new ByteDecoderSpec(ByteDecoderType.NUMBER, 2, "software_version"),
					new ByteDecoderSpec(ByteDecoderType.NUMBER, 1, "hardware_version"),
					new ByteDecoderSpec(ByteDecoderType.NUMBER, 1, "universe_count"));
			
			if(entries == null) return false;
			if(entries.get("code").getValueByte() != cmd.getCommandCode()) return false;
			if(entries.get("magic").getValueByte() != 0xFF) return false;
			this.protocol = entries.get("protocol").getValueInt();
			this.softwareVersion = entries.get("software_version").getValueInt();
			this.hardwareVersion = entries.get("hardware_version").getValueInt();
			this.universeCount = entries.get("universe_count").getValueInt();
			return true;
		}
		
	}
}
