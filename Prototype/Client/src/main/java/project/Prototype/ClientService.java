package project.Prototype;

import java.io.IOException;
import java.util.logging.Logger;

import ocsf.client.AbstractClient;
import project.CloneEntities.*;

public class ClientService extends AbstractClient {
	private static final Logger LOGGER = Logger.getLogger(ClientService.class.getName());
	private ClientMain clientM;

	public ClientService(String host, int port) {
		super(host, port);
		this.clientM = new ClientMain(this);
	}

	@Override
	protected void connectionEstablished() {
		super.connectionEstablished();
		LOGGER.info("Connected to server.");

		try {
			clientM.mainLoopThread();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void connectionClosed() {
		super.connectionClosed();
		clientM.closeConnection();
	}

	/**
	 * The function gets new msg from server Parsing the opcode and data Handle the
	 * server results
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
		try {
			DataElements de = (DataElements) msg;
			System.out.println("Received message from server: opcode = " + de.getOpcodeFromClient());
			switch (de.getOpCodeFromServer()) {
			case SendAllStudies:
				handleGetStudiesFromServer(de.getData());
				break;
			case SendAllCoursesInStudy:
				handleGetCoursesFromServer(de.getData());
				break;
			case SendAllQuestionInCourse:
				handleGetQuestionsFromServer(de.getData());
				break;
			default:
				clientM.displayMessageOnConsole(de.getData());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleGetStudiesFromServer(Object object) {
		PrimaryController.setDbCollect((String[]) object);
	}

	private void handleGetCoursesFromServer(Object object) {
		PrimaryController.setDbCollect((String[]) object);
	}

	private void handleGetQuestionsFromServer(Object object) {
		PrimaryController.setDbQuestion((CloneQuestion[]) object);
	}
}
