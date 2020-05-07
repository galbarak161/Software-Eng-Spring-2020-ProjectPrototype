package project.Prototype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.CloneEntities.*;
import project.Prototype.DataElements.ClientToServerOpcodes;

public class PrimaryController {
	
	/******************* Variables *******************/

	@FXML
	private MenuBar menu;

	@FXML
	private Menu help_menu;

	@FXML
	private MenuItem instru_help_menu;

	@FXML
	private Label title;

	@FXML
	private Label title2;

	@FXML
	private ListView<CloneQuestion> qList;

	@FXML
	private Label question_label;

	@FXML
	private ComboBox<CloneQuestion> question_combo;

	@FXML
	private Label course_label;

	@FXML
	private Label study_label;

	@FXML
	private ComboBox<CloneCourse> course_combo;

	@FXML
	private ComboBox<CloneStudy> study_combo;

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
	
    @FXML
    private Tab edtiorTab;
    
    @FXML
    private TabPane mainTab;

	ToggleGroup radioGroup;

	private static ObservableList<CloneStudy> dbStudy = null;
	
	private static ObservableList<CloneCourse> dbCourse = null;

	private static ObservableList<CloneQuestion> dbQuestion = null;

	private static List<CloneQuestion> allQuestions;
	
	private static CloneQuestion dbUpdatedQ = null;

	private static Alert alert = new Alert(Alert.AlertType.ERROR);
	
	/************************************** Functions (non-attached to dialogs) **************************************/
	
	
	/**
	 * Function called automatically when GUI is starting.
	 * We get from DB all "Studies" and put them on study_combo ("Editor" tab)
	 * Then we get all the questions from DB and put them on "qList" ListView ("Selector" tab)
	 * 
	 */
	public void initialize() {
		
		ObservableList<CloneStudy> val = FXCollections.observableArrayList(GetDataFromDBStudy(ClientToServerOpcodes.GetAllStudies, null));
		if (val == null)
			return;
		study_combo.setItems(val);
		radioGroup = new ToggleGroup();
		radio_1.setToggleGroup(radioGroup);
		radio_2.setToggleGroup(radioGroup);
		radio_3.setToggleGroup(radioGroup);
		radio_4.setToggleGroup(radioGroup);

		dbStudy = null;
		
		allQuestions = GetAllQuestions(ClientToServerOpcodes.GetAllQuestion, null);

		if (allQuestions == null)
			return;
		
		for (CloneQuestion item : allQuestions) {
			qList.getItems().add(item);
		}
	}
	
