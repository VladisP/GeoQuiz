package com.example.geoquiz.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geoquiz.R;
import com.example.geoquiz.presenter.QuizPresenter;
import com.example.geoquiz.presenter.QuizPresenterImpl;
import com.example.geoquiz.repository.QuizRepositoryImpl;

public class QuizActivity extends AppCompatActivity implements QuizPresenter.QuizView {

    private static final String KEY_SAVE_BUNDLE = "save_bundle";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private QuizPresenter mQuizPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            Bundle saveBundle = savedInstanceState.getBundle(KEY_SAVE_BUNDLE);

            if (saveBundle != null) {
                mQuizPresenter = new QuizPresenterImpl(this, new QuizRepositoryImpl(), saveBundle);
            } else {
                mQuizPresenter = new QuizPresenterImpl(this, new QuizRepositoryImpl());
            }

        } else {
            mQuizPresenter = new QuizPresenterImpl(this, new QuizRepositoryImpl());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mQuestionTextView = findViewById(R.id.question_text_view);
        mTrueButton = findViewById(R.id.btn_true);
        mFalseButton = findViewById(R.id.btn_false);
        mCheatButton = findViewById(R.id.cheat_button);
        mNextButton = findViewById(R.id.next_btn);
        mPrevButton = findViewById(R.id.prev_btn);

        setQuestion();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuizPresenter.onAnswerButtonPressed(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuizPresenter.onAnswerButtonPressed(false);
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CheatActivity.newIntent(QuizActivity.this, mQuizPresenter.isAnswerTrue());
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuizPresenter.onNextButtonPressed();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuizPresenter.onPrevButtonPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            if (CheatActivity.wasAnswerShown(data)) {
                mQuizPresenter.userIsCheater();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_SAVE_BUNDLE, mQuizPresenter.getSaveBundle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQuizPresenter.onQuizViewDestroyed();
    }

    @Override
    public void setQuestion() {
        int questionId = mQuizPresenter.getQuestionTextId();
        mQuestionTextView.setText(questionId);

        if (mQuizPresenter.isQuestionCompleted()) {
            lockAnswerButtons();
        } else {
            unlockAnswerButtons();
        }

        if (mQuizPresenter.isQuestionFirst()) {
            lockPrevButton();
        } else {
            unlockPrevButton();
        }
    }

    @Override
    public void showMessage(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void lockAnswerButtons() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    @Override
    public void unlockAnswerButtons() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    @Override
    public void lockPrevButton() {
        mPrevButton.setEnabled(false);
    }

    @Override
    public void unlockPrevButton() {
        mPrevButton.setEnabled(true);
    }
}
