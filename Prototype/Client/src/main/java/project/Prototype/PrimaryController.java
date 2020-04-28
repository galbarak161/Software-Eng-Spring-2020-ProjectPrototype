package project.Prototype;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.fxml.Initializable;

/**
 * the class should inherit from Initializable because we want to initialize the study combo box
 * with the query that selects all studies.
 * 
 * at the moment the information is initialized with random input' until there will be a DB
 * 
 * @author orash and sagi
 *
 */

public class PrimaryController implements Initializable {

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
	private static ObservableList<String> dbStudy = null;

	public static ObservableList<String> getDbStudy() {
		return dbStudy;
	}

	public static void setDbStudy(String[] object) {
		PrimaryController.dbStudy = FXCollections.observableArrayList(object);
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
<<<<<<< HEAD
	}
=======
    }
    
    
    //the way to initialize the strings for the combo boxes
    private ObservableList<String> dbStudy = FXCollections.observableArrayList("SQLite", "Moshe", "Sami", "Yaniv", "Baruch");
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	study_combo.setItems(dbStudy);
    	
    	//in the initialize of the form we set all radio buttons as a group 
    	radioGroup = new ToggleGroup();
    	radio_1.setToggleGroup(radioGroup);
    	radio_2.setToggleGroup(radioGroup);
    	radio_3.setToggleGroup(radioGroup);
    	radio_4.setToggleGroup(radioGroup);
    }
    
    
    
    /**
     * when the user selects a course, we send a query to show us the relevant study
     * we just enable the courses combo box, with the relevant courses (which linked to the chosen study)
     * 
     * @param event
     */
    @FXML
    void onClickedStudy(ActionEvent event) {
    	
        ObservableList<String> dbCourse = FXCollections.observableArrayList(study_combo.getValue(), "chino");
        course_combo.setItems(dbCourse);
        
        radio_1.setDisable(true);
        radio_2.setDisable(true);
        radio_3.setDisable(true);
        radio_4.setDisable(true);
    	
        subject_text.setDisable(true);
        question_text.setDisable(true);
        
        answer_line_1.setDisable(true);
        answer_line_2.setDisable(true);
        answer_line_3.setDisable(true);
        answer_line_4.setDisable(true);
        
        question_combo.setDisable(true);
        
        primaryButton.setDisable(true);
        
    	course_combo.setDisable(false);
    }
    
    
    
    
    /**
     * when the user selects a course, we send a query to show us the relevant course
     * we just enable the questions combo box
     * 
     * @param event
     */
    @FXML
    void onCourseClicked(ActionEvent event) {
    	
        
        ObservableList<String> dbQuest = FXCollections.observableArrayList(course_combo.getValue());
        question_combo.setItems(dbQuest);
        question_combo.setDisable(false);
        
        radio_1.setDisable(true);
        radio_2.setDisable(true);
        radio_3.setDisable(true);
        radio_4.setDisable(true);
    	
        subject_text.setDisable(true);
        question_text.setDisable(true);
        
        answer_line_1.setDisable(true);
        answer_line_2.setDisable(true);
        answer_line_3.setDisable(true);
        answer_line_4.setDisable(true);
        
        primaryButton.setDisable(true);
    }
    
    
    
    
    /**
     * when the user selects a question from the combo, we send a query to show us the relevant questiion
     * As well all texts, radio buttons, submit are enabled
     * 
     * @param event
     */
    @FXML
    void onClickedQuestion(ActionEvent event) {
    	
    	subject_text.setDisable(false);
    	subject_text.setText("Kol Hacavod");
    	
    	
        question_text.setDisable(false);
        question_text.setText("Adain Kol HaCavod");
        
        answer_line_1.setDisable(false);
        answer_line_1.setText("Kol hacavod");
        
        answer_line_2.setDisable(false);
        answer_line_2.setText("Kol hacavod");
        
        answer_line_3.setDisable(false);
        answer_line_3.setText("Kol hacavod");
        
        answer_line_4.setDisable(false);
        answer_line_4.setText("Kol hacavod");
        
        primaryButton.setDisable(false);
        
        radio_1.setDisable(false);
        radio_2.setDisable(false);
        radio_3.setDisable(false);
        radio_4.setDisable(false);
        
        radio_2.setSelected(true);
>>>>>>> refs/remotes/origin/master

<<<<<<< HEAD
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
=======
    }
    
    
    
    
    /**
     * the function is called when we want to disable all radio buttons, texts
     * and the relevant combos
     */
 /*   public void DisableAll() {
    	
    	radio_1.setDisable(true);
        radio_2.setDisable(true);
        radio_3.setDisable(true);
        radio_4.setDisable(true);
    	
        subject_text.setDisable(true);
        question_text.setDisable(true);
        
        answer_line_1.setDisable(true);
        answer_line_2.setDisable(true);
        answer_line_3.setDisable(true);
        answer_line_4.setDisable(true);
        
        question_combo.setDisable(true);
        
        primaryButton.setDisable(true);
    }*/
>>>>>>> refs/remotes/origin/master

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Send message to server
		DataElements de = new DataElements(ClientToServerOpcodes.GetAllStudies, null);
		if (sendRequestForDataFromServer(de) == -1)
			return;

		while (dbStudy == null)
			;

		System.out.println("JavaFx: load form");
		study_combo.setItems(dbStudy);
		radioGroup = new ToggleGroup();
		radio_1.setToggleGroup(radioGroup);
		radio_2.setToggleGroup(radioGroup);
		radio_3.setToggleGroup(radioGroup);
		radio_4.setToggleGroup(radioGroup);
	}

	@FXML
	void onClickedStudy(ActionEvent event) {
		ObservableList<String> dbCourse = FXCollections.observableArrayList(study_combo.getValue());
		course_combo.setItems(dbCourse);
		course_combo.setDisable(false);
	}

	@FXML
	void onCourseClicked(ActionEvent event) {

		ObservableList<String> dbQuest = FXCollections.observableArrayList(course_combo.getValue());
		question_combo.setItems(dbQuest);
		question_combo.setDisable(false);
	}

	@FXML
	void onClickedQuestion(ActionEvent event) {

		subject_text.setDisable(false);
		subject_text.setText("Kol Hacavod");

		question_text.setDisable(false);
		question_text.setText("Adain Kol HaCavod");

		answer_line_1.setDisable(false);
		answer_line_1.setText("Kol hacavod");

		answer_line_2.setDisable(false);
		answer_line_2.setText("Kol hacavod");

		answer_line_3.setDisable(false);
		answer_line_3.setText("Kol hacavod");

		answer_line_4.setDisable(false);
		answer_line_4.setText("Kol hacavod");

		primaryButton.setDisable(false);

		radio_1.setDisable(false);
		radio_2.setDisable(false);
		radio_3.setDisable(false);
		radio_4.setDisable(false);

		radio_2.setSelected(true);

	}
}