	/**
	 * Function makes a "DataElemnts" object that is used for a data request from the server
	 * @param obj
	 * Contains a required data for server ("Course" name when getting questions for a specific course for example)
	 */
	void sendUpdateToServer(Object obj) {
		try {
			DataElements de = new DataElements();
			de.setData(obj);
			de.setOpcodeFromClient(ClientToServerOpcodes.UpdateQuestion);
			ClientMain.sendMessageToServer(de);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Activate as a respond for an unknown returned value or an error from server
	 * @param object
	 * Contains the error description
	 */
	public static void popError(String object) {
		
		alert.setHeaderText("Error from server");
		alert.getDialogPane()
				.setExpandableContent(new ScrollPane(new TextArea(object)));
		alert.showAndWait();
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
	
	/**
	 * Static function that is called from ClientService for handling returned "Studies" from server
	 * @param object
	 * Contains a list of "CloneStudy"
	 */
	public static void setDbStudy(Object object) {
		PrimaryController.dbStudy = FXCollections.observableArrayList((List<CloneStudy>)object);
		System.out.println("Recived all Studies from server\n");
	}
	
	/**
	 * Static function that is called from ClientService for handling returned "Courses" from server
	 * @param object
	 * Contains a list of "CloneCourse"
	 */
	public static void setDbCourse(Object object) {
		PrimaryController.dbCourse = FXCollections.observableArrayList((List<CloneCourse>)object);
		System.out.println("Recived Courses from server\n");
	}

	/**
	 * Static function that is called from ClientService for handling returned "Questions" from server
	 * @param object
	 * Contains a list of "CloneQuestion"
	 */
	public static void setDbQuestion(Object object) {
		PrimaryController.dbQuestion = FXCollections.observableArrayList((List<CloneQuestion>)object);
		System.out.println("Recived Questions from server\n");
	}
	
	/**
	 * Static function that is called from ClientService for handling getting all questions in DB from server
	 * @param object
	 * Contains a list of "CloneQuestion"
	 */
	@SuppressWarnings("unchecked")
	public static void setAllQuestion(Object object) {
		PrimaryController.allQuestions = (List<CloneQuestion>)object;
		System.out.println("Recived ALL Questions from server\n");
	}
	
	
	/**
	 * Static function that is called from ClientService for handling getting updated "Question" from server
	 * @param object
	 * Contains a "CloneQuestion" (updated)
	 */
	public static void handleUpdateQuestionsFromServer(CloneQuestion object) {
		dbUpdatedQ = object;
	}
	
	/**
	 * Filling all text fields and radio buttons of question on "Editor" tab from "CurrentQuestion" argument
	 * @param CurrentQuestion
	 * Contains a question selected from questions_combo or qList
	 */
	void addQuestionFields(CloneQuestion CurrentQuestion) {
		
		course_combo.setDisable(false);
		question_combo.setDisable(false);
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

	}

	/**
	 * Creating request from server and getting a "Study" back from server
	 * 
	 * @param op-  what type of request do we want (enums)
	 * @param data - null
	 * @return
	 */
	ObservableList<CloneStudy> GetDataFromDBStudy(ClientToServerOpcodes op, Object data) {
		// Send message to server
		DataElements de = new DataElements(op, data);
		if (sendRequestForDataFromServer(de) == -1)
			return null;

		while (dbStudy == null) {
			System.out.print("");
		}
		return dbStudy;
	}
	
	/**
	 * Creating request from server and getting a "Course" back from server
	 * 
	 * @param op-  what type of request do we want (enums)
	 * @param data - "Study"
	 * @return
	 */
	ObservableList<CloneCourse> GetDataFromDBCourse(ClientToServerOpcodes op, Object data) {
		// Send message to server
		DataElements de = new DataElements(op, data);
		if (sendRequestForDataFromServer(de) == -1)
			return null;

		while (dbCourse == null) {
			System.out.print("");
		}
		return dbCourse;
	}

	/**
	 * Creating request from server and getting a "Question" back from server
	 * 
	 * @param op-  what type of request do we want (enums)
	 * @param data - "Course"
	 * @return
	 */
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
	 * Creating request from server and getting all questions back from server
	 * 
	 * @param op-  what type of request do we want (enums)
	 * @param data - null
	 */
	List<CloneQuestion> GetAllQuestions(ClientToServerOpcodes op, Object data) {
		// Send message to server
		DataElements de = new DataElements(op, data);
		if (sendRequestForDataFromServer(de) == -1)
			return null;

		while (allQuestions == null) {
			System.out.print("");
		}
		return allQuestions;
	}

	/**
	 * 
	 * we call this function every time there's change of a combo, therefore we want
	 * to clear all fields that linked to the data of a question
	 * 
	 */
	public void ClearAllFields() {

		question_combo.getItems().clear();
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
	

	/************************************** Functions (attached to dialogs) **************************************/

	/**
	 * Opens the Instructions window when clicked "Help" on the menubar
	 * @param event- doesn't matter
	 */
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

	/**
	 * Handles double-click on the qList (Like clicking on "Edit" button)
	 * @param event - Contains the clicking event on the mouse
	 */
    @FXML
    void onDoubleClick(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ) {
        	onClickedEdit(new ActionEvent());
             }       
    }
    
    /**
     * Handle clicking on "Edit" button and presenting the selected question from qList
     * @param event
     */
	@FXML
	void onClickedEdit(ActionEvent event) {
		ObservableList<CloneQuestion> selected_q = qList.getSelectionModel().getSelectedItems();
		if (selected_q.isEmpty()) {
			alert.setHeaderText("Please select a question");
			alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea("No question has been selected")));
			alert.showAndWait();
			return;
		}

		if (selected_q.size() > 1) {
			alert.setHeaderText("Please select only one question");
			alert.getDialogPane()
					.setExpandableContent(new ScrollPane(new TextArea("Multiple questions has benn selected")));
			alert.showAndWait();
			return;
		}
		//ObservableList<CloneQuestion> allQ = FXCollections.observableArrayList(allQuestions);
		
		EventHandler<ActionEvent> handler = question_combo.getOnAction();
		//question_combo.setOnAction(null);
		//question_combo.setItems(allQ);
		//question_combo.setOnAction(handler);
		
		question_combo.setValue(selected_q.get(0));
		
		mainTab.getSelectionModel().select(edtiorTab);
		
		handler = study_combo.getOnAction();
		study_combo.setOnAction(null);
		study_combo.getSelectionModel().clearSelection();
		study_combo.setOnAction(handler);
		
		handler = course_combo.getOnAction();
		course_combo.setOnAction(null);
		course_combo.getItems().clear();
		course_combo.setOnAction(handler);
	}
	
	/**
	 * Display the retrieved "Studies" from server on study_combo
	 * Reset other fields on "Editor" tab
	 * @param event - doesn't matter
	 */
	@FXML
	void onClickedStudy(ActionEvent event) {
		ObservableList<CloneCourse> val = FXCollections.observableArrayList(GetDataFromDBCourse(ClientToServerOpcodes.GetAllCoursesInStudy, study_combo.getValue()));
		if (val == null)
			return;
		
		course_combo.setItems(val);
		ClearAllFields();
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

		dbStudy = null;
	}

	/**
	 * Display the retrieved "Courses" from server on course_combo
	 * Reset other fields on "Editor" tab, except study_combo
	 * @param event - doesn't matter
	 */
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
		ObservableList<CloneQuestion> val = FXCollections.observableArrayList(GetDataFromDBQuestion(ClientToServerOpcodes.GetAllQuestionInCourse,
				course_combo.getValue()));
		if (val == null)
			return;
		ClearAllFields();
		question_combo.setItems(val);
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

		dbCourse = null;
	}

