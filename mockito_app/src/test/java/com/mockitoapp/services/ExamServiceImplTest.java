package com.mockitoapp.services;

import com.mockitoapp.models.Exam;
import com.mockitoapp.repositories.ExamRepository;
import com.mockitoapp.repositories.ExamRepositoryImpl;
import com.mockitoapp.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    ExamRepository repo;
    @Mock
    QuestionRepository questionRepository;
    @InjectMocks
    ExamServiceImpl service;

    /*@BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        *//*repo = mock(ExamRepository.class);
        questionRepository = mock(QuestionRepository.class);
        service = new ExamServiceImpl(repo, questionRepository);*//*
    }*/

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

    @Test
    void testQuestionsExam() {
        when(repo.findALl()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionsByExam(anyLong())).thenReturn(Data.QUESTIONS);
        Exam exam = service.findExamByQuestion("Math");
        assertEquals(4, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("Geometry"));
    }

    @Test
    void testQuestionsExamVerify() {
        when(repo.findALl()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionsByExam(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = service.findExamByQuestion("Math");

        assertEquals(4, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("Geometry"));

        // verify call to methods
        verify(repo).findALl();
        verify(questionRepository).findQuestionsByExam(anyLong()); //anylong don't need to define an id by default

    }

    @Test
    void testExamExits() {
        when(repo.findALl()).thenReturn(Collections.emptyList());
        when(questionRepository.findQuestionsByExam(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = service.findExamByQuestion("Math2");

        assertNull(exam);

        // verify call to methods
        verify(repo).findALl();
        verify(questionRepository).findQuestionsByExam(anyLong()); //anylong don't need to define an id by default
    }

    @Test
    void testSaveExam() {

        //Behavior-driven development
        // Given
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTIONS);

        //when(repo.save(any(Exam.class))).thenReturn(Data.EXAM);

        // anonymous class
        when(repo.save(any(Exam.class))).then(new Answer<Exam>(){

            Long sequence = 8L;

            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }});

        //var exam = service.save(Data.EXAM);

        // When
        var exam = service.save(newExam);

        // then
        assertNotNull(exam.getId());
        assertEquals(8L, exam.getId());
        assertEquals("Biology", exam.getName());

        verify(repo).save(any(Exam.class));
        verify(questionRepository).saveQuestions(anyList());
    }

}