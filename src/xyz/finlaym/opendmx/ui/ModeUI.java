package xyz.finlaym.opendmx.ui;

import static xyz.finlaym.opendmx.ui.UIConstants.FONT_MEDIUM;
import static xyz.finlaym.opendmx.ui.UIConstants.FONT_SMALL;
import static xyz.finlaym.opendmx.ui.UIConstants.GAP;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ModeUI {
	public void start(Stage primaryStage) {
		Stage modeStage = new Stage();
		modeStage.initOwner(primaryStage);
		
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
			System.out.println("Configure mode!");
		});
		root.add(btnConfigure, 0, 1, 2, 1);
		
		Button btnManual = new Button("Manual Control");
		btnManual.setFont(Font.font(FONT_SMALL));
		btnManual.setOnAction(event -> {
			System.out.println("Manual mode!");
		});
		root.add(btnManual, 0, 2, 2, 1);
		
		Button btnReplayCue = new Button("Replay Cues");
		btnReplayCue.setFont(Font.font(FONT_SMALL));
		btnReplayCue.setOnAction(event -> {
			System.out.println("Replay cue mode!");
		});
		root.add(btnReplayCue, 0, 3, 2, 1);
		
		Button btnRecordCue = new Button("Record Cue");
		btnRecordCue.setFont(Font.font(FONT_SMALL));
		btnRecordCue.setOnAction(event -> {
			System.out.println("Record cue mode!");
		});
		root.add(btnRecordCue, 0, 4, 2, 1);
		
		Scene s = new Scene(root, 600, 400);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
}
