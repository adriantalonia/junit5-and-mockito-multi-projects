package com.mockitoapp.repositories;

import java.util.List;

public interface QuestionRepository {
    List<String> findQuestionsByExamId(Long id);
    void saveQuestions(List<String> questions);
}
