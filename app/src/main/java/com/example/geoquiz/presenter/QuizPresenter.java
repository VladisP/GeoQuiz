package com.example.geoquiz.presenter;

import android.os.Bundle;

public interface QuizPresenter {

    void onAnswerButtonPressed(boolean isUserPressedTrue);

    void onNextButtonPressed();

    void onPrevButtonPressed();

    void onQuizViewDestroyed();

    Bundle getSaveBundle();

    int getQuestionTextId();

    boolean isQuestionCompleted();

    boolean isQuestionFirst();

    interface QuizView {

        void showMessage(int messageResId);

        void showMessage(String message);

        void lockAnswerButtons();

        void unlockAnswerButtons();

        void lockPrevButton();

        void unlockPrevButton();

        void setQuestion();
    }
}
