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

package xyz.finlaym.opendmx;

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import xyz.finlaym.opendmx.cue.CueSet;
import xyz.finlaym.opendmx.driver.ControllerHardware;
import xyz.finlaym.opendmx.driver.HardwareProbe;
import xyz.finlaym.opendmx.driver.SerialDevice;
import xyz.finlaym.opendmx.stage.ChannelRegistry;
import xyz.finlaym.opendmx.stage.StageContainer;
import xyz.finlaym.opendmx.stage.StageElement;
import xyz.finlaym.opendmx.stage.StageLoader;
import xyz.finlaym.opendmx.submaster.SubMasterLoader;
import xyz.finlaym.opendmx.submaster.SubMasterSet;
import xyz.finlaym.opendmx.ui.ModeUI;

public class OpenDMXStudio extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private ControllerHardware hardware = null;
	private Canvas canvas;
	private StageContainer currentStage;
	private ModeUI modeUI;
	private InterfaceMode mode = InterfaceMode.DEFAULT;
	private int selected = -1;
	private boolean dragged = false;
	private CueSet currCue = new CueSet();
	private ChannelRegistry cRegistry = new ChannelRegistry();
	private SubMasterSet masters;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Get devices list from the hardware prober
		SerialDevice[] devices = HardwareProbe.findDevices();
		for(SerialDevice d : devices) {
			if(d instanceof ControllerHardware) {
				// Find first controller
				hardware = (ControllerHardware) d;
			}
		}
		
		currentStage = StageLoader.loadStage(new File("test_stage/"));
		masters = SubMasterLoader.loadSubMasters(currentStage,this);
		
		// JavaFX stuff now
		Group root = new Group();
		root.setOnMousePressed(event -> {
			// When mouse pressed find the thingy
			int width = (int) canvas.getWidth();
			int height = (int) canvas.getHeight();
			int x = (int) event.getX();
			int y = (int) event.getY();
			for(int i = 0; i < currentStage.getElements().size(); i++) {
				StageElement elem = currentStage.getElements().get(i);
				int eX = (int) (elem.getX() * width);
				int eY = (int) (elem.getY() * height);
				if(eX < x && eX+elem.getRadius()*2 > x) {
					if(eY < y && eY+elem.getRadius()*2 > y) {
						selected = i;
						dragged = false;
						return;
					}
				}
			}
		});
		root.setOnMouseReleased(event -> {
			selected = -1;
			if(dragged) {
				dragged = false;
				return;
			}
			int width = (int) canvas.getWidth();
			int height = (int) canvas.getHeight();
			int x = (int) event.getX();
			int y = (int) event.getY();
			for(int i = 0; i < currentStage.getElements().size(); i++) {
				StageElement elem = currentStage.getElements().get(i);
				int eX = (int) (elem.getX() * width);
				int eY = (int) (elem.getY() * height);
				if(eX < x && eX+elem.getRadius()*2 > x) {
					if(eY < y && eY+elem.getRadius()*2 > y) {
						try {
							if(getMode() == InterfaceMode.MANUAL) {
								modeUI.control(i);
							}else if(getMode() == InterfaceMode.DEVICE) {
								modeUI.configure(i);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						return;
					}
				}
			}
		});
		root.setOnMouseDragged(event -> {
			dragged = true;
			if(selected == -1)
				return;
			if(getMode() != InterfaceMode.DEVICE)
				return;
			int width = (int) canvas.getWidth();
			int height = (int) canvas.getHeight();
			
			StageElement elem = currentStage.getElements().get(selected);
			
			double x = event.getX()/width - ((double) elem.getRadius()/width); // Convert width from x pixels to x (0.0-1.0) and offset by radius to put mouse in the middle
			double y = event.getY()/height - ((double) elem.getRadius()/height); // Same as x but for y
			
			x = Math.min(x, 1d);
			y = Math.min(y, 1d);
			
			elem.setX(x);
			elem.setY(y);
			
		});
		
		int width = 1000;//(int) primaryStage.getWidth();
		int height = 1000;//(int) primaryStage.getHeight();
		Scene s = new Scene(root, width, height, Color.WHITE);
		
		canvas = new Canvas(width,height);
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				GraphicsContext gc = canvas.getGraphicsContext2D();
				int width = (int) canvas.getWidth();
				int height = (int) canvas.getHeight();
				gc.drawImage(currentStage.getBackground(), 0, 0, width, height);
				for(StageElement elem : currentStage.getElements()) {
					int radius = elem.getRadius();
					int x = (int) (elem.getX()*width);
					int y = (int) (elem.getY()*height);
					Color c = elem.getColor();
					gc.setFill(c);
					gc.fillOval(x, y, radius*2, radius*2);
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		root.getChildren().add(canvas);
		
		primaryStage.setScene(s);
		primaryStage.setTitle("OpenDMXStudio");
		primaryStage.show();
		
		root.requestFocus();
		
		modeUI = new ModeUI(this);
		modeUI.start(primaryStage);
		
		timer.start();
	}
	public ControllerHardware getHardware() {
		return hardware;
	}

	public InterfaceMode getMode() {
		return mode;
	}
	public void setMode(InterfaceMode mode) {
		this.mode = mode;
		modeUI.update();
	}
	public StageContainer getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(StageContainer currentStage) {
		this.currentStage = currentStage;
	}
	public CueSet getCurrCue() {
		return currCue;
	}
	public void setCurrCue(CueSet currCue) {
		this.currCue = currCue;
	}
	public ModeUI getModeUI() {
		return modeUI;
	}
	public ChannelRegistry getCRegistry() {
		return cRegistry;
	}
	public SubMasterSet getMasters() {
		return masters;
	}
	public void setMasters(SubMasterSet masters) {
		this.masters = masters;
	}
}
