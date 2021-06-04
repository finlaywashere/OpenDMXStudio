package xyz.finlaym.opendmx.ui;

import static xyz.finlaym.opendmx.ui.UIConstants.*;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StageUI {
	private Stage modeStage;
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
		
		Label lblTitle = new Label("OpenDMXStudio Stage Manager");
		lblTitle.setFont(Font.font(FONT_MEDIUM));
		root.add(lblTitle, 0, 0);
		
		
		
		Scene s = new Scene(root, 300, 200);
		modeStage.setScene(s);
		modeStage.setTitle("Control Panel");
		modeStage.show();
	}
}
