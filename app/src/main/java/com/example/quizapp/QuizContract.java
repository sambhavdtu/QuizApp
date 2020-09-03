package com.example.quizapp;

import android.provider.BaseColumns;

/**
 * It is a container for all the constants. It is final because we don't want to subclass it.
 * Its constructor is private, so no object can be created for the class.
 */
public final class QuizContract {

    private QuizContract() {}

    public static class CategoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_categories";
        public static final String COLUMN_NAME = "name";
    }



    public static class QuestionTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        public static final String COLUMN_ANSWER_NR = "answer_nr";
        public static final String COLUMN_QUESTION_LEVEL = "difficulty_level";
        public static final String COLUMN_QUESTION_CATEGORY = "category_id";
    }

}
