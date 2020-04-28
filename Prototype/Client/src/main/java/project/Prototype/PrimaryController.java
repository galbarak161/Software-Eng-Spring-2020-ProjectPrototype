package project.Prototype;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import project.Prototype.DataElements.ClientToServerOpcodes;

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
	private ComboBox<String> question_combo;

	@FXML
	private Label course_label;

	@FXML
	private Label study_label;

	@FXML
	private ComboBox<String> course_combo;

	@FXML
	private ComboBox<String> study_combo;

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
	private TextField subject_text;

	@FXML
	private TextArea question_text;

	ToggleGroup radioGroup;
	
	private static ObservableList<String> dbCollect = null;

	public static void setDbCollect(String[] object) {
		PrimaryController.dbCollect = FXCollections.observableArrayList(object);
		System.out.println("Recived dbStudy from server");
	}

	@FXML
	void openInstructions(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Instructions.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Insructions");
			stage.getIcons().add(new Image(App.class.getResource("help_icon.jpeg").toExternalForm()));
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

	/**
	 * getDataFromServer(DataElements) The function calls the
	 * ClientMain.sendMessageToServer(Object) function
	 * 
	 * @param DataElements with opcode and data
	 * @return -1 for fail
	 */
	private int sendRequestForDataFromServer(DataElements de) {
		int status = -1;
		try {
			status = ClientMain.sendMessageToServer(de);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}

	public void initialize() {
		// Send message to server
		ObservableList<String> val = GetDataFromDB(ClientToServerOpcodes.GetAllStudies,null);
		if (val == null) return;
		study_combo.setItems(val);
		radioGroup = new ToggleGroup();
		radio_1.setToggleGroup(radioGroup);
		radio_2.setToggleGroup(radioGroup);
		radio_3.setToggleGroup(radioGroup);
		radio_4.setToggleGroup(radioGroup);
		
		dbCollect = null;
	}

	@FXML
	void onClickedStudy(ActionEvent event) {
		ObservableList<String> val = GetDataFromDB(ClientToServerOpcodes.GetAllCoursesInStudy,study_combo.getValue());
		if (val == null) return;
		course_combo.setItems(val);
		DisableAll();
		course_combo.setDisable(false);
		
		dbCollect = null;
	}

	@FXML
	void onCourseClicked(ActionEvent event) {
		ObservableList<String> val = GetDataFromDB(ClientToServerOpcodes.GetAllQuestionInCourse,course_combo.getValue());
		if (val == null) return;
		question_combo.setItems(val);
		DisableAll();
		course_combo.setDisable(false);
		question_combo.setDisable(false);
		
		dbCollect = null;
	}

	//TODO: After question entity is done, update this func. (parse subject, question, 4 answers and correct answer)
	@FXML
	void onClickedQuestion(ActionEvent event) {
		
		//ObservableList<String> val = GetDataFromDB(ClientToServerOpcodes.GetQuestion, question_combo.getValue());
		
		subject_text.setDisable(false);
		//subject_text.setText(val[0]);

		question_text.setDisable(false);

		answer_line_1.setDisable(false);

		answer_line_2.setDisable(false);

		answer_line_3.setDisable(false);

		answer_line_4.setDisable(false);

		primaryButton.setDisable(false);

		radio_1.setDisable(false);
		radio_2.setDisable(false);
		radio_3.setDisable(false);
		radio_4.setDisable(false);
		
		radio_2.setSelected(true);

	}
	
	void DisableAll() {
		
		course_combo.setDisable(true);
		
		question_combo.setDisable(true);
		
		subject_text.setDisable(true);
		question_text.setDisable(true);

		answer_line_1.setDisable(true);
		answer_line_2.setDisable(true);
		answer_line_3.setDisable(true);
		answer_line_4.setDisable(true);

		radio_1.setDisable(true);
		radio_2.setDisable(true);
		radio_3.setDisable(true);
		radio_4.setDisable(true);
		
		primaryButton.setDisable(true);
		
	}
	
	/**
	 * Creating request from server and getting data back from server
	 * @param op- what type of request do we want (enums)
	 * @param data - relevant data for request (like a name of field of study)
	 * @return
	 */
	ObservableList<String> GetDataFromDB(ClientToServerOpcodes op, Object data) {
		// Send message to server
		DataElements de = new DataElements(op, data);
		if (sendRequestForDataFromServer(de) == -1)
			return null;

		while (dbCollect == null) {
			System.out.print("");
		}
		return dbCollect;
	}
}
