package xyz.finlaym.opendmx.stage;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class StageContainer {
	private ArrayList<StageElement> elements;
	private Image background;
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
	public StageContainer(ArrayList<StageElement> elements, Image background) {
		this.elements = elements;
		this.background = background;
	}
}
