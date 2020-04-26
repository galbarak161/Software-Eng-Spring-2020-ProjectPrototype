package project.Prototype;

import java.io.IOException;
import java.util.logging.Logger;

import ocsf.client.AbstractClient;

public class ClientService extends AbstractClient {
	private static final Logger LOGGER = Logger.getLogger(ClientService.class.getName());

	private ClientMain clientM;

	public ClientService(String host, int port) {
		super(host, port);
		this.clientM = new ClientMain(this);
	}

	@Override
	protected void connectionEstablished() {
		// TODO Auto-generated method stub
		super.connectionEstablished();
		LOGGER.info("Connected to server.");

		try {
			clientM.mainLoopThread();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		clientM.displayMessage(msg);
	}

	@Override
	protected void connectionClosed() {
		// TODO Auto-generated method stub
		super.connectionClosed();
		clientM.closeConnection();
	}
}
