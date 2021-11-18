package model;

public class Quiz {
    private Question questions[];
    int i;
    int j;
    int points;
    public Quiz(){
        this.questions = new Question[100];
        this.i = 0;
        this.j = 0;
        this.points = 0;
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
