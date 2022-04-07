package com.mockitoapp.services;

import com.mockitoapp.models.Exam;
import com.mockitoapp.repositories.ExamRepository;
import com.mockitoapp.repositories.ExamRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExamServiceImplTest {

    @Test
    void findExamByName() {
        ExamRepository repo = new ExamRepositoryImpl();
        ExamService service = new ExamServiceImpl(repo);
        Exam exam = service.findExamByName("Math");
        assertNotNull(exam);
        assertEquals(5l, exam.getId());
        assertEquals("Math", exam.getName());
    }

    @Test
    void findExamByNameMockito() {
        ExamRepository repo = mock(ExamRepository.class);
        ExamService service = new ExamServiceImpl(repo);
        List<Exam> data = Arrays.asList(new Exam(5L, "Math"), new Exam(6l, "Languajes"), new Exam(7l, "History"));

        when(repo.findALl()).thenReturn(data);

        Exam exam = service.findExamByName("Math");
        assertNotNull(exam);
        assertEquals(5l, exam.getId());
        assertEquals("Math", exam.getName());
    }

    @Test
    void findExamByNameMockitoOptional() {
        ExamRepository repo = mock(ExamRepository.class);
        ExamService service = new ExamServiceImpl(repo);
        List<Exam> data = Collections.emptyList();

        when(repo.findALl()).thenReturn(data);

        Optional<Exam> exam = service.findExamByNameOptional("Math");
        assertTrue(exam.isPresent());
        assertEquals(5l, exam.orElseThrow().getId());
        assertEquals("Math", exam.orElseThrow().getName());
    }
}