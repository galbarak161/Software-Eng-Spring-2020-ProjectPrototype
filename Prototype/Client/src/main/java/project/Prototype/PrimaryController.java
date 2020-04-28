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
import project.CloneEntities.Question;
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

	private static ObservableList<Question> dbQuestion = null;

	public static void setDbCollect(String[] object) {
		PrimaryController.dbCollect = FXCollections.observableArrayList(object);
		System.out.println("Recived dbStudy from server\n");
	}

	public static void setDbQuestion(Question[] object) {
		PrimaryController.dbQuestion = FXCollections.observableArrayList(object);
		System.out.println("Recived dbQuestions from server\n");
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
		ObservableList<String> val = GetDataFromDB(ClientToServerOpcodes.GetAllStudies, null);
		if (val == null)
			return;
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
		ObservableList<String> val = GetDataFromDB(ClientToServerOpcodes.GetAllCoursesInStudy, study_combo.getValue());
		if (val == null)
			return;
		course_combo.setItems(val);
		DisableAll();
		course_combo.setDisable(false);

		dbCollect = null;
	}

	@FXML
	void onCourseClicked(ActionEvent event) {
		ObservableList<Question> val = GetDataFromDBQuestion(ClientToServerOpcodes.GetAllQuestionInCourse,
				course_combo.getValue());
		if (val == null)
			return;
		ObservableList<String> subjects = null;
		for (Question q : val) {
			subjects.add(q.getSubject() + " - " + Integer.toString(q.getId()));
		}
		question_combo.setItems(subjects);
		DisableAll();
		course_combo.setDisable(false);
		question_combo.setDisable(false);

		dbCollect = null;
	}

	// TODO: After question entity is done, update this func. (parse subject,
	// question, 4 answers and correct answer)
	@FXML
	void onClickedQuestion(ActionEvent event) {

		String[] tokens = question_combo.getValue().split(" - ");
		String CurrentSubject = tokens[0];
		String CurrentID = tokens[1];
		Question CurrentQuestion = null;

		for (Question q : dbQuestion) {
			if (CurrentSubject.compareTo(q.getSubject()) == 0
					&& CurrentID.compareTo(Integer.toString(q.getId())) == 0) {
				CurrentQuestion = q;
				break;
			}
		}

		subject_text.setDisable(false);
		subject_text.setText(CurrentQuestion.getSubject());

		question_text.setDisable(false);
		question_text.setText(CurrentQuestion.getQuestionText());

		answer_line_1.setDisable(false);
		answer_line_1.setText(CurrentQuestion.getAnswer_1());

		answer_line_2.setDisable(false);
		answer_line_2.setText(CurrentQuestion.getAnswer_2());

		answer_line_3.setDisable(false);
		answer_line_3.setText(CurrentQuestion.getAnswer_3());

		answer_line_4.setDisable(false);
		answer_line_4.setText(CurrentQuestion.getAnswer_4());

		primaryButton.setDisable(false);

		radio_1.setDisable(false);
		radio_2.setDisable(false);
		radio_3.setDisable(false);
		radio_4.setDisable(false);

		switch (CurrentQuestion.getCorrectAnswer()) {
		case 1:
			radio_1.setSelected(true);
		case 2:
			radio_2.setSelected(true);
		case 3:
			radio_3.setSelected(true);
		case 4:
			radio_4.setSelected(true);
		}

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
	 * 
	 * @param op-  what type of request do we want (enums)
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

	ObservableList<Question> GetDataFromDBQuestion(ClientToServerOpcodes op, Object data) {
		// Send message to server
		DataElements de = new DataElements(op, data);
		if (sendRequestForDataFromServer(de) == -1)
			return null;

		while (dbQuestion == null) {
			System.out.print("");
		}
		return dbQuestion;
	}

}
