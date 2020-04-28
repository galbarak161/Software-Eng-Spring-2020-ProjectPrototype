package project.Prototype;

import java.io.IOException;
import java.util.List;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import project.Entities.Study;
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
			System.out.println("Received message from client: opcode = " + de.getOpcode());
			
			switch (de.getOpcode()) {
			case 0:
				dataFromDB = handleSendStudiesToUser();
				de.setOpcode(DataElements.ServerToClientOpcodes.SendAllStudies.value);
				de.setData(dataFromDB);
				break;
			default:
				de.setOpcode(-1);
				de.setData(null);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			System.out.println("Send result to user! opcode = " + de.getOpcode());
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
