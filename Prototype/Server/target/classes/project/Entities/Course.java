package project.Entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import project.CloneEntities.*;

@Entity
@Table(name = "Course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "courseId")
	private int id;

	@Column(name = "courseName")
	private String courseName;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
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

	public CloneCourse createClone() {
		CloneCourse clone = new CloneCourse(this.id, this.courseName);
		return clone;
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

	public void addQuestions(Question question) {
		this.questions.add(question);
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
