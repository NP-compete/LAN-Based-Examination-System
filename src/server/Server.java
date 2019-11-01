package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;

import common.Question;

public class Server implements Runnable {
	
	private volatile boolean run = false;		// thread safe...
	
	public static volatile Question[] questions = new Question[10];		// thread safe...
	
	private InputStream inputStream;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	
	private ServerSocket serverSocket;
	
	public static void main(String[] args) {
		new Thread(new Server(60225)).start();
	}
	
	public Server(int port) {
		loadQuestions();
		
		try {
			System.out.println("Trying to start server...");
			
			serverSocket = new ServerSocket(port);
			run = true;
			
			System.out.println("Server started successfully...");
		}
		catch (Exception exc) {
			run = false;
			exc.printStackTrace();
		}
	}
	
	private void loadQuestions() {
		System.out.println("Loading questions...");
		
		int i = 0, j = 0, counter = 0;
		String text;
		
		try {
			inputStream = Server.class.getResourceAsStream("/questions/questions.txt");
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			
			while ((text = bufferedReader.readLine()) != null) {
				switch (counter) {
				case 0:
					questions[i] = new Question();
					questions[i].question = text;
					
					break;
				case 1:
				case 2:
				case 3:
				case 4:
					questions[i].options[j] = text;
					j++;
					
					break;
				case 5:
					questions[i].correctAnswer = text.charAt(0);
					i++;
					j = 0;
					
					break;
				default:
					break;
				}
				
				counter++;
				
				if (counter > 5) {
					counter = 0;
				}
			}
			
			inputStream.close();
			inputStreamReader.close();
			bufferedReader.close();
			
			System.out.println("Questions loaded successfully...");
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (run) {
			try {
				Thread.sleep(100);
				
				System.out.println("Waiting for client...");
				
				new Thread(new ServerTaskManager(serverSocket.accept())).start();
			}
			catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
}