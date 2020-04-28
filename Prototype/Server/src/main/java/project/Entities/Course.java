package project.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Course")
public class Course implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "courseId")
	private int id;

	@Column(name = "courseName")
	private String courseName;

	@ManyToMany(mappedBy = "courses", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Question> questions;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "course_study", joinColumns = @JoinColumn(name = "courseId"), inverseJoinColumns = @JoinColumn(name = "studyId"))
	private List<Study> studies;

	public Course() {
		super();
		this.questions = new ArrayList<Question>();
		this.studies = new ArrayList<Study>();
	}

	public Course(String courseName) {
		super();
		this.courseName = courseName;
		this.questions = new ArrayList<Question>();
		this.studies = new ArrayList<Study>();
	}

	public int getId() {
		return id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void addQuestions(Question... questions) {
		for (Question question : questions) {
			this.questions.add(question);
			question.getCourses().add(this);
		}
	}

	public List<Study> getStudies() {
		return studies;
	}

	public void addStudies(Study... studies) {
		for (Study study : studies) {
			this.studies.add(study);
			study.getCourses().add(this);
		}
	}

}
