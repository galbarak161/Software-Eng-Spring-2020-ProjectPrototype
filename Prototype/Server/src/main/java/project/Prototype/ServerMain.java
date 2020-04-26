package project.Prototype;

import java.io.IOException;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ServerMain extends AbstractServer {
	public ServerMain(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("Received Message: " + msg.toString());
		sendToAllClients(msg);
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Required argument: <port>");
		} else {
			ServerMain server = new ServerMain(Integer.parseInt(args[0]));
			server.listen();
			System.out.println("Server ready!");
			//did i just branched righ now??????
		}
	}
}
