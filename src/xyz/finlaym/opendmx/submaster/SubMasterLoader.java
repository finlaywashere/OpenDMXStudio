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

package xyz.finlaym.opendmx.submaster;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.stage.StageContainer;

public class SubMasterLoader {
	public static SubMasterSet loadSubMasters(StageContainer stage, OpenDMXStudio dmx) throws Exception{
		File f = new File(stage.getStageDir(), "masters.sub");
		if(!f.exists()) {
			System.err.println("Submaster file not found, skipping!");
			return new SubMasterSet();
		}
		Scanner in = new Scanner(f);
		int count = -1;
		int i = 0;
		SubMasterSet set = new SubMasterSet();
		SubMaster currMaster = null;
		while(in.hasNextLine()) {
			String s = in.nextLine();
			if(s.trim().equals(""))
				continue;
			if(s.equals("EOF")) {
				if(currMaster != null) 
					set.getMasters().add(currMaster);
				break;
			}
			if(currMaster == null || i >= count) {
				if(i >= count && count != -1) {
					set.getMasters().add(currMaster);
					currMaster = null;
				}
				String[] split = s.split(":",4);
				int id = Integer.valueOf(split[2]);
				SubMasterType type = SubMasterType.valueOf(split[1]);
				String name = split[3];
				count = Integer.valueOf(split[0]);
				i = 0;
				currMaster = new SubMaster(id, name, type);
			}else {
				currMaster.getChannels().add(SubMasterEntry.fromString(s,dmx));
				i++;
			}
		}
		in.close();
		return set;
	}
	public static void saveSubMasters(StageContainer stage, SubMasterSet master) throws Exception{
		File f = new File(stage.getStageDir(), "masters.sub");
		if(!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		f.delete();
		f.createNewFile();
		PrintWriter out = new PrintWriter(new FileWriter(f,true));
		
		for(SubMaster m : master.getMasters()) {
			out.println(m.getChannels().size()+":"+m.getType()+":"+m.getId()+":"+m.getName());
			for(SubMasterEntry i : m.getChannels()) {
				out.println(i.encode());
			}
		}
		out.println("EOF");
		
		out.close();
	}
}
