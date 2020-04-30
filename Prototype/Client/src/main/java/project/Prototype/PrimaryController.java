package project.Prototype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.CloneEntities.*;
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
	
	@FXML
    private Button submitButton;

	ToggleGroup radioGroup;

	private static ObservableList<String> dbCollect = null;

	private static ObservableList<CloneQuestion> dbQuestion = null;
	
	private CloneQuestion qToServer; 

	public static void setDbCollect(String[] object) {
		PrimaryController.dbCollect = FXCollections.observableArrayList(object);
		System.out.println("Recived data from server\n");
	}

	public static void setDbQuestion(CloneQuestion[] object) {
		PrimaryController.dbQuestion = FXCollections.observableArrayList(object);
		System.out.println("Recived data from server\n");
	}
	
	@FXML
	void countChars(ActionEvent event) {
		System.out.print("Sup Nigga");
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
	void onClickedSubmit(ActionEvent event) throws Exception {
		try {
			CloneQuestion q = new CloneQuestion();
			q.clone(qToServer);
			if (subject_text.getText().isEmpty()) throw new Exception("Subject is empty");
			q.setSubject(subject_text.getText());
			if (question_text.getText().isEmpty()) throw new Exception("Question is empty");
			q.setQuestionText(question_text.getText());
			if (answer_line_1.getText().isEmpty()) throw new Exception("Answer 1 is empty");
			q.setAnswer_1(answer_line_1.getText());
			if (answer_line_2.getText().isEmpty()) throw new Exception("Answer 2 is empty");
			q.setAnswer_2(answer_line_2.getText());
			if (answer_line_3.getText().isEmpty()) throw new Exception("Answer 3 is empty");
			q.setAnswer_3(answer_line_3.getText());
			if (answer_line_4.getText().isEmpty()) throw new Exception("Answer 4 is empty");
			q.setAnswer_4(answer_line_4.getText());
			RadioButton chk = (RadioButton) radioGroup.getSelectedToggle();
			switch (chk.getText()) {
				case "a.":
					q.setCorrectAnswer(1);
					break;
				case "b.":
					q.setCorrectAnswer(2);
					break;
				case "c.":
					q.setCorrectAnswer(3);
					break;
				case "d.":
					q.setCorrectAnswer(4);
					break;
				default:
					throw new Exception("No correct answer");
			}
			sendMessageToServer(q);
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Invalid input");
			alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea("Please fill all the fields")));
			alert.showAndWait();
		}
	}
	
	void sendMessageToServer(Object obj) {
		try {
			ClientMain.sendMessageToServer(obj);
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
		ClearAllFields();
		question_combo.setValue(" ");
		course_combo.setDisable(false);
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

		dbCollect = null;
	}

	@FXML
	void onCourseClicked(ActionEvent event) {
		if (course_combo.getValue() == null || course_combo.isDisable() == true) {
			ClearAllFields();
			course_combo.setDisable(false);
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

			return;
		}
		ObservableList<CloneQuestion> val = GetDataFromDBQuestion(ClientToServerOpcodes.GetAllQuestionInCourse,
				course_combo.getValue());
		if (val == null)
			return;
		List<String> subjects = new ArrayList<String>();
		for (CloneQuestion q : val) {
			subjects.add(q.getSubject() + " - " + Integer.toString(q.getQuestionCode()));
		}
		ObservableList<String> final_subjects = FXCollections.observableArrayList(subjects);
		question_combo.setItems(final_subjects);
		ClearAllFields();
		course_combo.setDisable(false);
		question_combo.setDisable(false);
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


		dbCollect = null;
	}

	// TODO: After question entity is done, update this func. (parse subject,
	// question, 4 answers and correct answer)
	@FXML
	void onClickedQuestion(ActionEvent event) {
		
		if (question_combo.getValue() == null || question_combo.isDisable() == true) {
			course_combo.setDisable(false);
			question_combo.setDisable(false);
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
			return;
		} 
		
		String[] tokens = question_combo.getValue().split(" - ");
		if (tokens.length < 2)
			return;
		String CurrentSubject = tokens[0];
		String CurrentID = tokens[1];
		CloneQuestion CurrentQuestion = null;
		for (CloneQuestion q : dbQuestion) {
			if (CurrentSubject.compareTo(q.getSubject()) == 0 && CurrentID.compareTo(Integer.toString(q.getQuestionCode())) == 0) {
				CurrentQuestion = q;
				break;
			}
		}

		qToServer = CurrentQuestion;
		
		subject_text.setText(CurrentQuestion.getSubject());
		question_text.setText(CurrentQuestion.getQuestionText());
		
		answer_line_1.setText(CurrentQuestion.getAnswer_1());
		answer_line_2.setText(CurrentQuestion.getAnswer_2());
		answer_line_3.setText(CurrentQuestion.getAnswer_3());
		answer_line_4.setText(CurrentQuestion.getAnswer_4());
		
		switch (CurrentQuestion.getCorrectAnswer()) {
		case 1:
			radio_1.setSelected(true);
			break;
		case 2:
			radio_2.setSelected(true);
			break;
		case 3:
			radio_3.setSelected(true);
			break;
		case 4:
			radio_4.setSelected(true);
			break;
		default:
			break;
		
		}
		
		
		question_text.setDisable(false);
		subject_text.setDisable(false);
		answer_line_1.setDisable(false);
		answer_line_2.setDisable(false);
		answer_line_3.setDisable(false);
		answer_line_4.setDisable(false);
		radio_1.setDisable(false);
		radio_2.setDisable(false);
		radio_3.setDisable(false);
		radio_4.setDisable(false);
		submitButton.setDisable(false);
		
		dbQuestion = null; // check if we should change the place of the nulling
		
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

	ObservableList<CloneQuestion> GetDataFromDBQuestion(ClientToServerOpcodes op, Object data) {
		// Send message to server
		DataElements de = new DataElements(op, data);
		if (sendRequestForDataFromServer(de) == -1)
			return null;
		
		while (dbQuestion == null) {
			System.out.print("");
		}
		return dbQuestion;
	}
	
	
	/**
	 * 
	 * we call this function every time there's change of a combo,
	 * therefore we want to clear all fields that linked to the data of a question
	 * 
	 */
	public void ClearAllFields() {
		
		subject_text.clear();
		question_text.clear();
		
		answer_line_1.clear();
		answer_line_2.clear();
		answer_line_3.clear();
		answer_line_4.clear();
		
		radio_1.setSelected(false);
		radio_2.setSelected(false);
		radio_3.setSelected(false);
		radio_4.setSelected(false);
		
	}

}
