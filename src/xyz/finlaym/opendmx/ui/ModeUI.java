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

package xyz.finlaym.opendmx.ui;

import static xyz.finlaym.opendmx.stage.StageElementType.LIGHT;
import static xyz.finlaym.opendmx.stage.StageElementType.OTHER;
import static xyz.finlaym.opendmx.stage.StageElementType.SMOKE_MACHINE;
import static xyz.finlaym.opendmx.ui.UIConstants.FONT_MEDIUM;
import static xyz.finlaym.opendmx.ui.UIConstants.FONT_SMALL;
import static xyz.finlaym.opendmx.ui.UIConstants.GAP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import xyz.finlaym.opendmx.InterfaceMode;
import xyz.finlaym.opendmx.OpenDMXStudio;
import xyz.finlaym.opendmx.Utils;
import xyz.finlaym.opendmx.command.SendCommand;
import xyz.finlaym.opendmx.cue.CueContainer;
import xyz.finlaym.opendmx.cue.CueEntry;
import xyz.finlaym.opendmx.cue.CueLoader;
import xyz.finlaym.opendmx.cue.CueSet;
import xyz.finlaym.opendmx.cue.CueTransitionType;
import xyz.finlaym.opendmx.stage.Channel;
import xyz.finlaym.opendmx.stage.ChannelType;
import xyz.finlaym.opendmx.stage.StageContainer;
import xyz.finlaym.opendmx.stage.StageElement;
import xyz.finlaym.opendmx.stage.StageElementType;
import xyz.finlaym.opendmx.stage.StageLoader;
import xyz.finlaym.opendmx.submaster.SubMaster;
import xyz.finlaym.opendmx.submaster.SubMasterEntry;
import xyz.finlaym.opendmx.submaster.SubMasterEntryType;
import xyz.finlaym.opendmx.submaster.SubMasterLoader;
import xyz.finlaym.opendmx.submaster.SubMasterSet;
import xyz.finlaym.opendmx.submaster.SubMasterType;

public class ModeUI {
	private Stage modeStage;
	private int elemIndex = -1;
	private OpenDMXStudio dmxStudio;
	private boolean submaster = false;
	
	public ModeUI(OpenDMXStudio dmxStudio) {
		this.dmxStudio = dmxStudio;
	}
	public void start(Stage primaryStage) {
		modeStage = new Stage();
		modeStage.initOwner(primaryStage);
		initDefault();
	}
	public void configure(int elemIndex) {
		this.elemIndex = elemIndex;
		this.submaster = false;
		dmxStudio.setMode(InterfaceMode.DEVICE);
	}
	public void control(int elemIndex) {
		this.elemIndex = elemIndex;
		this.submaster = false;
		dmxStudio.setMode(InterfaceMode.MANUAL);
	}
	public void reset() {
		dmxStudio.setMode(InterfaceMode.DEFAULT);
		this.elemIndex = -1;
		this.submaster = false;
		update();
	}
	public void update() {
		switch(dmxStudio.getMode()) {
		case DEFAULT:
			initDefault();
			break;
		case DEVICE:
			if(submaster && elemIndex == -1)
				initSubmasterConfiguration();
			else if(submaster)
				initSubmasterEdit();
			else if(elemIndex != -1)
				initDevice();
			else
				initStageConfiguration();
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
			dmxStudio.setMode(InterfaceMode.DEVICE);
			configure(-1);
		});
		root.add(btnConfigure, 0, 1, 2, 1);
		
		Button btnManual = new Button("Manual Control");
		btnManual.setFont(Font.font(FONT_SMALL));
		btnManual.setOnAction(event -> {
			dmxStudio.setMode(InterfaceMode.MANUAL);
		});
		root.add(btnManual, 0, 2, 2, 1);
		
		Button btnReplayCue = new Button("Replay Cues");
		btnReplayCue.setFont(Font.font(FONT_SMALL));
		btnReplayCue.setOnAction(event -> {
			dmxStudio.setMode(InterfaceMode.REPLAY);
		});
		root.add(btnReplayCue, 0, 3, 2, 1);
		
		Button btnRecordCue = new Button("Record Cue");
		btnRecordCue.setFont(Font.font(FONT_SMALL));
		btnRecordCue.setOnAction(event -> {
			dmxStudio.setMode(InterfaceMode.RECORD);
		});
		root.add(btnRecordCue, 0, 4, 2, 1);
		
		Button btnExit = new Button("Exit");
		btnExit.setOnAction(event -> {
			System.exit(0);
		});
		root.add(btnExit, 0, 6, 2, 1);
		
