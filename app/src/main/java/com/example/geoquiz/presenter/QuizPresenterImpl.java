package com.example.geoquiz.presenter;

import android.os.Bundle;

import com.example.geoquiz.R;
import com.example.geoquiz.repository.QuizRepository;

public class QuizPresenterImpl implements QuizPresenter {

    private static final String KEY_INDEX = "index";
    private static final String KEY_COUNT_CORRECT = "count_correct";
    private static final String KEY_COMPLETED_QUESTIONS = "is_completed_array";
    private static final String KEY_CHEATED_QUESTIONS = "cheated_questions";

    private QuizView mQuizView;
    private QuizRepository mQuizRepository;

    private byte[] mIsQuestionsCompleted = new byte[]{
            0, 0, 0, 0, 0,
    };
    private byte[] mCheatedQuestions = new byte[]{
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
        mCheatedQuestions = state.getByteArray(KEY_CHEATED_QUESTIONS);
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
        boolean isAnswerTrue = isUserPressedTrue == isAnswerTrue();

        int messageResId = (isUserCheater()) ?
                R.string.judgement_message :
                (isAnswerTrue) ?
                        R.string.correct_answer :
                        R.string.incorrect_answer;

        if (mQuizView != null) {
            mQuizView.showMessage(messageResId);
        }

        return (!isUserCheater()) && isAnswerTrue;
    }

    private boolean isUserCheater() {
        return mCheatedQuestions[mCurrentIndex] == 1;
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
        clearCheatedQuestions();
    }

    private void clearCompletedQuestions() {
        for (int i = 0; i < mIsQuestionsCompleted.length; i++) {
            mIsQuestionsCompleted[i] = 0;
        }
    }

    private void clearCheatedQuestions() {
        for (int i = 0; i < mCheatedQuestions.length; i++) {
            mCheatedQuestions[i] = 0;
        }
    }

    @Override
    public Bundle getSaveBundle() {
        Bundle outState = new Bundle();
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(KEY_COUNT_CORRECT, mCountCorrectAnswers);
        outState.putByteArray(KEY_COMPLETED_QUESTIONS, mIsQuestionsCompleted);
        outState.putByteArray(KEY_CHEATED_QUESTIONS, mCheatedQuestions);
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
    public boolean isAnswerTrue() {
        return mQuizRepository.getCurrentQuestion(mCurrentIndex).isAnswerTrue();
    }

    @Override
    public void onQuizViewDestroyed() {
        mQuizView = null;
    }

    @Override
    public void userIsCheater() {
        mCheatedQuestions[mCurrentIndex] = 1;
    }
}
