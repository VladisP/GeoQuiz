package com.example.geoquiz.repository;

import com.example.geoquiz.R;
import com.example.geoquiz.entities.Question;

public class QuizRepositoryImpl implements QuizRepository {

    private Question[] mQuestions = new Question[]{
            new Question(R.string.question_Moscow, true),
            new Question(R.string.question_Sydney, false),
            new Question(R.string.question_London, true),
            new Question(R.string.question_NewYork, false),
            new Question(R.string.question_Gothenburg, false),
    };

    @Override
    public Question getCurrentQuestion(int index) {
        return mQuestions[index];
    }

    @Override
    public int getQuestionsCount() {
        return mQuestions.length;
    }
}
