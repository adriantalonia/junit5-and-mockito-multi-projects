package com.mockitoapp.services;

import com.mockitoapp.models.Exam;
import com.mockitoapp.repositories.ExamRepository;
import com.mockitoapp.repositories.ExamRepositoryImpl;
import com.mockitoapp.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExamServiceImplTest {

    ExamRepository repo;
    ExamService service;
    QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        repo = mock(ExamRepository.class);
        questionRepository = mock(QuestionRepository.class);
        service = new ExamServiceImpl(repo, questionRepository);
    }

    @Test
    void findExamByName() {
        ExamRepository repo = new ExamRepositoryImpl();
        ExamService service = new ExamServiceImpl(repo, questionRepository);
        Exam exam = service.findExamByName("Math");
        assertNotNull(exam);
        assertEquals(5l, exam.getId());
        assertEquals("Math", exam.getName());
    }

    @Test
    void findExamByNameMockito() {
        List<Exam> data = Arrays.asList(new Exam(5L, "Math"), new Exam(6l, "Languajes"), new Exam(7l, "History"));
        when(repo.findALl()).thenReturn(data);
        Exam exam = service.findExamByName("Math");
        assertNotNull(exam);
        assertEquals(5l, exam.getId());
        assertEquals("Math", exam.getName());
    }

    @Test
    void findExamByNameMockitoOptional() {
        List<Exam> data = Arrays.asList(new Exam(5L, "Math"), new Exam(6l, "Languajes"), new Exam(7l, "History"));
        when(repo.findALl()).thenReturn(data);
        Optional<Exam> exam = service.findExamByNameOptional("Math");
        assertTrue(exam.isPresent());
        assertEquals(5l, exam.orElseThrow().getId());
        assertEquals("Math", exam.orElseThrow().getName());
    }

    @Test
    void findExamByNameMockitoEmptyList() {
        List<Exam> data = Collections.emptyList();
        when(repo.findALl()).thenReturn(data);
        Optional<Exam> exam = service.findExamByNameOptional("Math");
        assertFalse(exam.isPresent());
    }
}