package project.Prototype;

import java.io.Serializable;

/**
 * DataElements is a shared class for both server and client This file has to be
 * the same in both projects Last update - 28/04/20
 * 
 * @author Gal
 *
 */

@SuppressWarnings("serial")
public class DataElements implements Serializable {

	// Opcodes 0-9
	public enum ClientToServerOpcodes {
		GetAllStudies(0), GetAllCoursesInStudy(1), GetAllQuestionInCourse(2), UpdateQuestion(3);

		public int value;

		private ClientToServerOpcodes(int value) {
			this.value = value;
		}
	}

	// Opcodes 10-19
	public enum ServerToClientOpcodes {
		SendAllStudies(10), SendAllCoursesInStudy(11), SendAllQuestionInCourse(12), UpdateQuestionResult(13);

		public int value;

		private ServerToClientOpcodes(int value) {
			this.value = value;
		}
	}

	private int opcode;
	private Object data;

	public DataElements() {
	}

	public DataElements(ClientToServerOpcodes opCodeFromClient, Object data) {
		this.opcode = opCodeFromClient.value;
		this.data = data;
	}

	public DataElements(ServerToClientOpcodes opCodeFromServer, Object data) {
		this.opcode = opCodeFromServer.value;
		this.data = data;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}