package com.mockitoapp.services;

import com.mockitoapp.models.Exam;

import java.util.Optional;

public interface ExamService {
    Exam findExamByName(String name);
    Optional<Exam> findExamByNameOptional(String name);
    Exam findExamByQuestion(String name);
    Exam save(Exam exam);
}
