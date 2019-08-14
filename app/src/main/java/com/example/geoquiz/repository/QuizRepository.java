package com.example.geoquiz.repository;

import com.example.geoquiz.entities.Question;

public interface QuizRepository {

    Question getCurrentQuestion(int index);

    int getQuestionsCount();
}
