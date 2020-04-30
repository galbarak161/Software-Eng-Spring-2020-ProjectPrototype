package project.Prototype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import project.CloneEntities.*;
import project.Entities.*;
import project.Prototype.DataElements;

public class ServerMain extends AbstractServer {
	public ServerMain(int port) {
		super(port);
	}

	/**
	 * The function gets new msg from client Parsing the opcode and data Handle the
	 * client request Send back results
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		DataElements de = null;
		try {
			de = (DataElements) msg;
			Object dataFromDB = null;
			System.out.println("Received message from client: opcode = " + de.getOpcodeFromClient());

			switch (de.getOpcodeFromClient()) {
			case GetAllStudies:
				dataFromDB = handleSendStudiesToUser();
				de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.SendAllStudies);
				de.setData(dataFromDB);
				break;
			case GetAllCoursesInStudy:
				dataFromDB = handleSendCoursesFromStudy((CloneStudy) de.getData());
				de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.SendAllCoursesInStudy);
				de.setData(dataFromDB);
				break;
			case GetAllQuestionInCourse:
				dataFromDB = handleSendQuestionsFromCourse((CloneCourse) de.getData());
				de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.SendAllQuestionInCourse);
				de.setData(dataFromDB);
				break;
			case GetAllQuestion:
				dataFromDB = handleSendAllQuestions();
				de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.SendAllQuestion);
				de.setData(dataFromDB);
				break;
			case UpdateQuestion:
				dataFromDB = handleUpdateQuestion((CloneQuestion)de.getData());
				de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.UpdateQuestionResult);
				de.setData(dataFromDB);
				break;
			default:
				de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.Error);
				de.setData("handleMessageFromClient: Unknown Error");
			}

		} catch (Exception e) {
			e.printStackTrace();
			de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.Error);
			de.setData(e.getMessage());

		} finally {
			System.out.println("Send result to user! opcode = " + de.getOpCodeFromServer() + "\n");
			sendToAllClients(de);
		}
	}

	private Object handleUpdateQuestion(CloneQuestion data) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object handleSendAllQuestions() {
		List<Question> listFromDB = null;
		List<CloneQuestion> cloneQuestion = new ArrayList<CloneQuestion>();
		try {
			listFromDB = HibernateMain.getDataFromDB(Question.class);
			for (Question q : listFromDB) {
				cloneQuestion.add(q.createClone());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		cloneQuestion.forEach(q -> System.out.println(q.getQuestionCode()));
		return cloneQuestion;
	}

	private Object handleSendQuestionsFromCourse(CloneCourse cloneCourse) {
		List<Course> listFromDB = null;
		List<CloneQuestion> questionsFromCourse = new ArrayList<CloneQuestion>();
		try {
			listFromDB = HibernateMain.getDataFromDB(Course.class);
			for (Course c : listFromDB) {
				if(c.getId() == cloneCourse.getId()) {
					c.getQuestions().forEach(q -> questionsFromCourse.add(q.createClone()));
					break;
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		questionsFromCourse.forEach(q -> System.out.println(q.getQuestionCode()));
		return questionsFromCourse.toArray(new CloneQuestion[questionsFromCourse.size()]);
	}

	private Object handleSendStudiesToUser() {
		List<Study> listFromDB = null;
		List<CloneStudy> cloneStudies = new ArrayList<CloneStudy>();
		try {
			listFromDB = HibernateMain.getDataFromDB(Study.class);
			for (Study study : listFromDB) {
				cloneStudies.add(study.createClone());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		cloneStudies.forEach(q -> System.out.println(q.getStudyName()));
		return cloneStudies;
	}

	private Object handleSendCoursesFromStudy(CloneStudy cloneStudy) {
		List<Study> listFromDB = null;
		List<CloneCourse> courses = new ArrayList<CloneCourse>();
		try {
			listFromDB = HibernateMain.getDataFromDB(Study.class);
			for (Study study : listFromDB) {
				if (study.getId() == cloneStudy.getId()) {
					study.getCourses().forEach(course -> courses.add(course.createClone()));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		courses.forEach(q -> System.out.println(q.getCourseName()));
		return courses.toArray(new CloneCourse[courses.size()]);
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Required argument: <port>");
		} else {
			ServerMain server = new ServerMain(Integer.parseInt(args[0]));
			boolean hibernateStatus = HibernateMain.initHibernate();
			if (hibernateStatus == false) {
				System.out.println("Error during Hibernate initialization");
				return;
			}
			server.listen();
			System.out.println("Server ready!");
			
			System.out.println("Print data from handle functions\n");
			DataElements de = new DataElements();
		
			System.out.println("Check: handleSendStudiesToUser()");
			de.setOpcodeFromClient(DataElements.ClientToServerOpcodes.GetAllStudies);
			de.setData(null);
			server.handleSendStudiesToUser();
			System.out.println();
			
			System.out.println("Check: handleSendAllCourses(CloneStudy)");
			de.setOpcodeFromClient(DataElements.ClientToServerOpcodes.GetAllStudies);
			de.setData(new CloneStudy(1,"Arts"));
			server.handleSendCoursesFromStudy((CloneStudy)de.getData());
			System.out.println();
			
			System.out.println("Check: handleSendQuestionsFromCourseToUser(CloneCourse)");
			de.setOpcodeFromClient(DataElements.ClientToServerOpcodes.GetAllQuestionInCourse);
			de.setData(new CloneCourse(2,"Pharmacy"));
			server.handleSendQuestionsFromCourse((CloneCourse)de.getData());
			System.out.println();
			
			System.out.println("Check: handleSendAllQuestionsToUser()");
			de.setOpcodeFromClient(DataElements.ClientToServerOpcodes.GetAllQuestion);
			de.setData(new CloneStudy(1,"Arts"));
			server.handleSendAllQuestions();
			System.out.println();
		}
	}
}
