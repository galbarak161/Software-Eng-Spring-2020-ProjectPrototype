package project.Entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import project.CloneEntities.CloneQuestion;

@Entity
@Table(name = "Question")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "questionId")
	private int id;

	@Column(name = "questionCode")
	private int questionCode;

	@Column(name = "subject", length = 180)
	private String subject;

	@Column(name = "questionText", length = 180)
	private String questionText;

	@Column(name = "answer_1", length = 180)
	private String answer_1;

	@Column(name = "answer_2", length = 180)
	private String answer_2;

	@Column(name = "answer_3", length = 180)
	private String answer_3;

	@Column(name = "answer_4", length = 180)
	private String answer_4;

	@Column(name = "correctAnswer")
	private int correctAnswer;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "question_course", joinColumns = @JoinColumn(name = "questionId"), inverseJoinColumns = @JoinColumn(name = "courseId"))
	private List<Course> courses;

	public Question() {
		this.courses = new ArrayList<Course>();
	}

	public Question(String subject, String questionText, String answer_1, String answer_2, String answer_3,
			String answer_4, int correctAnswer) {
		this.questionCode = GenerateQuestionCode();
		this.subject = subject;
		this.questionText = questionText;
		this.answer_1 = answer_1;
		this.answer_2 = answer_2;
		this.answer_3 = answer_3;
		this.answer_4 = answer_4;
		this.correctAnswer = correctAnswer;
		this.courses = new ArrayList<Course>();
	}

	public CloneQuestion createClone() {
		CloneQuestion clone = new CloneQuestion(this.id, this.questionCode, this.subject, this.questionText,
				this.answer_1, this.answer_2, this.answer_3, this.answer_4, this.correctAnswer);
		return clone;
	}

	private int GenerateQuestionCode() {
		// TODO Generate code according to CourseId and QuestionId
		return 0;
	}

	public int getId() {
		return id;
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

	public List<Course> getCourses() {
		return courses;
	}

	public void addCourses(Course... courses) {
		for (Course course : courses) {
			this.courses.add(course);
			course.addQuestions(this);
		}
	}
}