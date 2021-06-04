package xyz.finlaym.opendmx.ui;

import static xyz.finlaym.opendmx.ui.UIConstants.FONT_MEDIUM;
import static xyz.finlaym.opendmx.ui.UIConstants.GAP;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.driver.ControllerHardware;
import xyz.finlaym.opendmx.driver.SerialDevice;

public class HardwareUI {
	private Stage modeStage;
	private OpenDMXStudio dmx;
	private ComboBox<HardwareMenu> cbxHardware;
	
	public HardwareUI(OpenDMXStudio dmx) {
		this.dmx = dmx;
	}
	
	public void start(Stage primaryStage) {
		modeStage = new Stage();
		modeStage.initOwner(primaryStage);
		initDefault();
	}
	private void initDefault() {
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblTitle = new Label("OpenDMXStudio Hardware Control Panel");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		cbxHardware = new ComboBox<HardwareMenu>();
		fillHardwareMenu();
		if(cbxHardware.getItems().size() > 0)
			cbxHardware.getSelectionModel().select(0);
		
		root.add(cbxHardware, 0, 2);
		
		Scene s = new Scene(root, 600, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void fillHardwareMenu() {
		cbxHardware.getItems().clear();
		for(ControllerHardware h : dmx.getHardwareManager().getHardware()) {
			HardwareMenu m = new HardwareMenu(h);
			cbxHardware.getItems().add(m);
		}
		for(SerialDevice s : dmx.getHardwareManager().getDevices()) {
			if(!(s instanceof ControllerHardware)) {
				// Its not an OpenDMXController, included at the bottom anyway tho
				HardwareMenu m = new HardwareMenu(s);
				cbxHardware.getItems().add(m);
			}
		}
	}
	//TODO: Make this class much much better
	private class HardwareMenu{
		private String text;
		
		public HardwareMenu(SerialDevice s) {
			this.text = "Serial Device: "+s.getSerialPort().getDescriptivePortName();
		}
		public HardwareMenu(ControllerHardware h) {
			this.text = "OpenDMXController: "+h.getSerialPort().getDescriptivePortName();
		}
		@Override
		public String toString() {
			return text;
		}
	}
}
