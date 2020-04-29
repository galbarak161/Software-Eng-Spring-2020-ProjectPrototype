package project.CloneEntities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CloneStudy implements Serializable {

	private int id;

	private String studyName;

	private List<CloneCourse> courses;

	public CloneStudy() {
		this.courses = new ArrayList<CloneCourse>();
	}

	public CloneStudy(int id, String studyName) {
		this.id = id;
		this.studyName = studyName;
		this.courses = new ArrayList<CloneCourse>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public List<CloneCourse> getCourses() {
		return courses;
	}

	public void setCourses(List<CloneCourse> courses) {
		this.courses = courses;
	}
}
