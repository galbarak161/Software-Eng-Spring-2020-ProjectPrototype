package project.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "Study")
public class Study implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "studyId")
	private int id;

	@Column(name = "studyName")
	private String studyName;

	@ManyToMany(mappedBy = "studies", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Course> courses;

	public Study() {
		super();
		this.courses = new ArrayList<Course>();
	}
	
	public Study(String studyName) {
		super();
		this.studyName = studyName;
		this.courses = new ArrayList<Course>();
	}
	
	public int getId() {
		return id;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void addCourses(Course... courses) {
		for (Course course : courses) {
			this.courses.add(course);
			course.getStudies().add(this);
		}
	}
}
