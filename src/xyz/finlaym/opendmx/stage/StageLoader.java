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

package xyz.finlaym.opendmx.stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import xyz.finlaym.opendmx.Constants;
import xyz.finlaym.opendmx.Utils;

public class StageLoader {
	public static StageContainer loadStage(File stageDir) throws Exception{
		File background = new File(stageDir,"background.jpg");
		Image stageImg = null;
		if(background.exists())
			stageImg = new Image(new FileInputStream(background));
		File dataFile = new File(stageDir,"stage.data");
		ArrayList<StageElement> elements = new ArrayList<StageElement>();
		if(dataFile.exists()) {
			Scanner in = new Scanner(dataFile);
			int version = Integer.valueOf(in.nextLine());
			if(version == Constants.CONFIG_VERSION) {
				while(in.hasNextLine()) {
					String s = in.nextLine();
					if(s.trim().isEmpty())
						continue;
					String[] split = s.split(":",8);
					double x = Double.valueOf(split[0]);
					double y = Double.valueOf(split[1]);
					StageElementType type = StageElementType.valueOf(split[2]);
					String name = split[3];
					String[] split2 = split[4].split(",");
					int length = Integer.valueOf(split2[0]);
					Channel[] channels = new Channel[length];
					for(int i = 0; i < length; i++) {
						channels[i] = new Channel(Integer.valueOf(split2[i*5+1]),Integer.valueOf(split2[i*5+2]), ChannelType.valueOf(split2[i*5+3]), Integer.valueOf(split2[i*5+4]), Integer.valueOf(split2[i*5+5]));
					}
					int radius = Integer.valueOf(split[5]);
					String[] split3 = split[6].split(",");
					Color color = Color.rgb(Integer.valueOf(split3[0]),Integer.valueOf(split3[1]),Integer.valueOf(split3[2]));
					int id = Integer.valueOf(split[7]);
					StageElement elem = new StageElement(x,y,type,name,channels,radius,color,id);
					elements.add(elem);
				}
				in.close();
			}else {
				in.close();
				System.err.println("Error: Attempted to load invalid/new configuration that is unsupported!");
				// Handle the error with like an FX dialogue or smth idk
				return null;
			}
		}
		return new StageContainer(elements,stageImg,stageDir);
	}
	public static void saveStage(File stageDir, StageContainer stage) throws Exception{
		Utils.deleteDirectory(stageDir);
		stageDir.mkdirs();
		
		if(stage.getBackground() != null) {
			BufferedImage img = SwingFXUtils.fromFXImage(stage.getBackground(), null);
			ImageIO.write(img, "jpg", new File(stageDir,"background.jpg"));
		}
		
		File stageData = new File(stageDir,"stage.data");
		stageData.createNewFile();
		
		PrintWriter out = new PrintWriter(new FileWriter(stageData,true));
		out.println(Constants.CONFIG_VERSION);
		for(StageElement elem : stage.getElements()) {
			out.println(elem.encode());
		}
		out.close();
	}
}
