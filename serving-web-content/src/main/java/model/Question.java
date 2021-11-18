package model;

public class Question {
    private String[] answers;
    private String correctAnswer;
    private String question;

    public Question(String answers[], String correctAnswer, String question){
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.question = question;
    }
    public String[] getAnswers(){
        return this.answers;
    }
    public void setAnswers(String answers[], String correctAnswer){
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }
    public String getQuestion() { return this.question;}

    public boolean guess(String guess) {
        System.out.println(guess + " and " + correctAnswer);
        if(guess.equals(correctAnswer)){
            return true;
        }
        return false;
    }
}
