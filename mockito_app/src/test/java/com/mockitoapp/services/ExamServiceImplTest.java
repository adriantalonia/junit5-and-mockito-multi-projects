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
class ExamServiceImplTest {

    /*@Mock
    ExamRepository repo;
    @Mock
    QuestionRepository questionRepository;*/

    @Mock
    ExamRepositoryImpl repo;
    @Mock
    QuestionRepositoryImpl questionRepository;
    @InjectMocks
    ExamServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

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
        when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        Exam exam = service.findExamByQuestion("Math");
        assertEquals(4, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("Geometry"));
    }

    @Test
    void testQuestionsExamVerify() {
        when(repo.findALl()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = service.findExamByQuestion("Math");

        assertEquals(4, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("Geometry"));

        // verify call to methods
        verify(repo).findALl();
        verify(questionRepository).findQuestionsByExamId(anyLong()); //anylong don't need to define an id by default

    }

    @Test
    void testExamExits() {
        when(repo.findALl()).thenReturn(Collections.emptyList());
        when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = service.findExamByQuestion("Math2");

        assertNull(exam);

        // verify call to methods
        verify(repo).findALl();
        verify(questionRepository).findQuestionsByExamId(anyLong()); //anylong don't need to define an id by default
    }

    @Test
    void testSaveExam() {

        //Behavior-driven development
        // Given
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTIONS);

        //when(repo.save(any(Exam.class))).thenReturn(Data.EXAM);

        // anonymous class
        when(repo.save(any(Exam.class))).then(new Answer<Exam>() {

            Long sequence = 8L;

            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        });

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

    @Test
    void testExceptionManage() {
        when(repo.findALl()).thenReturn(Data.EXAMS_ID_NULL);
        when(questionRepository.findQuestionsByExamId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamByQuestion("Math");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(repo).findALl();
        verify(questionRepository).findQuestionsByExamId(isNull());
    }

    // argument matchers
    // validate arguments set to methods
    @Test
    void testArgumentsMatchers() {
        when(repo.findALl()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        service.findExamByQuestion("Math");

        verify(repo).findALl();
        // specify argument value
        verify(questionRepository).findQuestionsByExamId(argThat(arg -> arg != null && arg.equals(5L)));
    }

    // inner test class
    public static class MyArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "custom message when test case fail " +
                    "Argument must be different of null and granter than 0";
        }
    }

    @Test
    void testArgumentsMatchersTwo() {
        when(repo.findALl()).thenReturn(Data.EXAMS_ID_NEGATIVE);
        when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        service.findExamByQuestion("Math");

        verify(repo).findALl();
        verify(questionRepository).findQuestionsByExamId(argThat(new MyArgsMatchers()));
    }

    @Test
    void testArgumentsMatchersThree() {
        when(repo.findALl()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        service.findExamByQuestion("Math");

        verify(repo).findALl();
        verify(questionRepository).findQuestionsByExamId(argThat((argument) -> argument != null && argument > 0));
    }

    @Test
    void testArgumentCaptor() {
        when(repo.findALl()).thenReturn(Data.EXAMS);
        //when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        service.findExamByQuestion("Math");

        // instance use annotation
        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(questionRepository).findQuestionsByExamId(captor.capture()); // recover value of the test

        assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Exam exam = Data.EXAM;
        exam.setQuestions(Data.QUESTIONS);
        // doThrow when use a void method
        doThrow(IllegalArgumentException.class).when(questionRepository).saveQuestions(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.save(exam);
        });
    }

    @Test
    void testDoAnswer() {
        when(repo.findALl()).thenReturn(Data.EXAMS);
        // custom response
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5L ? Data.QUESTIONS: Collections.emptyList();
        }).when(questionRepository).findQuestionsByExamId(anyLong());

        Exam exam = service.findExamByQuestion("Math");
        assertEquals(5L, exam.getId());
        assertEquals("Math", exam.getName());

        verify(questionRepository).findQuestionsByExamId(anyLong());
    }

    @Test
    void testSaveExamDoAnswer() {

        //Behavior-driven development
        // Given
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTIONS);

        doAnswer(new Answer<Exam>() {

            Long sequence = 8L;

            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        }).when(repo).save(any(Exam.class));


        // When
        var exam = service.save(newExam);

        // then
        assertNotNull(exam.getId());
        assertEquals(8L, exam.getId());
        assertEquals("Biology", exam.getName());

        verify(repo).save(any(Exam.class));
        verify(questionRepository).saveQuestions(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(repo.findALl()).thenReturn(Data.EXAMS);
        //when(questionRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        // call the real methods
        doCallRealMethod().when(questionRepository).findQuestionsByExamId(anyLong());

        Exam exam = service.findExamByQuestion("Math");
        assertEquals(5L, exam.getId());
        assertEquals("Math", exam.getName());
    }

}