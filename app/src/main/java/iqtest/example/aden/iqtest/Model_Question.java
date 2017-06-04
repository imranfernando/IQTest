package iqtest.example.aden.iqtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Model_Question implements Serializable {
    private String Question;
    private ArrayList<String> Answers;
    private int CorrectAnswer;
    private int SelectedAnswer;
    private double TimeLimit;
    private String tips;
    private String Difficulty;
    //Mathematical, Logical ......
    private String QuestionType;

    //Constructors
    public Model_Question() {
    }

    public Model_Question(String question, ArrayList<String> answers, int correctAnswer, double timeLimit, String tip) {
        Question = question;
        Answers = answers;
        CorrectAnswer = correctAnswer;
        TimeLimit = timeLimit;
        tips = tip;
        SelectedAnswer = -1;
    }
    //Setters
    public void setSelectedAnswer(int selectedAnswer) {
        SelectedAnswer = selectedAnswer;
    }

    //Getters
    public double getTimeLimit() {
        return TimeLimit;
    }

    public String getQuestion() {
        return Question;
    }

    public List<String> getAnswers() {
        return Answers;
    }

    public int getCorrectAnswer() {
        return CorrectAnswer;
    }

    public int getSelectedAnswer() {
        return SelectedAnswer;
    }

    public boolean isSelectedAnswerCorrect(){
        return SelectedAnswer==CorrectAnswer;
    }

    public String getTips() {
        return tips;
    }
}
