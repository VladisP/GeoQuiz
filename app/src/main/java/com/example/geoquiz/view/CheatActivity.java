package com.example.geoquiz.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.geoquiz.R;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown";
    private static final String KEY_CHEATER = "cheater";

    private boolean mAnswerIsTrue;
    private boolean mIsUserCheater = false;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        if (savedInstanceState != null) {
            mIsUserCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsUserCheater) {
            setAnswerText();
            setAnswerShownResult();
        }

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswerText();
                mIsUserCheater = true;
                setAnswerShownResult();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_CHEATER, mIsUserCheater);
    }

    private void setAnswerText() {
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.correct_answer);
        } else {
            mAnswerTextView.setText(R.string.incorrect_answer);
        }
    }

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, true);
        setResult(RESULT_OK, data);
    }
}
