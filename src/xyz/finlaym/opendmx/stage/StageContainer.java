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
