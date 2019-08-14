package com.example.geoquiz.presenter;

import android.os.Bundle;

import com.example.geoquiz.R;
import com.example.geoquiz.repository.QuizRepository;

public class QuizPresenterImpl implements QuizPresenter {

    private static final String KEY_INDEX = "index";
    private static final String KEY_COUNT_CORRECT = "count_correct";
    private static final String KEY_COMPLETED_QUESTIONS = "is_completed_array";

    private QuizView mQuizView;
    private QuizRepository mQuizRepository;

    private byte[] mIsQuestionsCompleted = new byte[]{
            0, 0, 0, 0, 0,
    };
    private int mCurrentIndex = 0;
    private int mCountCorrectAnswers = 0;

    public QuizPresenterImpl(QuizView quizView, QuizRepository quizRepository) {
        mQuizView = quizView;
        mQuizRepository = quizRepository;
    }

    public QuizPresenterImpl(QuizView quizView, QuizRepository quizRepository, Bundle state) {
        mQuizView = quizView;
        mQuizRepository = quizRepository;
        mCurrentIndex = state.getInt(KEY_INDEX, 0);
        mCountCorrectAnswers = state.getInt(KEY_COUNT_CORRECT, 0);
        mIsQuestionsCompleted = state.getByteArray(KEY_COMPLETED_QUESTIONS);
    }

    @Override
    public void onAnswerButtonPressed(boolean isUserPressedTrue) {
        if (checkAnswer(isUserPressedTrue)) {
            mCountCorrectAnswers++;
        }

        mIsQuestionsCompleted[mCurrentIndex] = 1;

        if (mQuizView != null) {
            mQuizView.lockAnswerButtons();
        }
    }

    private boolean checkAnswer(boolean isUserPressedTrue) {
        boolean isAnswerTrue = mQuizRepository.getCurrentQuestion(mCurrentIndex).isAnswerTrue();

        int messageResId = (isUserPressedTrue == isAnswerTrue) ?
                R.string.correct_answer :
                R.string.incorrect_answer;

        if (mQuizView != null) {
            mQuizView.showMessage(messageResId);
        }

        return isUserPressedTrue == isAnswerTrue;
    }

    @Override
    public void onNextButtonPressed() {
        if (mCurrentIndex + 1 < mQuizRepository.getQuestionsCount()) {
            mCurrentIndex++;
        } else {
            restartQuiz();
        }

        if (mQuizView != null) {
            mQuizView.setQuestion();
        }
    }

    @Override
    public void onPrevButtonPressed() {
        mCurrentIndex--;
        if (mQuizView != null) {
            mQuizView.setQuestion();
        }
    }

    private void restartQuiz() {
        if (mQuizView != null) {
            mQuizView.showMessage("Тест завершен! Ваша оценка - " + mCountCorrectAnswers);
        }
        mCurrentIndex = 0;
        mCountCorrectAnswers = 0;
        clearCompletedQuestions();
    }

    private void clearCompletedQuestions() {
        for (int i = 0; i < mIsQuestionsCompleted.length; i++) {
            mIsQuestionsCompleted[i] = 0;
        }
    }

    @Override
    public Bundle getSaveBundle() {
        Bundle outState = new Bundle();
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(KEY_COUNT_CORRECT, mCountCorrectAnswers);
        outState.putByteArray(KEY_COMPLETED_QUESTIONS, mIsQuestionsCompleted);
        return outState;
    }

    @Override
    public int getQuestionTextId() {
        return mQuizRepository.getCurrentQuestion(mCurrentIndex).getTextResId();
    }

    @Override
    public boolean isQuestionCompleted() {
        return mIsQuestionsCompleted[mCurrentIndex] == 1;
    }

    @Override
    public boolean isQuestionFirst() {
        return mCurrentIndex == 0;
    }

    @Override
    public void onQuizViewDestroyed() {
        mQuizView = null;
    }
}
