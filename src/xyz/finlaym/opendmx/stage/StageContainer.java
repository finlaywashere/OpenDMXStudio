package xyz.finlaym.opendmx.stage;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class StageContainer {
	private ArrayList<StageElement> elements;
	private Image background;
	private File stageDir;
	
	public StageContainer(ArrayList<StageElement> elements, Image background, File stageDir) {
		this.elements = elements;
		this.background = background;
		this.stageDir = stageDir;
	}
	public ArrayList<StageElement> getElements() {
		return elements;
	}
	public void setElements(ArrayList<StageElement> elements) {
		this.elements = elements;
	}
	public Image getBackground() {
		return background;
	}
	public void setBackground(Image background) {
		this.background = background;
	}
	public File getStageDir() {
		return stageDir;
	}
}
