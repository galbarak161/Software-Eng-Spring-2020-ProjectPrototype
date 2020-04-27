package project.Prototype;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private MenuBar menu;

    @FXML
    private Menu help_menu;

    @FXML
    private MenuItem instru_help_menu;

    @FXML
    private Button primaryButton;

    @FXML
    private Label title;

    @FXML
    private Label question_label;

    @FXML
    private ComboBox<?> question_combo;

    @FXML
    private Label course_label;

    @FXML
    private Label study_label;

    @FXML
    private ComboBox<?> course_combo;

    @FXML
    private ComboBox<?> study_combo;

    @FXML
    private TextField question_line;

    @FXML
    private TextField answer_line_1;

    @FXML
    private RadioButton radio_1;

    @FXML
    private TextField answer_line_2;

    @FXML
    private RadioButton radio_2;

    @FXML
    private TextField answer_line_3;

    @FXML
    private RadioButton radio_3;

    @FXML
    private TextField answer_line_4;

    @FXML
    private RadioButton radio_4;

    @FXML
    void openInstructions(ActionEvent event) {
    	
    	try {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Instructions.fxml"));
    		Parent root1 = (Parent) fxmlLoader.load();
    		Stage stage = new Stage();
    		stage.setTitle("Insructions");
    		//stage.getIcons().add(new Image("question_mark.png"));
    		stage.setScene(new Scene(root1));
    		stage.setResizable(false);
    		stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }


    @FXML
    void sendMessageToServer(ActionEvent event) {
    	try {
			ClientMain.sendMessageToServer("Check this message");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


}
