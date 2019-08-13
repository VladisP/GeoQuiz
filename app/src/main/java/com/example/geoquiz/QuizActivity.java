package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "index";
    private static final String KEY_COUNT_CORRECT = "count_correct";
    private static final String KEY_COMPLETED_QUESTIONS = "is_completed_array";
    private static final String KEY_ENABLE_PREV_BTN = "is_enable_prev_btn";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private Question[] mQuestions = new Question[]{
            new Question(R.string.question_Moscow, true),
            new Question(R.string.question_Sydney, false),
            new Question(R.string.question_London, true),
            new Question(R.string.question_NewYork, false),
            new Question(R.string.question_Gothenburg, false),
    };

    private byte[] mIsQuestionsCompleted = new byte[]{
            0, 0, 0, 0, 0,
    };
    private int mCurrentIndex = 0;
    private int mCountCorrectAnswers = 0;
    private boolean mEnablePrevBtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsQuestionsCompleted = savedInstanceState.getByteArray(KEY_COMPLETED_QUESTIONS);
            mCountCorrectAnswers = savedInstanceState.getInt(KEY_COUNT_CORRECT, 0);
            mEnablePrevBtn = savedInstanceState.getBoolean(KEY_ENABLE_PREV_BTN, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mQuestionTextView = findViewById(R.id.question_text_view);
        mTrueButton = findViewById(R.id.btn_true);
        mFalseButton = findViewById(R.id.btn_false);
        mNextButton = findViewById(R.id.next_btn);
        mPrevButton = findViewById(R.id.prev_btn);

        setQuestion();
        mPrevButton.setEnabled(mEnablePrevBtn);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerBtnPressed(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerBtnPressed(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex + 1 < mQuestions.length) {
                    mCurrentIndex++;
                    setEnabledPrevBtn(true);
                } else {
                    showResultMessage();
                    mCurrentIndex = 0;
                    mCountCorrectAnswers = 0;
                    clearCompletedQuestions();
                    setEnabledPrevBtn(false);
                }
                setQuestion();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex--;
                if (mCurrentIndex == 0) {
                    setEnabledPrevBtn(false);
                }
                setQuestion();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putByteArray(KEY_COMPLETED_QUESTIONS, mIsQuestionsCompleted);
        outState.putInt(KEY_COUNT_CORRECT, mCountCorrectAnswers);
        outState.putBoolean(KEY_ENABLE_PREV_BTN, mEnablePrevBtn);
    }

    private void setQuestion() {
        int questionId = mQuestions[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(questionId);

        if (mIsQuestionsCompleted[mCurrentIndex] == 1) {
            lockAnswerBtns();
        } else {
            unlockAnswerBtns();
        }
    }

    private void answerBtnPressed(boolean isUserPressedTrue) {
        if (checkAnswer(isUserPressedTrue)) {
            mCountCorrectAnswers++;
        }

        mIsQuestionsCompleted[mCurrentIndex] = 1;
        lockAnswerBtns();
    }

    private boolean checkAnswer(boolean isUserPressedTrue) {
        boolean isAnswerTrue = mQuestions[mCurrentIndex].isAnswerTrue();

        int messageResId = (isUserPressedTrue == isAnswerTrue) ?
                R.string.correct_answer :
                R.string.incorrect_answer;
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        return isUserPressedTrue == isAnswerTrue;
    }

    private void lockAnswerBtns() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void unlockAnswerBtns() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void setEnabledPrevBtn(boolean isEnable) {
        mEnablePrevBtn = isEnable;
        mPrevButton.setEnabled(isEnable);
    }

    private void clearCompletedQuestions() {
        for (int i = 0; i < mIsQuestionsCompleted.length; i++) {
            mIsQuestionsCompleted[i] = 0;
        }
    }

    private void showResultMessage() {
        Toast.makeText(this, "Тест завершен! Ваша оценка - " + mCountCorrectAnswers, Toast.LENGTH_SHORT).show();
    }
}