		Scene s = new Scene(root, 600, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void initSubmasterEdit() {
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		SubMaster master = dmxStudio.getMasters().getMasters().get(elemIndex);
		
		Label lblTitle = new Label("OpenDMXStudio Control Panel");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		GridPane buttons = new GridPane();
		buttons.setHgap(GAP);
		buttons.setVgap(GAP);
		buttons.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblName = new Label("Name: ");
		lblName.setFont(Font.font(FONT_SMALL));
		buttons.add(lblName, 0, 1);
		
		TextField txtName = new TextField(master.getName());
		buttons.add(txtName, 1, 1);
		
		ListView<SubMasterEntry> entries = new ListView<SubMasterEntry>();
		entries.getItems().addAll(master.getChannels());
		root.add(entries, 0, 1);
		
		Label lblFixture = new Label("Fixture: ");
		lblFixture.setFont(Font.font(FONT_SMALL));
		buttons.add(lblFixture, 0, 3);
		
		ComboBox<StageElement> elements = new ComboBox<StageElement>();
		elements.getItems().addAll(dmxStudio.getCurrentStage().getElements());
		buttons.add(elements, 1, 3);
		
		Label lblChannel = new Label("Channel: ");
		lblChannel.setFont(Font.font(FONT_SMALL));
		buttons.add(lblChannel, 0, 4);
		
		ComboBox<Channel> channels = new ComboBox<Channel>();
		buttons.add(channels, 1, 4);
		channels.setDisable(true);
		
		Label lblStatus = new Label();
		lblStatus.setFont(Font.font(FONT_SMALL));
		buttons.add(lblStatus, 0, 6);
		
		Button btnAdd = new Button("Add");
		buttons.add(btnAdd, 0, 5);
		
		Button btnRem = new Button("Remove");
		buttons.add(btnRem, 1, 5);
		
		Button btnBack = new Button("Back");
		buttons.add(btnBack, 0, 6);
		
		txtName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
				master.setName(newValue);
				dmxStudio.getMasters().getMasters().set(elemIndex, master);
			}
		});
		btnRem.setOnAction(event -> {
			if(entries.getSelectionModel().isEmpty())
				return;
			int index = entries.getSelectionModel().getSelectedIndex();
			entries.getItems().remove(index);
			master.getChannels().remove(index);
			dmxStudio.getMasters().getMasters().set(elemIndex, master);
		});
		btnAdd.setOnAction(event -> {
			SubMasterEntry entry = null;
			if(channels.getSelectionModel().isEmpty()) {
				// Only fixture is selected
				if(elements.getSelectionModel().isEmpty()) {
					entry = null;
				}else {
					entry = new SubMasterEntry(SubMasterEntryType.DEVICE, elements.getValue().getId(),dmxStudio);
				}
			}else {
				// Fixture and channel are selected
				entry = new SubMasterEntry(SubMasterEntryType.CHANNEL, channels.getValue().getId(),dmxStudio);
			}
			if(entry == null) {
				lblStatus.setText("Fixture and or channel must be selected");
				return;
			}
			master.getChannels().add(entry);
			dmxStudio.getMasters().getMasters().set(elemIndex, master);
			entries.getItems().add(entry);
		});
		
		elements.valueProperty().addListener(new ChangeListener<StageElement>() {
			@Override
			public void changed(ObservableValue<? extends StageElement> arg0, StageElement oldValue, StageElement newValue) {
				channels.getItems().clear();
				channels.getItems().addAll(newValue.getChannels());
				channels.setDisable(false);
			}
		});
		
		btnBack.setOnAction(event -> {
			elemIndex = -1;
			update();
		});
		
		root.add(buttons, 1, 1);
		
		Scene s = new Scene(root, 800, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void initSubmasterConfiguration() {
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		GridPane buttons = new GridPane();
		buttons.setHgap(GAP);
		buttons.setVgap(GAP);
		buttons.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblTitle = new Label("OpenDMXStudio Control Panel");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		ListView<SubMaster> masters = new ListView<SubMaster>();
		masters.getItems().addAll(dmxStudio.getMasters().getMasters());
		root.add(masters, 0, 1);
		
		Button btnSave = new Button("Save Submasters");
		buttons.add(btnSave, 1, 0);
		
		Button btnEdit = new Button("Edit Selected Submaster");
		buttons.add(btnEdit, 0, 0);
		
		Button btnUp = new Button("Move Up");
		buttons.add(btnUp, 0, 2);
		
		Button btnDown = new Button("Move Down");
		buttons.add(btnDown, 1, 2);
		
		Label lblName = new Label("Name: ");
		lblName.setFont(Font.font(FONT_SMALL));
		buttons.add(lblName, 0, 4);
		
		TextField txtName = new TextField();
		buttons.add(txtName, 1, 4);
		
		Label lblType = new Label("Submaster Type: ");
		lblType.setFont(Font.font(FONT_SMALL));
		buttons.add(lblType, 0, 6);
		
		ComboBox<SubMasterType> cbxMasters = new ComboBox<SubMasterType>();
		cbxMasters.getItems().addAll(SubMasterType.MASTER,SubMasterType.SUBMASTER);
		cbxMasters.getSelectionModel().clearAndSelect(1);
		buttons.add(cbxMasters, 1, 6);
		
		Button btnAdd = new Button("Add");
		buttons.add(btnAdd, 0, 7);
		
		Button btnRemove = new Button("Remove");
		buttons.add(btnRemove, 1, 7);
		
		Button btnBack = new Button("Back");
		buttons.add(btnBack, 0, 8);
		
		Label lblStatus = new Label();
		buttons.add(lblStatus, 1, 8);
		
		root.add(buttons, 1, 1);
		
		btnBack.setOnAction(event -> {
			submaster = false;
			update();
		});
		btnEdit.setOnAction(event -> {
			if(masters.getSelectionModel().isEmpty())
				return;
			int index = masters.getSelectionModel().getSelectedIndex();
			elemIndex = index;
			update();
		});
		btnAdd.setOnAction(event -> {
			String name = txtName.getText();
			if(name.trim().equals(""))
				name = "Untitled Submaster";
			txtName.setText("");
			int id = 0;
			
			for(SubMaster m : masters.getItems()) {
				if(m.getId() > id)
					id = m.getId();
			}
			id++;
			
			SubMaster e = new SubMaster(id, name, cbxMasters.getValue());
			masters.getItems().add(e);
			SubMasterSet set = dmxStudio.getMasters();
			set.getMasters().clear();
			set.getMasters().addAll(masters.getItems());
			dmxStudio.setMasters(set);
		});
		btnRemove.setOnAction(event -> {
			if(masters.getSelectionModel().isEmpty())
				return;
			masters.getItems().remove(masters.getSelectionModel().getSelectedIndex());
		});
		btnUp.setOnAction(event -> {
			int index = masters.getSelectionModel().getSelectedIndex();
			if(index == 0)
				return;
			SubMaster tmp = masters.getItems().get(index-1);
			masters.getItems().set(index-1, masters.getItems().get(index));
			masters.getItems().set(index, tmp);
			masters.getSelectionModel().clearAndSelect(index-1);
		});
		btnDown.setOnAction(event -> {
			int index = masters.getSelectionModel().getSelectedIndex();
			if(index == masters.getItems().size()-1)
				return;
			SubMaster tmp = masters.getItems().get(index+1);
			masters.getItems().set(index+1, masters.getItems().get(index));
			masters.getItems().set(index, tmp);
			masters.getSelectionModel().clearAndSelect(index+1);
		});
		btnSave.setOnAction(event -> {
			// Save
			SubMasterSet set = dmxStudio.getMasters();
			set.getMasters().clear();
			set.getMasters().addAll(masters.getItems());
			dmxStudio.setMasters(set);
			try {
				SubMasterLoader.saveSubMasters(dmxStudio.getCurrentStage(), dmxStudio.getMasters());
			} catch (Exception e) {
				e.printStackTrace();
				lblStatus.setText("Error occured!");
			}
			lblStatus.setText("Successfully saved submasters!");
		});
		
		Scene s = new Scene(root, 800, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void initStageConfiguration() {
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblTitle = new Label("OpenDMXStudio Control Panel");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		Button btnLoad = new Button("Load Stage");
		root.add(btnLoad, 0, 1);
		
		Button btnSave = new Button("Save Stage");
		root.add(btnSave, 1, 1);
		
		Button btnAddDevice = new Button("Add DMX512 Device");
		root.add(btnAddDevice, 0, 3);
		
		Button btnSetBackground = new Button("Set Background");
		root.add(btnSetBackground, 0, 4);
		
		Button btnConfigureSubmasters = new Button("Configure Submasters");
		root.add(btnConfigureSubmasters, 0, 5);
		
		Button btnBack = new Button("Back");
		root.add(btnBack, 0, 7);
		
		Label lblStatus = new Label();
		lblStatus.setFont(Font.font(FONT_SMALL));
		root.add(lblStatus, 0, 8);
		
		btnConfigureSubmasters.setOnAction(event -> {
			submaster = true;
			update();
		});
		
		btnLoad.setOnAction(event -> {
			DirectoryChooser dChooser = new DirectoryChooser();
			dChooser.setTitle("Load Stage");
			File stageDir = dChooser.showDialog(modeStage);
			
			try {
				StageContainer stage = StageLoader.loadStage(stageDir);
				dmxStudio.setCurrentStage(stage);
				update();
			} catch (Exception e) {
				e.printStackTrace();
				lblStatus.setText("Error loading stage!");
			}
		});
		btnSave.setOnAction(event -> {
			try {
				StageContainer stage = dmxStudio.getCurrentStage();
				StageLoader.saveStage(stage.getStageDir(), stage);
			}catch(Exception e) {
				e.printStackTrace();
				lblStatus.setText("Error saving stage!");
			}
		});
		btnAddDevice.setOnAction(event -> {
			StageContainer stage = dmxStudio.getCurrentStage();
			int maxId = Integer.MIN_VALUE;
			for(StageElement elem : stage.getElements()) {
				if(elem.getId() > maxId)
					maxId = elem.getId();
			}
			if(maxId < 0)
				maxId = 0;
			stage.getElements().add(new StageElement(.5d, .5d, StageElementType.OTHER, "New Stage Element", new Channel[0], 30, Color.RED, maxId+1));
		});
		btnSetBackground.setOnAction(event -> {
			FileChooser fChooser = new FileChooser();
			fChooser.setTitle("Open Background");
			File background = fChooser.showOpenDialog(modeStage);
			if(background == null)
				return;
			try {
				dmxStudio.getCurrentStage().setBackground(new Image(new FileInputStream(background)));
			} catch (Exception e) {
				e.printStackTrace();
				lblStatus.setText("Error loading background!");
			}
		});
		btnBack.setOnAction(event -> {
			reset();
		});
		
		Scene s = new Scene(root, 600, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void initDevice() {
		StageElement elem = dmxStudio.getCurrentStage().getElements().get(elemIndex);
		
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
		wrappers.add(new ChannelWrapper("Select channel!"));
		for(int i = 0; i < elem.getChannels().length; i++) {
			Channel c = elem.getChannels()[i];
			wrappers.add(new ChannelWrapper(c,i+1));
		}
		wrappers.add(new ChannelWrapper("Add channel"));
		cbxChannels.getItems().addAll(wrappers);
		cbxChannels.getSelectionModel().clearAndSelect(0);
		root.add(cbxChannels, 1, 5);
		
		// Channel stuff here
		Label lblChnlType = new Label("Channel Type: ");
		lblChnlType.setFont(Font.font(FONT_SMALL));
		root.add(lblChnlType, 0, 7);
		
		ComboBox<ChannelType> cbxChnlType = new ComboBox<ChannelType>();
		cbxChnlType.getItems().addAll(ChannelType.LIGHT_MASTER,ChannelType.LIGHT_RED,ChannelType.LIGHT_GREEN,ChannelType.LIGHT_BLUE,ChannelType.LIGHT_EFFECT,ChannelType.LIGHT_OTHER);
		root.add(cbxChnlType, 1, 7);
		
		Label lblUniverse = new Label("Channel Universe: ");
		lblUniverse.setFont(Font.font(FONT_SMALL));
		root.add(lblUniverse, 0, 8);
		
		TextField txtUniverse = new TextField();
		root.add(txtUniverse, 1, 8);
		
		Label lblChannelNum = new Label("Channel Number: ");
		lblChannelNum.setFont(Font.font(FONT_SMALL));
		root.add(lblChannelNum, 0, 9);
		
		TextField txtChannelNum = new TextField();
		root.add(txtChannelNum, 1, 9);
		
		Button btnSave = new Button("Save");
		root.add(btnSave, 1, 12);
		
		Button btnBack = new Button("Back");
		root.add(btnBack, 0, 12);
		
		Label lblStatus = new Label();
		lblStatus.setFont(Font.font(FONT_SMALL));
		root.add(lblStatus, 0, 13);
		
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
			
			if(wrapper.getChannel() == null) {
				cbxChnlType.getSelectionModel().clearAndSelect(5);
				if(cbxChannels.getSelectionModel().getSelectedIndex() == 0)
					return;
				
				Channel[] newChannels = new Channel[elem.getChannels().length+1];
				for(int i = 0; i < elem.getChannels().length; i++) {
					newChannels[i] = elem.getChannels()[i];
				}
				int maxId = Integer.MIN_VALUE;
				for(StageElement e : dmxStudio.getCurrentStage().getElements()) {
					for(Channel c : e.getChannels()) {
						if(c.getId() > maxId)
							maxId = c.getId();
					}
				}
				if(maxId < 0)
					maxId = 0;
				Channel c = new Channel(0, 0, ChannelType.LIGHT_OTHER,maxId+1);
				newChannels[elem.getChannels().length] = c;
				
				cbxChannels.getItems().remove(cbxChannels.getItems().size()-1); // Remove add channel box
				cbxChannels.getItems().add(new ChannelWrapper(c,elem.getChannels().length+1));
				cbxChannels.getItems().add(new ChannelWrapper("Add channel"));
				cbxChannels.getSelectionModel().clearAndSelect(newChannels.length);
				
				elem.setChannels(newChannels);
				return;
			}
			
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
			case LIGHT_OTHER:
				typeIndex = 5;
				break;
			}
			cbxChnlType.getSelectionModel().clearAndSelect(typeIndex);
			txtUniverse.setText(String.valueOf(wrapper.getChannel().getUniverse()));
			txtChannelNum.setText(String.valueOf(wrapper.getChannel().getChannel()));
		});
		
		btnSave.setOnAction(event -> {
			// Handle save
			ChannelWrapper wrapper = cbxChannels.getSelectionModel().getSelectedItem();
			int index = cbxChannels.getSelectionModel().getSelectedIndex()-1;
			
			if(wrapper.getChannel() != null) {
				if(!Utils.isInt(txtUniverse.getText()) || !Utils.isInt(txtChannelNum.getText())) {
					lblStatus.setText("Expected number but got letter!");
					return;
				}
				
				Channel c = wrapper.getChannel();
				
				c.setType(cbxChnlType.getValue());
				c.setUniverse(Integer.valueOf(txtUniverse.getText()));
				c.setChannel(Integer.valueOf(txtChannelNum.getText()));
				
				elem.getChannels()[index] = c;
			}
			
			elem.setName(txtName.getText());
			elem.setType(cbxType.getValue());
			elem.setRadius((int) sldSize.getValue());
			elem.setColor(pcrColour.getValue());
			
			StageContainer currStage = dmxStudio.getCurrentStage();
			
			currStage.getElements().set(elemIndex, elem);
			
			try {
				StageLoader.saveStage(currStage.getStageDir(), currStage);
			} catch (Exception e) {
				e.printStackTrace();
				lblStatus.setText("Error occured!");
				return;
			}
			
			lblStatus.setText("Successfully saved!");
		});
		btnBack.setOnAction(event -> {
			elemIndex = -1;
			update();
		});
		
		Scene s = new Scene(root, 600, 600);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void initRecord() {
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		GridPane buttons = new GridPane();
		buttons.setHgap(GAP);
		buttons.setVgap(GAP);
		buttons.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblTitle = new Label("OpenDMXStudio Control Panel");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		ListView<CueContainer> cues = new ListView<CueContainer>();
		cues.getItems().addAll(dmxStudio.getCurrCue().getCues());
		root.add(cues, 0, 1);
		
		Button btnLoad = new Button("Load Cues");
		buttons.add(btnLoad, 0, 0);
		
		Button btnSave = new Button("Save Cues");
		buttons.add(btnSave, 1, 0);
		
		Button btnUp = new Button("Move Up");
		buttons.add(btnUp, 0, 2);
		
		Button btnDown = new Button("Move Down");
		buttons.add(btnDown, 1, 2);
		
		Label lblName = new Label("Name: ");
		lblName.setFont(Font.font(FONT_SMALL));
		buttons.add(lblName, 0, 4);
		
		TextField txtName = new TextField();
		buttons.add(txtName, 1, 4);
		
		Label lblTime = new Label("Transition Time: ");
		lblTime.setFont(Font.font(FONT_SMALL));
		buttons.add(lblTime, 0, 5);
		
		Slider sldTime = new Slider(0, 30, 5);
		buttons.add(sldTime, 1, 5);
		
		Label lblType = new Label("Transition Type: ");
		lblType.setFont(Font.font(FONT_SMALL));
		buttons.add(lblType, 0, 6);
		
		ComboBox<CueTransitionType> cbxTypes = new ComboBox<CueTransitionType>();
		cbxTypes.getItems().add(CueTransitionType.CROSSFADE);
		buttons.add(cbxTypes, 1, 6);
		
		Button btnAdd = new Button("Add");
		buttons.add(btnAdd, 0, 7);
		
		Button btnRemove = new Button("Remove");
		buttons.add(btnRemove, 1, 7);
		
		Button btnBack = new Button("Back");
		buttons.add(btnBack, 0, 8);
		
		Label lblStatus = new Label();
		buttons.add(lblStatus, 1, 8);
		
		root.add(buttons, 1, 1);
		
		btnBack.setOnAction(event -> {
			reset();
		});
		
		btnAdd.setOnAction(event -> {
			String name = txtName.getText();
			if(name.trim().equals(""))
				name = "Untitled Cue";
			txtName.setText("");
			CueContainer cue = new CueContainer(name);
			
			for(StageElement elem : dmxStudio.getCurrentStage().getElements()) {
				for(Channel c : elem.getChannels()) {
					int id = c.getId();
					int oldValue = 0;
					// Find the previous value of the channel, if there is none then it defaults to 0
					if(cues.getItems().size() > 0) {
						for(CueEntry entry : cues.getItems().get(cues.getItems().size()-1).getEntries()) {
							int id2 = entry.getNewValue().getId();
							if(id == id2) {
								// Found the old value of the channel
								oldValue = entry.getNewValue().getCurrValRaw();
								break;
							}
						}
					}
					Channel c1 = new Channel(c.getUniverse(), c.getChannel(), c.getType(), c.getId());
					c1.setCurrValRaw(oldValue);
					Channel c2 = new Channel(c.getUniverse(), c.getChannel(), c.getType(), c.getId());
					c2.setCurrValRaw(c.getCurrValRaw());
					CueEntry entry = new CueEntry(c2, c1, cbxTypes.getSelectionModel().getSelectedItem(),sldTime.getValue());
					cue.getEntries().add(entry);
				}
			}
			
			cues.getItems().add(cue);
			CueSet set = dmxStudio.getCurrCue();
			set.getCues().clear();
			set.getCues().addAll(cues.getItems());
			dmxStudio.setCurrCue(set);
		});
		btnRemove.setOnAction(event -> {
			if(cues.getSelectionModel().isEmpty())
				return;
			cues.getItems().remove(cues.getSelectionModel().getSelectedIndex());
		});
		btnUp.setOnAction(event -> {
			int index = cues.getSelectionModel().getSelectedIndex();
			if(index == 0)
				return;
			CueContainer tmp = cues.getItems().get(index-1);
			cues.getItems().set(index-1, cues.getItems().get(index));
			cues.getItems().set(index, tmp);
			cues.getSelectionModel().clearAndSelect(index-1);
		});
		btnDown.setOnAction(event -> {
			int index = cues.getSelectionModel().getSelectedIndex();
			if(index == cues.getItems().size()-1)
				return;
			CueContainer tmp = cues.getItems().get(index+1);
			cues.getItems().set(index+1, cues.getItems().get(index));
			cues.getItems().set(index, tmp);
			cues.getSelectionModel().clearAndSelect(index+1);
		});
		btnLoad.setOnAction(event -> {
			FileChooser fChooser = new FileChooser();
			fChooser.setTitle("Load Cue");
			File cueDir = new File(dmxStudio.getCurrentStage().getStageDir(),"cues/");
			if(!cueDir.exists())
				cueDir.mkdirs();
			fChooser.setInitialDirectory(cueDir);
			File f = fChooser.showOpenDialog(modeStage);
			if(f == null)
				return;
			try {
				CueSet set = CueLoader.loadCue(dmxStudio.getCurrentStage(), f);
				dmxStudio.setCurrCue(set);
				cues.getItems().clear();
				cues.getItems().addAll(set.getCues());
			} catch (Exception e) {
				e.printStackTrace();
				lblStatus.setText("Error occured!");
			}
			lblStatus.setText("Successfully loaded cues!");
		});
		btnSave.setOnAction(event -> {
			CueSet set = dmxStudio.getCurrCue();
			set.getCues().clear();
			set.getCues().addAll(cues.getItems());
			dmxStudio.setCurrCue(set);
			// Save
			FileChooser fChooser = new FileChooser();
			fChooser.setTitle("Save Cue");
			File cueDir = new File(dmxStudio.getCurrentStage().getStageDir(),"cues/");
			if(!cueDir.exists())
				cueDir.mkdirs();
			fChooser.setInitialDirectory(cueDir);
			File f = fChooser.showSaveDialog(modeStage);
			if(f == null)
				return;
			try {
				CueLoader.saveCue(dmxStudio.getCurrentStage(),dmxStudio.getCurrCue(),f);
			} catch (Exception e) {
				e.printStackTrace();
				lblStatus.setText("Error occured!");
			}
			lblStatus.setText("Successfully saved cues!");
		});
		
		Scene s = new Scene(root, 800, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void initReplay() {
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		GridPane buttons = new GridPane();
		buttons.setHgap(GAP);
		buttons.setVgap(GAP);
		buttons.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblTitle = new Label("OpenDMXStudio Control Panel");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		ListView<CueContainer> cues = new ListView<CueContainer>();
		cues.getItems().addAll(dmxStudio.getCurrCue().getCues());
		cues.setSelectionModel(null);
		root.add(cues, 0, 1);
		
		Button btnLoad = new Button("Load Cues");
		buttons.add(btnLoad, 0, 0);
		
		Button btnGo = new Button("Go");
		buttons.add(btnGo, 0, 1);
		
		Button btnBack = new Button("Back");
		buttons.add(btnBack, 0, 5);
		
		Label lblStatus = new Label();
		buttons.add(lblStatus, 1, 5);
		
		root.add(buttons, 1, 1);
		
		btnBack.setOnAction(event -> {
			reset();
		});
		btnGo.setOnAction(event -> {
			try {
				boolean success = dmxStudio.getCurrCue().execute(dmxStudio.getHwInterface());
				if(!success) {
					lblStatus.setText("Reached end of cues!");
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
				lblStatus.setText("Error occured!");
			}
			lblStatus.setText("Changed to next cue!");
		});
		
		btnLoad.setOnAction(event -> {
			FileChooser fChooser = new FileChooser();
			fChooser.setTitle("Load Cue");
			File cueDir = new File(dmxStudio.getCurrentStage().getStageDir(),"cues/");
			if(!cueDir.exists())
				cueDir.mkdirs();
			fChooser.setInitialDirectory(cueDir);
			File f = fChooser.showOpenDialog(modeStage);
			if(f == null)
				return;
			try {
				CueSet set = CueLoader.loadCue(dmxStudio.getCurrentStage(), f);
				dmxStudio.setCurrCue(set);
				cues.getItems().clear();
				cues.getItems().addAll(set.getCues());
			} catch (Exception e) {
				e.printStackTrace();
				lblStatus.setText("Error occured!");
			}
			lblStatus.setText("Successfully loaded cues!");
		});
		
		Scene s = new Scene(root, 700, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
	private void initManual() {
		GridPane root = new GridPane();
		root.setHgap(GAP);
		root.setVgap(GAP);
		root.setPadding(new Insets(GAP,GAP,GAP,GAP));
		
		Label lblTitle = new Label("OpenDMXStudio Control Panel");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		if(elemIndex == -1) {
			
			Label lblUniverse = new Label("Universe: ");
			lblUniverse.setFont(Font.font(FONT_SMALL));
			root.add(lblUniverse, 0, 1);
			
			TextField txtUniverse = new TextField();
			root.add(txtUniverse, 1, 1);
			
			Label lblChannel = new Label("Channel: ");
			lblChannel.setFont(Font.font(FONT_SMALL));
			root.add(lblChannel, 0, 2);
			
			TextField txtChannel = new TextField();
			root.add(txtChannel, 1, 2);
			
			Label lblValue = new Label("Value: ");
			lblValue.setFont(Font.font(FONT_SMALL));
			root.add(lblValue, 0, 3);
			
			Slider sldValue = new Slider(0,255,0);
			root.add(sldValue, 1, 3);
			
			Button btnApply = new Button("Apply");
			root.add(btnApply, 1, 4);
			
			Label lblStatus = new Label();
			lblStatus.setFont(Font.font(FONT_SMALL));
			root.add(lblStatus, 0, 5);
			
			Button btnBack = new Button("Back");
			root.add(btnBack, 0, 4);
			
			GridPane sliders = new GridPane();
			sliders.setHgap(GAP);
			sliders.setVgap(GAP);
			sliders.setPadding(new Insets(GAP,GAP,GAP,GAP));
			
			int i = 0;
			for(SubMaster m : dmxStudio.getMasters().getMasters()) {
				Label lblName = new Label(m.getName());
				lblName.setFont(Font.font(FONT_SMALL));
				sliders.add(lblName, i, 0);
				Slider sldMaster = new Slider(0,255,m.getValue());
				sldMaster.setOrientation(Orientation.VERTICAL);
				final int index = i;
				sldMaster.valueProperty().addListener(new ChangeListener<Number>(){
					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
						try {
							dmxStudio.getMasters().getMasters().get(index).setValue((int)((double) newValue), dmxStudio);
						} catch (IOException e) {
							e.printStackTrace();
							lblStatus.setText("Error setting channel!");
						}
					}
				});
				sliders.add(sldMaster, i, 1);
				i++;
			}
			root.add(sliders, 0, 6);
			btnApply.setOnAction(event -> {
				if(!Utils.isInt(txtUniverse.getText()) || !Utils.isInt(txtChannel.getText())) {
					lblStatus.setText("Expected number but got letter!");
					return;
				}
				int universe = Integer.valueOf(txtUniverse.getText());
				int channel = Integer.valueOf(txtChannel.getText());
				int value = (int) sldValue.getValue();
				SendCommand cmd = new SendCommand(universe, channel, value);
				try {
					dmxStudio.getHwInterface().sendCommand(cmd);
				} catch (IOException e) {
					e.printStackTrace();
					lblStatus.setText("Error occured!");
					return;
				}
				lblStatus.setText("Successfully applied changes!");
			});
			btnBack.setOnAction(event -> {
				reset();
			});
		}else {
			StageElement elem = dmxStudio.getCurrentStage().getElements().get(elemIndex);
			int currRow = 1;
			List<Slider> sliders = new ArrayList<Slider>();
			boolean rgb = false;
			for(Channel c : elem.getChannels()) {
				if(c.getType() == ChannelType.LIGHT_BLUE || c.getType() == ChannelType.LIGHT_GREEN || c.getType() == ChannelType.LIGHT_RED)
					rgb = true;
				Label lblName = new Label(c.getType().toString());
				lblName.setFont(Font.font(FONT_SMALL));
				root.add(lblName, 0, currRow);
				
				Slider sldValue = new Slider(0,255,c.getCurrVal(dmxStudio));
				root.add(sldValue, 1, currRow);
				sliders.add(sldValue);
				
				currRow++;
			}
			
			int currR = 0, currG = 0, currB = 0;
			for(Channel c : elem.getChannels()) {
				if(c.getType() == ChannelType.LIGHT_RED)
					currR = c.getCurrVal(dmxStudio);
				else if(c.getType() == ChannelType.LIGHT_GREEN)
					currG = c.getCurrVal(dmxStudio);
				else if(c.getType() == ChannelType.LIGHT_BLUE)
					currB = c.getCurrVal(dmxStudio);
			}
			Color col = Color.rgb(currR, currG, currB);
			
			final ColorPicker cprRgb = new ColorPicker(col);
			
			if(rgb) {
				Label lblName = new Label("LIGHT_RGB");
				lblName.setFont(Font.font(FONT_SMALL));
				root.add(lblName, 0, currRow);
				
				root.add(cprRgb, 1, currRow);
				currRow++;
			}
			Button btnApply = new Button("Apply Sliders");
			root.add(btnApply, 1, currRow);
			
			Button btnColour = new Button("Apply Colour");
			btnColour.setDisable(true);
			root.add(btnColour, 0, currRow);
			
			if(rgb)
				btnColour.setDisable(false);
			
			Label lblStatus = new Label();
			lblStatus.setFont(Font.font(FONT_SMALL));
			root.add(lblStatus, 0, currRow+1);
			
			Button btnBack = new Button("Back");
			root.add(btnBack, 0, currRow+2);
			
			btnBack.setOnAction(event -> {
				control(-1);
			});
			btnColour.setOnAction(event -> {
				Color c = cprRgb.getValue();
				for(int i = 0; i < sliders.size(); i++) {
					Channel chn = elem.getChannels()[i];
					Slider sld = sliders.get(i);
					int value = -1;
					if(chn.getType() == ChannelType.LIGHT_RED) {
						sld.setValue(c.getRed()*255);
						value = (int) (c.getRed()*255);
					}else if(chn.getType() == ChannelType.LIGHT_GREEN) {
						sld.setValue(c.getGreen()*255);
						value = (int) (c.getGreen()*255);
					}else if(chn.getType() == ChannelType.LIGHT_BLUE) {
						sld.setValue(c.getBlue()*255);
						value = (int) (c.getBlue()*255);
					}else if(chn.getType() == ChannelType.LIGHT_MASTER && sld.getValue() == 0) {
						sld.setValue(255);
						value = 255;
					}
					if(value != -1) {
						chn.setCurrVal(value,dmxStudio);
						SendCommand cmd = new SendCommand(chn.getUniverse(), chn.getChannel(), value);
						try {
							dmxStudio.getHwInterface().sendCommand(cmd);
						} catch (IOException e) {
							e.printStackTrace();
							lblStatus.setText("Error occured!");
							return;
						}
					}
				}
				lblStatus.setText("Successfully applied changes!");
			});
			btnApply.setOnAction(event -> {
				int r= 0, g = 0, b = 0;
				for(int i = 0; i < sliders.size(); i++) {
					int value = (int) sliders.get(i).getValue();
					Channel c = elem.getChannels()[i];
					
					if(c.getType() == ChannelType.LIGHT_RED)
						r = value;
					else if(c.getType() == ChannelType.LIGHT_GREEN)
						g = value;
					else if(c.getType() == ChannelType.LIGHT_BLUE)
						b = value;
					
					c.setCurrVal(value,dmxStudio);
					SendCommand cmd = new SendCommand(c.getUniverse(), c.getChannel(), value);
					try {
						dmxStudio.getHwInterface().sendCommand(cmd);
					} catch (IOException e) {
						e.printStackTrace();
						lblStatus.setText("Error occured!");
						return;
					}
				}
				Color c = Color.rgb(r, g, b);
				cprRgb.setValue(c);
				lblStatus.setText("Successfully applied changes!");
			});
		}
		
		Scene s = new Scene(root, 600, 600);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
}
