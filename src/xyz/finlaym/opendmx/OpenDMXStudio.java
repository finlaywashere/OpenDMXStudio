package xyz.finlaym.opendmx;

import java.io.File;
import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import xyz.finlaym.opendmx.driver.HardwareInterface;
import xyz.finlaym.opendmx.stage.StageContainer;
import xyz.finlaym.opendmx.stage.StageElement;
import xyz.finlaym.opendmx.stage.StageLoader;

public class OpenDMXStudio extends Application{
	public static void main(String[] args) {
		launch(args);
	}
	
	private HardwareInterface hwInterface;
	private Canvas canvas;
	private StageContainer currentStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// We starting bois
		SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.setBaudRate(9600);
		comPort.openPort();
		
		// Now we have an open port
		hwInterface = new HardwareInterface(comPort);
		
		currentStage = StageLoader.loadStage(new File("test_stage/"));
		
		// JavaFX stuff now
		Group root = new Group();
		root.setOnMouseClicked(event -> {
			int width = (int) canvas.getWidth();
			int height = (int) canvas.getHeight();
			int x = (int) event.getX();
			int y = (int) event.getY();
			for(StageElement elem : currentStage.getElements()) {
				int eX = (int) (elem.getX() * width);
				int eY = (int) (elem.getY() * height);
				if(eX < x && eX+elem.getRadius()*2 > x) {
					if(eY < y && eY+elem.getRadius()*2 > y) {
						try {
							elem.onClick(this);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
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
		
		timer.start();
	}
	public HardwareInterface getHwInterface() {
		return hwInterface;
	}
}
