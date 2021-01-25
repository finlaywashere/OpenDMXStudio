package xyz.finlaym.opendmx.cue;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import xyz.finlaym.opendmx.stage.StageContainer;

public class CueLoader {
	public static CueSet loadCue(StageContainer stage, File f) throws Exception{
		Scanner in = new Scanner(f);
		CueSet set = new CueSet();
		CueContainer currCue = null;
		int count = -1;
		int i = 0;
		while(in.hasNextLine()) {
			String s = in.nextLine();
			if(s.trim().equals(""))
				continue;
			if(s.equals("EOF")) {
				if(currCue != null) 
					set.getCues().add(currCue);
				break;
			}
			if(currCue == null || i >= count) {
				if(i >= count && count != -1) {
					set.getCues().add(currCue);
					currCue = null;
				}
				String[] split = s.split(":",3);
				String cName = split[0];
				count = Integer.valueOf(split[1]);
				i = 0;
				currCue = new CueContainer(cName);
			}else {
				currCue.getEntries().add(CueEntry.fromString(s));
				i++;
			}
		}
		in.close();
		return set;
	}
	public static void saveCue(StageContainer stage, CueSet cue, File f) throws Exception{
		if(!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		f.delete();
		f.createNewFile();
		
		PrintWriter out = new PrintWriter(new FileWriter(f,true));
		for(CueContainer c : cue.getCues()) {
			out.println(c.getName()+":"+c.getEntries().size());
			for(CueEntry e : c.getEntries()) {
				out.println(e.toString());
			}
		}
		out.println("EOF");
		out.close();
	}
}
