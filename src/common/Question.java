package common;

public class Question {
	
	public char correctAnswer;
	public String question;
	public String[] options = new String[4];
	
	public void display(int questionNumber) {
		System.out.println("\nQ " + questionNumber + ". " + question);
		
		for (int i=0; i<options.length; i++) {
			System.out.print(options[i] + "\t\t");
			
			if (i%2 != 0) {
				System.out.println();
			}
		}
	}
	
	public static Question parseQuestion(String text) {
		try {
			String[] splits = text.split("%optns%");
			
			Question question = new Question();
			question.question = splits[0];
			question.options[0] = splits[1];
			question.options[1] = splits[2];
			question.options[2] = splits[3];
			question.options[3] = splits[4];
			
			return question;
		}
		catch (Exception exc) {
			exc.printStackTrace();
			
			return null;
		}
	}
	
	@Override
	public String toString() {
		String text = question;
		
		for (int i=0; i<options.length; i++) {
			text += "%optns%" + options[i];
		}
		
		return text;
	}
	
}