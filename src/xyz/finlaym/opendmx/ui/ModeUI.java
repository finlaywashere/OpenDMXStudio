package xyz.finlaym.opendmx.ui;

import static xyz.finlaym.opendmx.stage.StageElementType.LIGHT;
import static xyz.finlaym.opendmx.stage.StageElementType.OTHER;
import static xyz.finlaym.opendmx.stage.StageElementType.SMOKE_MACHINE;
import static xyz.finlaym.opendmx.ui.UIConstants.FONT_MEDIUM;
import static xyz.finlaym.opendmx.ui.UIConstants.FONT_SMALL;
import static xyz.finlaym.opendmx.ui.UIConstants.GAP;

import static xyz.finlaym.opendmx.stage.ChannelType.*;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import xyz.finlaym.opendmx.InterfaceMode;
import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.stage.Channel;
import xyz.finlaym.opendmx.stage.ChannelType;
import xyz.finlaym.opendmx.stage.StageElement;
import xyz.finlaym.opendmx.stage.StageElementType;

public class ModeUI {
	private Stage modeStage;
	private int elemIndex = -1;
	
	public void start(Stage primaryStage) {
		modeStage = new Stage();
		modeStage.initOwner(primaryStage);
		initDefault();
	}
	public void configure(int elemIndex) {
		this.elemIndex = elemIndex;
		OpenDMXStudio.INSTANCE.setMode(InterfaceMode.DEVICE);
	}
	public void reset() {
		OpenDMXStudio.INSTANCE.setMode(InterfaceMode.DEFAULT);
		elemIndex = -1;
	}
	public void update() {
		switch(OpenDMXStudio.INSTANCE.getMode()) {
		case CONFIGURE:
			initConfigure();
			break;
		case DEFAULT:
			initDefault();
			break;
		case DEVICE:
			initDevice();
			break;
		case MANUAL:
			initManual();
			break;
		case RECORD:
			initRecord();
			break;
		case REPLAY:
			initReplay();
			break;
		}
	}
	private void initDefault() {
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblTitle = new Label("OpenDMXStudio Control Panel");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		Button btnConfigure = new Button("Configure");
		btnConfigure.setFont(Font.font(FONT_SMALL));
		btnConfigure.setOnAction(event -> {
			OpenDMXStudio.INSTANCE.setMode(InterfaceMode.CONFIGURE);
		});
		root.add(btnConfigure, 0, 1, 2, 1);
		
		Button btnManual = new Button("Manual Control");
		btnManual.setFont(Font.font(FONT_SMALL));
		btnManual.setOnAction(event -> {
			OpenDMXStudio.INSTANCE.setMode(InterfaceMode.MANUAL);
		});
		root.add(btnManual, 0, 2, 2, 1);
		
		Button btnReplayCue = new Button("Replay Cues");
		btnReplayCue.setFont(Font.font(FONT_SMALL));
		btnReplayCue.setOnAction(event -> {
			OpenDMXStudio.INSTANCE.setMode(InterfaceMode.REPLAY);
		});
		root.add(btnReplayCue, 0, 3, 2, 1);
		
		Button btnRecordCue = new Button("Record Cue");
		btnRecordCue.setFont(Font.font(FONT_SMALL));
		btnRecordCue.setOnAction(event -> {
			OpenDMXStudio.INSTANCE.setMode(InterfaceMode.RECORD);
		});
		root.add(btnRecordCue, 0, 4, 2, 1);
		
		Scene s = new Scene(root, 600, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void initDevice() {
		StageElement elem = OpenDMXStudio.INSTANCE.getCurrentStage().getElements().get(elemIndex);
		
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblTitle = new Label("OpenDMXStudio Control Panel");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		Label lblName = new Label("Name: ");
		lblName.setFont(Font.font(FONT_SMALL));
		root.add(lblName, 0, 1);
		
		TextField txtName = new TextField();
		root.add(txtName, 1, 1);
		
		Label lblType = new Label("Type: ");
		lblType.setFont(Font.font(FONT_SMALL));
		root.add(lblType, 0, 2);
		
		ComboBox<StageElementType> cbxType = new ComboBox<StageElementType>();
		cbxType.getItems().addAll(LIGHT,SMOKE_MACHINE,OTHER);
		root.add(cbxType, 1, 2);
		
		Label lblSize = new Label("Radius: ");
		lblSize.setFont(Font.font(FONT_SMALL));
		root.add(lblSize, 0, 3);
		
		Slider sldSize = new Slider(1, 100, 25);
		root.add(sldSize, 1, 3);
		
		Label lblColour = new Label("Colour: ");
		lblColour.setFont(Font.font(FONT_SMALL));
		root.add(lblColour, 0, 4);
		
		ColorPicker pcrColour = new ColorPicker(Color.BLACK);
		root.add(pcrColour, 1, 4);
		
		Label lblChannel = new Label("Channel: ");
		lblChannel.setFont(Font.font(FONT_SMALL));
		root.add(lblChannel, 0, 5);
		
		ComboBox<ChannelWrapper> cbxChannels = new ComboBox<ChannelWrapper>();
		List<ChannelWrapper> wrappers = new ArrayList<ChannelWrapper>();
		for(Channel c : elem.getChannels()) {
			wrappers.add(new ChannelWrapper(c));
		}
		wrappers.add(new ChannelWrapper());
		cbxChannels.getItems().addAll(wrappers);
		cbxChannels.getSelectionModel().clearAndSelect(0);
		root.add(cbxChannels, 1, 5);
		
		// Channel stuff here
		Label lblChnlType = new Label("Channel Type: ");
		lblChnlType.setFont(Font.font(FONT_SMALL));
		root.add(lblChnlType, 0, 7);
		
		ComboBox<ChannelType> cbxChnlType = new ComboBox<ChannelType>();
		cbxChnlType.getItems().addAll(ChannelType.LIGHT_MASTER,ChannelType.LIGHT_RED,ChannelType.LIGHT_GREEN,ChannelType.LIGHT_BLUE,ChannelType.LIGHT_EFFECT);
		root.add(cbxChnlType, 1, 7);
		
		if(elem != null) {
			txtName.setText(elem.getName());
			switch(elem.getType()) {
			case LIGHT:
				cbxType.getSelectionModel().clearAndSelect(0);
				break;
			case SMOKE_MACHINE:
				cbxType.getSelectionModel().clearAndSelect(1);
				break;
			case OTHER:
				cbxType.getSelectionModel().clearAndSelect(2);
				break;
			}
			sldSize.setValue(elem.getRadius());
			pcrColour.setValue(elem.getColor());
		}
		
		cbxChannels.setOnAction(event -> {
			// Handle setting channel specific stuff
			ChannelWrapper wrapper = cbxChannels.getValue();
			
			ChannelType currType = wrapper.getChannel().getType();
			int typeIndex = 0;
			switch(currType) {
			case LIGHT_MASTER:
				typeIndex = 0;
				break;
			case LIGHT_RED:
				typeIndex = 1;
				break;
			case LIGHT_GREEN:
				typeIndex = 2;
				break;
			case LIGHT_BLUE:
				typeIndex = 3;
				break;
			case LIGHT_EFFECT:
				typeIndex = 4;
				break;
			}
			cbxChnlType.getSelectionModel().clearAndSelect(typeIndex);
		});
		
		Scene s = new Scene(root, 600, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void initConfigure() {
		
	}
	private void initRecord() {
		
	}
	private void initReplay() {
		
	}
	private void initManual() {
		initDefault();
	}
}
