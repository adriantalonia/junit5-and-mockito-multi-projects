package com.mockitoapp.services;

import com.mockitoapp.models.Exam;
import com.mockitoapp.repositories.ExamRepository;
import com.mockitoapp.repositories.QuestionRepository;

import java.util.Optional;

public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;
    private QuestionRepository questionRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Exam findExamByName(String name) {
        Optional<Exam> exam = examRepository.findALl().stream().filter(e -> e.getName().contains(name)).findFirst();
        if (exam.isPresent()) {
            return exam.orElseThrow();
        }
        return null;
    }

    @Override
    public Optional<Exam> findExamByNameOptional(String name) {
        return examRepository.findALl().stream().filter(e -> e.getName().contains(name)).findFirst();
    }

    @Override
    public Exam findExamByQuestion(String name) {
        Exam exam = findExamByNameOptional(name).orElseThrow();
        exam.setQuestions(questionRepository.findQuestionsByExam(exam.getId()));
        return exam;
    }

    @Override
    public Exam save(Exam exam) {
        if (!exam.getQuestions().isEmpty()) {
            questionRepository.saveQuestions(exam.getQuestions());
        }
        return examRepository.save(exam);
    }
}
