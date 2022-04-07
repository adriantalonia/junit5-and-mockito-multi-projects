package com.mockitoapp.services;

import com.mockitoapp.models.Exam;
import com.mockitoapp.repositories.ExamRepository;

import java.util.Optional;

public class ExamServiceImpl implements ExamService {

    private ExamRepository repository;

    public ExamServiceImpl(ExamRepository repository) {
        this.repository = repository;
    }

    @Override
    public Exam findExamByName(String name) {
        Optional<Exam> exam = repository.findALl().stream().filter(e -> e.getName().contains(name)).findFirst();
        if(exam.isPresent()) {
            return exam.orElseThrow();
        }
        return null;
    }

    @Override
    public Optional<Exam> findExamByNameOptional(String name) {
        return repository.findALl().stream().filter(e -> e.getName().contains(name)).findFirst();
    }
}
