package com.example.quizapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;



public class Question implements Parcelable {

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeStringList(answers);
        dest.writeInt(answerNr);
        dest.writeString(level.name());
        dest.writeInt(category);
    }

    public enum DifficultyLevel { EASY, MEDIUM, HARD};

    private String question;
    private ArrayList<String> answers;
    private int answerNr;
    private DifficultyLevel level;
    private int category;

    public Question(String question, ArrayList<String> answers, int answerNr, DifficultyLevel level, int category) {
        this.question = question;
        this.answers = answers;
        this.answerNr = answerNr;
        this.level = level;
        this.category = category;
    }

    public Question() {

    }

    protected Question(Parcel in) {
        question = in.readString();
        answers = in.createStringArrayList();
        answerNr = in.readInt();
        level = DifficultyLevel.valueOf(in.readString());
        category = in.readInt();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }

    public DifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
