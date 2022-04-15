package com.mockitoapp.services;

import com.mockitoapp.Data;
import com.mockitoapp.models.Exam;
import com.mockitoapp.repositories.ExamRepository;
import com.mockitoapp.repositories.ExamRepositoryImpl;
import com.mockitoapp.repositories.QuestionRepository;
import com.mockitoapp.repositories.QuestionRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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
class ExamServiceImplSpyTest {

    @Spy
    ExamRepositoryImpl repo;
    @Spy
    QuestionRepositoryImpl questionRepository;
    @InjectMocks
    ExamServiceImpl service;

    @Test
    void testSpyAnnotation() {


        List<String> questions = Arrays.asList("Geometry");
        //when(questionRepo.findQuestionsByExamId(anyLong())).thenReturn(questions);
        //when(questionRepo.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        doReturn(questions).when(questionRepository).findQuestionsByExamId(anyLong());

        Exam exam =  service.findExamByQuestion("Math");

        assertEquals(5, exam.getId());
        assertEquals("Math", exam.getName());
        assertEquals(1, exam.getQuestions().size());
        //assertEquals(4, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("Geometry"));

        verify(repo).findALl();
        verify(questionRepository).findQuestionsByExamId(anyLong());
    }

}