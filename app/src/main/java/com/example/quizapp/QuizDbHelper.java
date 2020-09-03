package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 5;

    private SQLiteDatabase db;
    private static QuizDbHelper instance;

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance ==  null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @brief onCreate is only called for the first time DB is created and it won't be created every time
     *         on Activity creation, it is only destroyed once the app is deleted
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                QuizContract.CategoriesTable.TABLE_NAME + " ( " +
                QuizContract.CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " + QuizContract.QuestionTable.TABLE_NAME + " ( " +
                QuizContract.QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuizContract.QuestionTable.COLUMN_QUESTION_LEVEL + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_QUESTION_CATEGORY + " INTEGER, " +
                "FOREIGN KEY(" + QuizContract.QuestionTable.COLUMN_QUESTION_CATEGORY + ") REFERENCES " +
                QuizContract.CategoriesTable.TABLE_NAME + "(" + QuizContract.CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("What is the capital of India? ", new ArrayList<String>(Arrays.asList("Delhi", "Miami", "Tokyo", "Milan")), 1, Question.DifficultyLevel.EASY, Category.GEOGRAPHY);
        addQuestion(q1);
        Question q2 = new Question("What is the capital of Japan? ", new ArrayList<String>(Arrays.asList("Delhi", "Miami", "Tokyo", "Milan")), 3, Question.DifficultyLevel.EASY, Category.GEOGRAPHY);
        addQuestion(q2);
        Question q3 = new Question("What is the capital of Italy? ", new ArrayList<String>(Arrays.asList("Delhi", "Rome", "Tokyo", "Milan")), 2, Question.DifficultyLevel.MEDIUM, Category.GEOGRAPHY);
        addQuestion(q3);
        Question q4 = new Question("What is the capital of Germany? ", new ArrayList<String>(Arrays.asList("Delhi", "Miami", "Tokyo", "Berlin")), 4, Question.DifficultyLevel.MEDIUM, Category.GEOGRAPHY);
        addQuestion(q4);
        Question q5 = new Question("What is the capital of France? ", new ArrayList<String>(Arrays.asList("Paris", "Miami", "Tokyo", "Milan")), 1, Question.DifficultyLevel.HARD, Category.GEOGRAPHY);
        addQuestion(q5);
        Question q6 = new Question("What is the capital of Australia?", new ArrayList<String>(Arrays.asList("Delhi", "Syndney", "Melbourne", "Milan")), 2, Question.DifficultyLevel.HARD, Category.GEOGRAPHY);
        addQuestion(q6);
        Question q7 = new Question("Insertion in binary search tree has time-complexity of ____", new ArrayList<String>(Arrays.asList("O(1)", "O(logn)", "O(n)", "O(nlogn)")), 2, Question.DifficultyLevel.EASY, Category.PROGRAMMING);
        addQuestion(q7);
        Question q8 = new Question("Insertion in hash-map has amortized complexity of _____", new ArrayList<String>(Arrays.asList("O(1)", "O(n)", "O(n^2)", "O(n^3)")), 1, Question.DifficultyLevel.HARD, Category.PROGRAMMING);
        addQuestion(q8);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Programming");
        addCategory(c1);
        Category c2 = new Category("Geography");
        addCategory(c2);
        Category c3 = new Category("Mathematics");
        addCategory(c3);
    }

    private void addCategory(Category c) {
        ContentValues cv = new ContentValues();
        cv.put(QuizContract.CategoriesTable.COLUMN_NAME, c.getName());
        db.insert(QuizContract.CategoriesTable.TABLE_NAME, null, cv);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuizContract.QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION1, question.getAnswers().get(0));
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION2, question.getAnswers().get(1));
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION3, question.getAnswers().get(2));
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION4, question.getAnswers().get(3));
        cv.put(QuizContract.QuestionTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuizContract.QuestionTable.COLUMN_QUESTION_LEVEL, question.getLevel().name());
        cv.put(QuizContract.QuestionTable.COLUMN_QUESTION_CATEGORY, question.getCategory());
        db.insert(QuizContract.QuestionTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuizContract.CategoriesTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                Category cat = new Category(c.getString(c.getColumnIndex(QuizContract.CategoriesTable.COLUMN_NAME)));
                cat.setId(c.getInt(c.getColumnIndex(QuizContract.CategoriesTable._ID)));
                categoryList.add(cat);
            } while(c.moveToNext());
        }
        return categoryList;
    }

    public ArrayList<Question> getQuestions(int categoryId, Question.DifficultyLevel dLevel) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME + " WHERE " + QuizContract.QuestionTable.COLUMN_QUESTION_LEVEL + " = ? " + " AND " +
                QuizContract.QuestionTable.COLUMN_QUESTION_CATEGORY + " = ? ", new String[] { dLevel.name(), String.valueOf(categoryId)});
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                ArrayList<String> options = new ArrayList<String>();
                options.add(0, c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION1)));
                options.add(1, c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION2)));
                options.add(2, c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION3)));
                options.add(3, c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION4)));
                question.setAnswers(options);
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_ANSWER_NR)));
                question.setLevel(Question.DifficultyLevel.valueOf(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION_LEVEL))));
                question.setCategory(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION_CATEGORY)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
