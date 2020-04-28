package project.Prototype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import project.Entities.*;
import project.Prototype.DataElements;

public class ServerMain extends AbstractServer {
	public ServerMain(int port) {
		super(port);
	}

	/**
	 * The function gets new msg from client
	 * Parsing the opcode and data
	 * Handle the client request
	 * Send back results
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
				dataFromDB = handleSendCoursesToUser(de.getData());
				de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.SendAllCoursesInStudy);
				de.setData(dataFromDB);
				break;
			case GetAllQuestionInCourse:
				dataFromDB = handleSendStudiesToUser();
				de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.SendAllQuestionInCourse);
				de.setData(dataFromDB);
				break;
			default:
				de.setOpCodeFromServer(DataElements.ServerToClientOpcodes.Error);
				de.setData(null);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			System.out.println("Send result to user! opcode = " + de.getOpCodeFromServer());
			sendToAllClients(de);
		}
	}

	/**
	 * handleSendStudiesToUser()
	 * @return all studies names from DB
	 */
	private Object handleSendStudiesToUser() {
		List<Study> listFromDB = null;
		String[] studiesName = null;
		try {
			listFromDB = HibernateMain.getDataFromDB(Study.class);
			studiesName = new String[listFromDB.size()];
			for (int i = 0; i < listFromDB.size(); i++) {
				studiesName[i] = listFromDB.get(i).getStudyName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return studiesName;
	}
	
	private Object handleSendCoursesToUser(Object study) {
		List<Course> listFromDB = null;
		List<String> coursesName = new ArrayList<String>();
		String[] names = null;
		try {
			listFromDB = HibernateMain.getDataFromDB(Course.class);
			for (int i = 0; i < listFromDB.size(); i++) {
				List<Study> studies = listFromDB.get(i).getStudies();
				for (int j = 0; j < studies.size(); j++) {
					if (studies.get(j).getStudyName().compareTo(study.toString()) == 0) {
						coursesName.add(listFromDB.get(i).getCourseName());
						break;
					}
				}
			}
			names = new String[coursesName.size()];
			for (int i=0;i<coursesName.size(); i++) {
				names[i] = coursesName.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return names;
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
		}
	}
}
