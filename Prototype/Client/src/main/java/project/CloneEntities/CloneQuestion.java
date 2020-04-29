package project.CloneEntities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CloneQuestion implements Serializable {

	private int id;

	private int questionCode;

	private String subject;

	private String questionText;

	private String answer_1;

	private String answer_2;

	private String answer_3;

	private String answer_4;

	private int correctAnswer;

	private List<CloneCourse> courses;

	public CloneQuestion() {
		this.courses = new ArrayList<CloneCourse>();
	}

	public CloneQuestion(int id, int questionCode, String subject, String questionText, String answer_1,
			String answer_2, String answer_3, String answer_4, int correctAnswer) {
		this.id = id;
		this.questionCode = questionCode;
		this.subject = subject;
		this.questionText = questionText;
		this.answer_1 = answer_1;
		this.answer_2 = answer_2;
		this.answer_3 = answer_3;
		this.answer_4 = answer_4;
		this.correctAnswer = correctAnswer;
		this.courses = new ArrayList<CloneCourse>();
	}

	public void clone(CloneQuestion copyFrom) {
		this.id = copyFrom.getId();
		this.questionCode = copyFrom.getQuestionCode();
		this.subject = copyFrom.getSubject();
		this.questionText = copyFrom.getQuestionText();
		this.answer_1 = copyFrom.getAnswer_1();
		this.answer_2 = copyFrom.getAnswer_2();
		this.answer_3 = copyFrom.getAnswer_3();
		this.answer_4 = copyFrom.getAnswer_4();
		this.correctAnswer = copyFrom.getCorrectAnswer();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(int questionCode) {
		this.questionCode = questionCode;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getAnswer_1() {
		return answer_1;
	}

	public void setAnswer_1(String answer_1) {
		this.answer_1 = answer_1;
	}

	public String getAnswer_2() {
		return answer_2;
	}

	public void setAnswer_2(String answer_2) {
		this.answer_2 = answer_2;
	}

	public String getAnswer_3() {
		return answer_3;
	}

	public void setAnswer_3(String answer_3) {
		this.answer_3 = answer_3;
	}

	public String getAnswer_4() {
		return answer_4;
	}

	public void setAnswer_4(String answer_4) {
		this.answer_4 = answer_4;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public List<CloneCourse> getCourses() {
		return courses;
	}

	public void setCourses(List<CloneCourse> courses) {
		this.courses = courses;
	}

}