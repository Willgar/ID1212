package model;

public class Quiz {
    private String[] answers;
    private String correctAnswer;

    public Quiz(String answers[], String correctAnswer){
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public boolean guess(String guess) {
        System.out.println(guess + " and " + correctAnswer);
        if(guess.equals(correctAnswer)){
            System.out.println("hello");
            return true;
        }
        return false;
    }
    public String[] getAnswers(){
        return this.answers;
    }
    public void setAnswers(String answers[], String correctAnswer){
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }
    public void setCorrect(String answer){
        this.correctAnswer=answer;
    }
}
