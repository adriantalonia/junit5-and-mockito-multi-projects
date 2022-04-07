package com.mockitoapp.repositories;

import com.mockitoapp.models.Exam;

import java.util.Arrays;
import java.util.List;

public class ExamRepositoryImpl implements ExamRepository {
    @Override
    public List<Exam> findALl() {
        return Arrays.asList(new Exam(5L, "Math"), new Exam(6l, "Languajes"), new Exam(7l, "History"));
    }
}
