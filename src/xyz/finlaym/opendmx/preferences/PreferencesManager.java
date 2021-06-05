package xyz.finlaym.opendmx.preferences;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class PreferencesManager {
	private static final File config = new File(new File(System.getProperty("user.home")),"OpenDMXStudio/preferences.ini");
	
	private File lastStage = null;
	
	public PreferencesManager() throws Exception{
		if(config.exists()) {
			Scanner in = new Scanner(config);
			if(in.hasNextLine()) {
				String last = in.nextLine();
				lastStage = new File(last);
			}
			in.close();
		}
	}
	public void save() throws Exception{
		if(!config.getParentFile().exists())
			config.getParentFile().mkdirs();
		config.delete();
		config.createNewFile();
		PrintWriter out = new PrintWriter(new FileWriter(config,true));
		if(lastStage != null)
			out.println(lastStage.getAbsolutePath());
		out.close();
	}
	public File getLastStage() {
		return lastStage;
	}
	public void setLastStage(File lastStage) {
		this.lastStage = lastStage;
	}
}
