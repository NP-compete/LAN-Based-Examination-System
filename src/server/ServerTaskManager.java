package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import common.Question;

public class ServerTaskManager implements Runnable {
	
	private int score = 0;
	
	private Random random = new Random();
	private ArrayList<Integer> randomNumbers = new ArrayList<Integer>();
	private ArrayList<Question> questions = new ArrayList<Question>();
	
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	public ServerTaskManager(Socket socket) {
		try {
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			
			System.out.println("Client connected successfully...");
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	private void write(String text) {
		try {
			dataOutputStream.writeUTF(text);
			dataOutputStream.flush();
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		int randomNumber = 0;
		
		try {
			for (int i=0; i<Server.questions.length; i++) {
				
				do {
					randomNumber = random.nextInt(10);
				}
				while (randomNumbers.contains(randomNumber));
				
				randomNumbers.add(randomNumber);
				questions.add(Server.questions[randomNumber]);
				
				write(Server.questions[randomNumber].toString());
				
				if (dataInputStream.readUTF().toLowerCase().charAt(0) == Server.questions[randomNumber].correctAnswer) {
					score++;
				}
				
			}
			
			write("%scr%You scored = " + score + " out of " + Server.questions.length);
			
			for (int i=0; i<questions.size(); i++) {
				Question temp = questions.get(i);
				
				write("%crctans%\nQ " + (i+1) + ". " + temp.question + "\nAns: " + temp.options[(int) temp.correctAnswer - 97]);
			}
			
			write("end");
			
			dataInputStream.close();
			dataOutputStream.close();
		}
		catch (Exception exc) {
			System.out.println("Client disconnected...");
		}
	}
	
}