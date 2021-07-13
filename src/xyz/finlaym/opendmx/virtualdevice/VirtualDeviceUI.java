package xyz.finlaym.opendmx.virtualdevice;

import static xyz.finlaym.opendmx.ui.UIConstants.FONT_MEDIUM;
import static xyz.finlaym.opendmx.ui.UIConstants.FONT_SMALL;
import static xyz.finlaym.opendmx.ui.UIConstants.GAP;
import static xyz.finlaym.opendmx.virtualdevice.VirtualDeviceType.MOTORIZED;
import static xyz.finlaym.opendmx.virtualdevice.VirtualDeviceType.RGB;
import static xyz.finlaym.opendmx.virtualdevice.VirtualDeviceType.SINGLE_CHANNEL;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.Utils;
import xyz.finlaym.opendmx.command.SendCommand;

public class VirtualDeviceUI {
	private Stage virtualStage;
	private OpenDMXStudio dmx;
	private VirtualDeviceType selected = RGB;
	private int selectedIndex;
	private int baseChannel;
	private int universe;
	// Initialize now to prevent null pointers
	private ColorPicker picker = new ColorPicker();
	
	public VirtualDeviceUI(OpenDMXStudio dmx) {
		this.dmx = dmx;
		// Clear colour to make my life easier
		this.picker.setValue(Color.BLACK);
		// Hook in virtual hardware
		this.dmx.getHardwareManager().getHardware().add(new VirtualHardware(this));
	}
	
	public void start(Stage modeStage) {
		this.virtualStage = new Stage();
		this.virtualStage.initOwner(modeStage);
		setup();
	}
	private void setup() {
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblTitle = new Label("OpenDMXStudio Virtual Device");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		Label lblTypes = new Label("Fixture Type: ");
		lblTypes.setFont(Font.font(FONT_SMALL));
		root.add(lblTypes, 0, 1);
		
		ComboBox<VirtualDeviceType> cbxTypes = new ComboBox<VirtualDeviceType>();
		cbxTypes.getItems().addAll(RGB,SINGLE_CHANNEL,MOTORIZED);
		cbxTypes.getSelectionModel().select(selectedIndex);
		root.add(cbxTypes, 1, 1);
		
		Label lblUniverse = new Label("Universe: ");
		lblUniverse.setFont(Font.font(FONT_SMALL));
		root.add(lblUniverse, 0, 2);
		
		TextField txtUniverse = new TextField(String.valueOf(universe));
		txtUniverse.setFont(Font.font(FONT_SMALL));
		root.add(txtUniverse, 1, 2);
		
		Label lblBaseChannel = new Label("Base Channel: ");
		lblBaseChannel.setFont(Font.font(FONT_SMALL));
		root.add(lblBaseChannel, 0, 3);
		
		TextField txtBaseChannel = new TextField(String.valueOf(baseChannel));
		txtBaseChannel.setFont(Font.font(FONT_SMALL));
		root.add(txtBaseChannel, 1, 3);
		
		Label lblStatus = new Label();
		lblStatus.setFont(Font.font(FONT_SMALL));
		root.add(lblStatus, 0, 4);
		
		Button btnApply = new Button("Apply");
		btnApply.setOnAction(event -> {
			if(!Utils.isInt(txtUniverse.getText())) {
				lblStatus.setText("Universe must be an integer!");
				return;
			}
			if(!Utils.isInt(txtBaseChannel.getText())) {
				lblStatus.setText("Base channel must be an integer!");
				return;
			}
			this.universe = Integer.valueOf(txtUniverse.getText());
			this.baseChannel = Integer.valueOf(txtBaseChannel.getText());
			this.selectedIndex = cbxTypes.getSelectionModel().getSelectedIndex();
			this.selected = cbxTypes.getValue();
			setup();
		});
		root.add(btnApply, 1, 4);
		
		if(selected == RGB) {
			Label lblColour = new Label("Colour: ");
			lblColour.setFont(Font.font(FONT_SMALL));
			root.add(lblColour, 0, 5);
			
			this.picker = new ColorPicker();
			this.picker.setDisable(true);
			this.picker.setValue(Color.BLACK);
			root.add(picker, 1, 5);
		}else if(selected == SINGLE_CHANNEL) {
			
		}else if(selected == MOTORIZED) {
			
		}
		
		Scene s = new Scene(root, 600, 400);
		virtualStage.setScene(s);
		virtualStage.setTitle("Virtual Device");
		virtualStage.show();
	}
	public void handleCommand(SendCommand send) {
		if(send.getUniverse() == universe && send.getChannel() >= baseChannel) {
			int numChannels = 0;
			if(selected == RGB) {
				numChannels = 3;
			}else if(selected == SINGLE_CHANNEL) {
				numChannels = 1;
			}else if(selected == MOTORIZED) {
				numChannels = 6;
			}
			if(send.getChannel() < baseChannel + numChannels) {
				// This device is selected
				if(selected == RGB) {
					Color curr = picker.getValue();
					Color newColour = null;
					switch(send.getChannel()-baseChannel) {
					case 0:
						// Red
						newColour = new Color(((double) send.getValue())/255, curr.getGreen(), curr.getBlue(), curr.getOpacity());
						break;
					case 1:
						// Green
						newColour = new Color(curr.getRed(), ((double) send.getValue())/255, curr.getBlue(), curr.getOpacity());
						break;
					case 2:
						// Blue
						newColour = new Color(curr.getRed(), curr.getGreen(), ((double) send.getValue())/255, curr.getOpacity());
						break;
					}
					picker.setValue(newColour);
				}else if(selected == SINGLE_CHANNEL) {
					
				}else if(selected == MOTORIZED) {
					
				}
			}
		}
	}
}
