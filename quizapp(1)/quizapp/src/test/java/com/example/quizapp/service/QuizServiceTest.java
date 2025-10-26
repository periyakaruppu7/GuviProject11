package com.example.quizapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.quizapp.entity.Quiz;
import com.example.quizapp.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizService quizService;

    private Quiz quiz1;
    private Quiz quiz2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        quiz1 = new Quiz();
        quiz1.setId(1L);
        quiz1.setQuizname("Math Quiz");

        quiz2 = new Quiz();
        quiz2.setId(2L);
        quiz2.setQuizname("Science Quiz");
    }

    @Test
    void testGetAllQuizzes() {
        when(quizRepository.findAll()).thenReturn(Arrays.asList(quiz1, quiz2));

        List<Quiz> quizzes = quizService.getAllQuizzes();
        assertEquals(2, quizzes.size());
        assertTrue(quizzes.contains(quiz1));
        assertTrue(quizzes.contains(quiz2));
    }

    @Test
    void testGetQuizById_Found() {
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz1));

        Quiz quiz = quizService.getQuizById(1L);
        assertEquals(quiz1, quiz);
    }

    @Test
    void testGetQuizById_NotFound() {
        when(quizRepository.findById(99L)).thenReturn(Optional.empty());

        Quiz quiz = quizService.getQuizById(99L);
        assertNull(quiz);
    }

    @Test
    void testSaveQuiz() {
        when(quizRepository.save(quiz1)).thenReturn(quiz1);

        Quiz savedQuiz = quizService.saveQuiz(quiz1);
        assertEquals(quiz1, savedQuiz);
        verify(quizRepository, times(1)).save(quiz1);
    }

    @Test
    void testDeleteQuiz() {
        doNothing().when(quizRepository).deleteById(1L);

        quizService.deleteQuiz(1L);
        verify(quizRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetQuizByName_Found() {
        when(quizRepository.findByQuizname("Math Quiz")).thenReturn(Optional.of(quiz1));

        Quiz quiz = quizService.getQuizByName("Math Quiz");
        assertEquals(quiz1, quiz);
    }

    @Test
    void testGetQuizByName_NotFound() {
        when(quizRepository.findByQuizname("Unknown Quiz")).thenReturn(Optional.empty());

        Quiz quiz = quizService.getQuizByName("Unknown Quiz");
        assertNull(quiz);
    }
}
