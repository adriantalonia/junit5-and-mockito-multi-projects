package com.mockitoapp.repositories;

import java.util.List;

public interface QuestionRepository {
    List<String> findQuestionsByExam(Long id);
}
