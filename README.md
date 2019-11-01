# Online-Examination-System
A simple Online Examination System written in Java (socket programming).

# Requirements
1) Java SE Runtime Environment (JRE) 7 or newer.

# Features
1) Server can handle multiple clients simultaneously.  
2) Server arranges different sequence of questions for different clients.  
3) If a question is not answered in a minute, that question will be answered automatically with a default answer (a).  
4) Next question will be sent by the server if the previous question is answered or timed out.  
5) Server sends the score along with the correct answers to the client after the examination is over.

# How to run?

git clone 

```
cd bin
```

## To start the server: 

```
java server.Server
```


## To start the client:

```
java client.Client
```

# Classes
 
## Question
        
### 
| **Data Members**    |
| -------------       |
| char correctAnswer  |
| String question     |
| String[] options    |


### 
| **Member Functions** | **Functionality** |
| --- | --- |
| `display(int questionNumber)` | Displays question number, question and options in proper format |
| `parseQuestion(String text)` | Converts the passed text to a Question object; *return* Question object |
| `toString()` | converts the Question object to string - question followed by options as one string |



 
## Server implements Runnable

### 
| **Data Members**    |
| -------------       |
| boolean run |
| Question[] questions |
| InputStream inputStream |
| InputStreamReader inputStreamReader |
| BufferedReader bufferedReader |
| ServerSocket serverSocket |


### 
| **Member Functions** | **Functionality** |
| --- | --- |
| main() | create thread which calls the Server constructor to begin execution |
| Server(int port) | Consructor; calls loadQuestions; creates a socket on port passed as an argument; sets run to true |
| loadQuestions() | reads from the file "/questions/questions.txt"; converts each question to a Question object and puts on the inputStream; closes the streams |
| run() | create threads for clients; calles ServerTaskManager which accepts the connection from clients; starts threads |
  
## ServerTaskManager implements Runnable
       
### 
| **Data Members**    |
| -------------       |
| int score |
| Random random | 
| ArrayList<Integer> randomNumbers |
| ArrayList<Question> questions |
| DataInputStream dataInputStream |
| DataOutputStream dataOutputStream |


### 
| **Member Functions** | **Functionality** |
| --- | --- |
|ServerTaskManager(Socket socket) | creates input/output datastreams; accepts client connection |
| write(String text) | writes data onto dataOutputStream |
| run() | randomly adds questions onto the dataOutputStream; reads data from dataInputStream (client answers); add up the scores if answer matches the correct answer; writes total score on the dataOutputStream; writes the questions with correct answers on the output stream; writes end on the dataOutputStream to signfy end of file and stop reading from the streams; closes the streams |
 
 
## Client implements Runnable

### 
| **Data Members**    |
| -------------       |
| boolean run |
| String answer |
| Socket socket |
| DataInputStream dataInputStream |
| DataOutputStream dataOutputStream |
| Runnable runnable |


### 
| **Member Functions** | **Functionality** |
| --- | --- |
| main() | creates a thread calling Client constructor and connects to server (here localhost on given port) |
| Client(int port, String ipAddress) | creates a socket with specified ip address and port; creates input and output data streams; sets run to true; |
| run() of class Client | reads questions from DataInputStream of type Question; parses the question read; takes input from user and writes the answer on the dataOutputStream; if all questions are answered; displays the score and the answers to all questions from the dataInputStream |
| run() of object Runnable inside class Client | sets timer to 1 minute, if not answered in 1 minutes, defaults to option a|


# Class Diagram
![Reload Page](https://github.com/NP-compete/LAN-Based-Examination-System/blob/master/Class%20Diagram.jpg, "Class Diagram")



# Team
| Name        | Roll No. | Linkedin  |
| ------------- |:-----------:|:----- |
| Nandini Soni | 17MCMC61 | linkedin.com/in/sohamdutta/ |
| Soham Dutta | 17MCMC21 | linkedin.com/in/nandini-soni/ |