	/**
	 * Display the a retrieved "Question" from server on the question text fields and radio buttons, also enable submit button.
	 * @param event - doesn't matter
	 */
	@FXML
	void onClickedQuestion(ActionEvent event) {
		
		if(dbQuestion == null) {
			if (question_combo.getValue() == null)
				addQuestionFields(qList.getSelectionModel().getSelectedItems().get(0));
			else
				addQuestionFields(question_combo.getValue());
		} else {
			
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
			
			addQuestionFields(question_combo.getValue());
			dbQuestion = null; // check if we should change the place of the nulling
		}	

	}
	
	/**
	 * Handles the click on "Submit" button- send an update question query to server
	 * Updates qList with updated question retrieved from server and also question_combo if needed
	 * On a success - "Submit" button changes its color to green
	 * On a failure - "Submit" button changes its color to red
	 * @param event - doesn't matter
	 * @throws Exception - Used for checking all fields are filled
	 */
	@FXML
	void onClickedSubmit(ActionEvent event) throws Exception {
		try {
			String bstyle=String.format("-fx-background-color: #FFFF00;");
			submitButton.setStyle(bstyle);
			CloneQuestion q = new CloneQuestion();
			q.clone(question_combo.getValue());
			if (subject_text.getText().isEmpty())
				throw new Exception("Subject is empty");
			q.setSubject(subject_text.getText());
			if (question_text.getText().isEmpty())
				throw new Exception("Question is empty");
			q.setQuestionText(question_text.getText());
			if (answer_line_1.getText().isEmpty())
				throw new Exception("Answer 1 is empty");
			q.setAnswer_1(answer_line_1.getText());
			if (answer_line_2.getText().isEmpty())
				throw new Exception("Answer 2 is empty");
			q.setAnswer_2(answer_line_2.getText());
			if (answer_line_3.getText().isEmpty())
				throw new Exception("Answer 3 is empty");
			q.setAnswer_3(answer_line_3.getText());
			if (answer_line_4.getText().isEmpty())
				throw new Exception("Answer 4 is empty");
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
			sendUpdateToServer(q);
			
			while(dbUpdatedQ == null)
			{
				System.out.print("");
			}
			
			if (question_combo.getItems().size() > 1) {
				for(CloneQuestion q2:question_combo.getItems()) {
					if(dbUpdatedQ.getId() == q2.getId())
					{
						EventHandler<ActionEvent> handler = question_combo.getOnAction();
						question_combo.setOnAction(null);
						question_combo.getItems().remove(q2);
						CloneQuestion newItem = dbUpdatedQ;
						question_combo.getItems().add(newItem);
						question_combo.setOnAction(handler);
						break;
					}
				}
			}
			
			for(CloneQuestion q2:qList.getItems()) {
				if(dbUpdatedQ.getId() == q2.getId())
				{
					qList.getItems().remove(q2);
					CloneQuestion newItem = dbUpdatedQ;
					qList.getItems().add(newItem);
					dbUpdatedQ = null;
					bstyle=String.format("-fx-background-color: #00FF09;");
					submitButton.setStyle(bstyle);
					return;
				}
			}
			
		} catch (Exception e) {
			String bstyle=String.format("-fx-background-color: #FF0000;");
			submitButton.setStyle(bstyle);
			alert.setHeaderText("Invalid input");
			alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea("Please fill all the fields")));
			alert.showAndWait();
		}
	}
	
	/**
	 * Limits the number of chars on TextArea to 100
	 * @param event
	 */
	@FXML
	void countChars100(KeyEvent event) {
		TextField n = (TextField)event.getSource();
		n.setTextFormatter(new TextFormatter<String>(change -> 
        change.getControlNewText().length() <= 100 ? change : null));
	}
	
	/**
	 * Limits the number of chars on TextArea to 180
	 * @param event
	 */
	@FXML
	void countChars180(KeyEvent event) {
		TextArea n = (TextArea)event.getSource();
		n.setTextFormatter(new TextFormatter<String>(change -> 
        change.getControlNewText().length() <= 180 ? change : null));
	}

}