package model;

import java.sql.*;

public class Quiz {
    private Question questions[];
    int i;
    int j;
    int points;
    public Quiz() throws ClassNotFoundException, SQLException {
        this.questions = new Question[100];
        this.i = 0;
        this.j = 0;
        this.points = 0;
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
        Statement stmt=con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from questions");
        while(rs.next()){
            String question = rs.getString(2);
            String answers[] = {rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)};
            String correct = rs.getString(7);
            this.addQuestion(answers, correct, question);
        }
        con.close();
    }
    public void addQuestion(String answers[], String correctAnswer, String question){
        questions[i] = new Question(answers, correctAnswer,  question);
        i++;
    }

    public boolean guess(String guess) {
        if(questions[j].guess(guess)){
            j++;
            points++;
            return true;
        }
        j++;
        return false;
    }
    public String[] getAnswers(){ return questions[j].getAnswers();    }

    public String getQuestion(){return questions[j].getQuestion();}
    public int getPoints(){return this.points;}
    public boolean hasNext(){
        if(questions[j+1] != null) {
            return true;
        } else return false;
    }

}
/* SQL Code
CREATE TABLE questions (
    idquestions INT,
    questionText VARCHAR(150),
    ans1 VARCHAR(150),
    ans2 VARCHAR(150),
    ans3 VARCHAR(150),
    ans4 VARCHAR(150),
    correct VARCHAR(150),
);

INSERT INTO questions (idquestions, questionText, ans1,ans2,ans3,ans4,correct)
VALUES ("1", "What comes after 3?", "1", "2","3", "4", "d");

INSERT INTO questions (idquestions, questionText, ans1,ans2,ans3,ans4,correct)
VALUES ("2", "What is the course code", "DH2642", "ID1206", "ID1212", "IV1350", "c");

INSERT INTO questions (idquestions, questionText, ans1,ans2,ans3,ans4,correct)
VALUES ("3", "What university is this", "BTH", "KTH", "LTH", "Chalmers", "b");

INSERT INTO questions (idquestions, questionText, ans1,ans2,ans3,ans4,correct)
VALUES ("4", "What is this framework called","Spring", "Node.js", "MongoDB", "C++", "a");

INSERT INTO questions (idquestions, questionText, ans1,ans2,ans3,ans4,correct)
VALUES ("5", "What comes after b?", "d", "c", "b", "d", "b");
*/

