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

public class StageLoader {
	public static StageContainer loadStage(File stageDir) throws Exception{
		Image stageImg = new Image(new FileInputStream(new File(stageDir,"background.jpg")));
		File dataFile = new File(stageDir,"stage.data");
		ArrayList<StageElement> elements = new ArrayList<StageElement>();
		Scanner in = new Scanner(dataFile);
		int version = Integer.valueOf(in.nextLine());
		if(version == Constants.CONFIG_VERSION) {
			while(in.hasNextLine()) {
				String s = in.nextLine();
				if(s.trim().isEmpty())
					continue;
				String[] split = s.split(":",7);
				double x = Double.valueOf(split[0]);
				double y = Double.valueOf(split[1]);
				StageElementType type = StageElementType.valueOf(split[2]);
				String name = split[3];
				String[] split2 = split[4].split(",");
				int length = Integer.valueOf(split2[0]);
				Channel[] channels = new Channel[length];
				for(int i = 1; i <= length; i+=3) {
					channels[i/2] = new Channel(Integer.valueOf(split2[i]),Integer.valueOf(split2[i+1]), ChannelType.valueOf(split2[i+2]));
				}
				int radius = Integer.valueOf(split[5]);
				String[] split3 = split[6].split(",");
				Color color = Color.rgb(Integer.valueOf(split3[0]),Integer.valueOf(split3[1]),Integer.valueOf(split3[2]));
				StageElement elem = new StageElement(x,y,type,name,channels,radius,color);
				elements.add(elem);
			}
			in.close();
		}else {
			in.close();
			System.err.println("Error: Attempted to load invalid/new configuration that is unsupported!");
			// Handle the error with like an FX dialogue or smth idk
			return null;
		}
		return new StageContainer(elements,stageImg);
	}
	private static void deleteDirectory(File dir) {
		if(!dir.isDirectory()) {
			dir.delete();
			return;
		}
		for(File f : dir.listFiles()) {
			deleteDirectory(f);
		}
		dir.delete();
	}
	public static void saveStage(File stageDir, StageContainer stage) throws Exception{
		deleteDirectory(stageDir);
		stageDir.mkdirs();
		
		BufferedImage img = SwingFXUtils.fromFXImage(stage.getBackground(), null);
		ImageIO.write(img, "JPG", new File(stageDir,"background.jpg"));
		
		File stageData = new File(stageDir,"stage.data");
		stageData.createNewFile();
		
		PrintWriter out = new PrintWriter(new FileWriter(stageData,true));
		out.println(Constants.CONFIG_VERSION);
		for(StageElement elem : stage.getElements()) {
			out.println(elem.toString());
		}
		out.close();
	}
}