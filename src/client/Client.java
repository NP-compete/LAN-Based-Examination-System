package client;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import common.Question;

public class Client implements Runnable {
	
	private volatile boolean run = false;		// thread safe...
	
	private volatile String answer = "";
	
	private Socket socket;
	private Scanner scanner;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	private Robot robot;
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			int time = 60000;
			
			try {
				while (true) {
					Thread.sleep(100);
					
					time -= 100;
					
					if (answer.equals("")) {
						if (time == 0) {
							robot.keyPress(KeyEvent.VK_A);
							robot.keyRelease(KeyEvent.VK_A);
							robot.keyPress(KeyEvent.VK_ENTER);
							robot.keyRelease(KeyEvent.VK_ENTER);
						}
					}
					else {
						break;
					}
				}
			}
			catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	};
	
	private Thread timeKeeper;
	
	public static void main(String[] args) {
		new Thread(new Client(60225, "localhost")).start();
	}
	
	public Client(int port, String ipAddress) {
		scanner = new Scanner(System.in);
		
		try {
			robot = new Robot();
			System.out.println("Trying to connect to the server...");
			
			socket = new Socket(ipAddress, port);
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			
			run = true;
			
			System.out.println("Successfully connected to the server...");
		}
		catch (Exception exc) {
			run = false;
			
			exc.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		int counter = 1;
		
		try {
			if (run) {
				System.out.println("Ready to receive questions from server...\n");
				System.out.println("You will get 10 questions and for each question, you'll get one minute.\nIf you don't answer within one minute, '(a)' will be selected as the default answer.\nBest of luck... :)");
			}
			
			while (run) {
				while ((timeKeeper != null) && timeKeeper.isAlive()) {
					/*
					 * waiting for the death of time keeper thread...
					 */
					
					Thread.sleep(10);
				}
				
				String questionFromServer = dataInputStream.readUTF();
				
				if (questionFromServer.startsWith("%scr%")) {
					System.out.println("\n" + questionFromServer.substring(5, questionFromServer.length()));
					System.out.println("\nCorrect Answers\n===============");		// because, after score, server will send correct answers...
					
					continue;
				}
				else if (questionFromServer.startsWith("%crctans%")) {
					System.out.println(questionFromServer.substring(9));
					
					continue;
				}
				else if (questionFromServer.equalsIgnoreCase("end")) {
					break;
				}
				
				Question question = Question.parseQuestion(questionFromServer);
				question.display(counter);
				
				System.out.print("\nAns: ");
				
				answer = "";
				
				timeKeeper = new Thread(runnable);
				timeKeeper.start();
				
				answer = scanner.nextLine();
				
				dataOutputStream.writeUTF(answer);
				dataOutputStream.flush();
				
				counter++;
			}
			
			dataInputStream.close();
			dataOutputStream.close();
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
}