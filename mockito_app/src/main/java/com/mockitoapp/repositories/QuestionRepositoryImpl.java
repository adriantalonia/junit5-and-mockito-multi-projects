package com.mockitoapp.repositories;

import com.mockitoapp.Data;

import java.util.List;

public class QuestionRepositoryImpl implements QuestionRepository{
    @Override
    public List<String> findQuestionsByExamId(Long id) {
        System.out.println("QuestionRepositoryImpl findQuestionsByExamId");
        return Data.QUESTIONS;
    }

    @Override
    public void saveQuestions(List<String> questions) {
        System.out.println("QuestionRepositoryImpl saveQuestions");
    }
}
